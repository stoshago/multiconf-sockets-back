package ua.nix.multiconfback.amq.processor;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ua.nix.multiconfback.model.TodoList;
import ua.nix.multiconfback.model.User;
import ua.nix.multiconfback.service.TodoListService;
import ua.nix.multiconfback.sockets.messages.WsMessage;
import ua.nix.multiconfback.util.DtoMapper;

import java.util.List;
import java.util.Map;

import static ua.nix.multiconfback.util.Constants.PRIVATE_LISTS_UPDATED_TOPIC;

@Component
@RequiredArgsConstructor
public class PullPrivateListsProcessor implements Processor {

    private final TodoListService todoListService;
    private final UserDetailsService userService;
    private final DtoMapper dtoMapper;
    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        Object body = returnData(exchange);
        exchange.getIn().setBody(gson.toJson(body));
    }

    protected WsMessage returnData(Exchange exchange) {
        // there is no info about current user from context here
        String userName = getUsername(exchange);
        User currentUser = (User) userService.loadUserByUsername(userName);

        List<TodoList> updatedLists = todoListService.findAllByCreatedByAndIsPublicFalseOrderByCreatedDate(currentUser);
        return WsMessage.builder()
                .topic(PRIVATE_LISTS_UPDATED_TOPIC)
                .data(dtoMapper.convertDetailedTodoLists(updatedLists))
                .build();
    }

    private String getUsername(Exchange exchange) {
        return (String) gson.fromJson(exchange.getIn().getBody(String.class), Map.class).get("username");
    }
}
