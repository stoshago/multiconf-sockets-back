package ua.nix.multiconfback.sockets;

import lombok.RequiredArgsConstructor;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import ua.nix.multiconfback.security.JwtService;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.stream.Collectors;

import static ua.nix.multiconfback.util.Constants.AUTH_ATTRIBUTE;

@RequiredArgsConstructor
@Component
public class TokenHandshakeInterceptor implements HandshakeInterceptor {

    private static final String TOKEN_KEY = "token";

    private final JwtService tokenService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String securityToken = parseUrlParams(request.getURI()).get(TOKEN_KEY);
        if (!tokenService.isTokenValid(securityToken)) {
            return false;
        }
        attributes.put(AUTH_ATTRIBUTE, tokenService.getUsernameFromToken(securityToken));
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // no implementation required
    }

    // HELPER METHODS
    private Map<String, String> parseUrlParams(URI uri) {
        return URLEncodedUtils.parse(uri, Charset.defaultCharset())
                .stream()
                .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
    }
}
