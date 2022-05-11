package ua.nix.multiconfback.sockets;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class SocketSessionStorage {

    private final Map<String, List<WebSocketSession>> sessionMap = new HashMap<>();

    public void addSession(String username, WebSocketSession session) {
        List<WebSocketSession> userSessions = sessionMap.get(username);
        if (userSessions == null) {
            userSessions = new LinkedList<>();
        }
        userSessions.add(session);
        sessionMap.put(username, userSessions);
    }

    public void deleteSession(String username, WebSocketSession session) {
        sessionMap.get(username).remove(session);
    }

    public Map<String, List<WebSocketSession>> getSessionMap() {
        return sessionMap;
    }
}
