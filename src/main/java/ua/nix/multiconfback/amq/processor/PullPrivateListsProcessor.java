package ua.nix.multiconfback.amq.processor;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import ua.nix.multiconfback.model.TodoList;
import ua.nix.multiconfback.model.User;
import ua.nix.multiconfback.service.TodoListService;
import ua.nix.multiconfback.service.UserService;
import ua.nix.multiconfback.sockets.messages.WsMessage;
import ua.nix.multiconfback.util.DtoConverter;

import java.util.List;
import java.util.Map;

import static ua.nix.multiconfback.util.Constants.PRIVATE_LISTS_UPDATED_TOPIC;

@Component
@RequiredArgsConstructor
public class PullPrivateListsProcessor implements Processor {

    private final TodoListService todoListService;
    private final UserService userService;

    @Override
    public void process(Exchange exchange) {
        Object body = returnData(exchange);
        exchange.getIn().setBody(new Gson().toJson(body));
    }

    protected WsMessage returnData(Exchange exchange) {
        // there is no info about current user from context here
        String userName = getUsername(exchange);
        User currentUser = userService.findByLogin(userName);

        List<TodoList> updatedLists = todoListService.findAllByCreatedByAndIsPublicFalseOrderByCreatedDate(currentUser);
        return WsMessage.builder()
                .topic(PRIVATE_LISTS_UPDATED_TOPIC)
                .data(DtoConverter.convertDetailedTodoLists(updatedLists))
                .build();
    }

    private String getUsername(Exchange exchange) {
        return (String) new Gson().fromJson(exchange.getIn().getBody(String.class), Map.class).get("username");
    }
}
