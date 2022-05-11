package ua.nix.multiconfback.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ua.nix.multiconfback.sockets.SocketHandler;
import ua.nix.multiconfback.sockets.TokenHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final SocketHandler socketHandler;
    private final TokenHandshakeInterceptor handshakeInterceptor;

    public WebSocketConfig(SocketHandler socketHandler, TokenHandshakeInterceptor handshakeInterceptor) {
        this.socketHandler = socketHandler;
        this.handshakeInterceptor = handshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(socketHandler, "/web-socket")
                .addInterceptors(handshakeInterceptor)
                .setAllowedOriginPatterns("*");
    }

}
