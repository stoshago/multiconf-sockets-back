package ua.nix.multiconfback.sockets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import static ua.nix.multiconfback.util.Constants.AUTH_ATTRIBUTE;

@Slf4j
@RequiredArgsConstructor
@Component
public class SocketHandler extends TextWebSocketHandler {

    private final SocketSessionStorage sessionStorage;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // can be used to handle messages sent back from Socket client
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String username = (String) session.getAttributes().get(AUTH_ATTRIBUTE);
        sessionStorage.addSession(username, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        String username = (String) session.getAttributes().get(AUTH_ATTRIBUTE);
        sessionStorage.deleteSession(username, session);
    }

}
