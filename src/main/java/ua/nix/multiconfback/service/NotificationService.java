package ua.nix.multiconfback.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ua.nix.multiconfback.sockets.SocketSessionStorage;
import ua.nix.multiconfback.sockets.messages.WsMessage;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final SocketSessionStorage sessionStorage;
    private final Gson gson;

    public void notifyAll(WsMessage message) {
        sessionStorage.getSessionMap().values().stream()
                .flatMap(Collection::stream)
                .forEach(session -> notify(session, message));
    }

    public void notifyUser(String username, WsMessage message) {
        sessionStorage.getUserSessions(username).forEach(session -> notify(session, message));
    }

    private void notify(WebSocketSession session, WsMessage message) {
        try {
            session.sendMessage(new TextMessage(gson.toJson(message)));
        } catch (IOException e) {
            log.error("Error occurred during message sending over WebSockets", e);
        }
    }

}
