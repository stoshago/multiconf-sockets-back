package ua.nix.multiconfback.amq;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import ua.nix.multiconfback.service.NotificationService;
import ua.nix.multiconfback.sockets.messages.WsMessage;

import java.util.Optional;

import static ua.nix.multiconfback.util.Constants.RECIPIENT_HEADER;

@Component
@RequiredArgsConstructor
public class BrokerMessageProcessor implements Processor {

    private final NotificationService notificationService;
    private final Gson gson;

    @Override
    public void process(Exchange exchange) {
        WsMessage message = parseMessage(exchange);
        Optional<Object> recipientUsername = getHeader(exchange, RECIPIENT_HEADER);
        if (recipientUsername.isEmpty()) {
            notificationService.notifyAll(message);
            return;
        }
        notificationService.notifyUser((String) recipientUsername.get(), message);
    }

    private WsMessage parseMessage(Exchange exchange) {
        String jsonBody = exchange.getIn().getBody(String.class);
        return gson.fromJson(jsonBody, WsMessage.class);
    }

    private Optional<Object> getHeader(Exchange exchange, String headerName) {
        return Optional.ofNullable(exchange.getIn().getHeader(headerName));
    }
}
