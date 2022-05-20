package ua.nix.multiconfback.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import ua.nix.multiconfback.api.model.TodoItemDto;
import ua.nix.multiconfback.api.model.TodoListDto;
import ua.nix.multiconfback.sockets.SocketSessionStorage;
import ua.nix.multiconfback.sockets.messages.ItemCompletedBody;
import ua.nix.multiconfback.sockets.messages.ItemDeletedBody;
import ua.nix.multiconfback.sockets.messages.ListDeletedBody;
import ua.nix.multiconfback.sockets.messages.WsMessage;

import java.io.IOException;

import static ua.nix.multiconfback.util.Constants.ITEM_ADDED_TOPIC;
import static ua.nix.multiconfback.util.Constants.ITEM_COMPLETED_TOPIC;
import static ua.nix.multiconfback.util.Constants.ITEM_DELETED_TOPIC;
import static ua.nix.multiconfback.util.Constants.LIST_ADDED_TOPIC;
import static ua.nix.multiconfback.util.Constants.LIST_DELETED_TOPIC;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final SocketSessionStorage sessionStorage;
    private final AuthService authService;

    public void notifyAll(WsMessage message) {
        sessionStorage.getSessionMap().forEach((key, value) -> value.forEach((session) -> {
            try {
                session.sendMessage(new TextMessage(new Gson().toJson(message)));
            } catch (IOException e) {
                log.error("Error occurred during message sending over WebSockets", e);
            }
        }));
    }

    public void notifyUser(String username, WsMessage message) {
        sessionStorage.getSessionMap().get(username).forEach((session) -> {
            try {
                session.sendMessage(new TextMessage(new Gson().toJson(message)));
            } catch (IOException e) {
                log.error("Error occurred during message sending over WebSockets", e);
            }
        });
    }

    public void notifyCurrentUser(WsMessage message) {
        String username = authService.getCurrentUser().getUsername();
        notifyUser(username, message);
    }

    public void notifyListItemCompleted(String itemId, boolean isCompleted, boolean notifyAll) {
        WsMessage message = WsMessage.builder()
                .topic(ITEM_COMPLETED_TOPIC)
                .data(ItemCompletedBody.builder()
                        .itemId(itemId)
                        .isCompleted(isCompleted)
                        .build())
                .build();
        notify(message, notifyAll);
    }

    public void notifyListItemAdded(TodoItemDto itemDto, boolean notifyAll) {
        WsMessage message = WsMessage.builder()
                .topic(ITEM_ADDED_TOPIC)
                .data(itemDto)
                .build();
        notify(message, notifyAll);
    }

    public void notifyListItemDeleted(String listId, String itemId, boolean notifyAll) {
        WsMessage message = WsMessage.builder()
                .topic(ITEM_DELETED_TOPIC)
                .data(ItemDeletedBody.builder()
                        .listId(listId)
                        .itemId(itemId)
                        .build())
                .build();
        notify(message, notifyAll);
    }

    public void notifyListDeleted(String listId, boolean isPublic) {
        WsMessage message = WsMessage.builder()
                .topic(LIST_DELETED_TOPIC)
                .data(new ListDeletedBody(listId, isPublic))
                .build();
        if (isPublic) {
            notifyAll(message);
        } else {
            notifyCurrentUser(message);
        }
    }

    public void notifyListAdded(TodoListDto list, boolean notifyAll) {
        WsMessage message = WsMessage.builder()
                .topic(LIST_ADDED_TOPIC)
                .data(list)
                .build();
        notify(message, notifyAll);
    }

    private void notify(WsMessage message, boolean notifyAll) {
        if (notifyAll) {
            notifyAll(message);
        } else {
            notifyCurrentUser(message);
        }
    }

}
