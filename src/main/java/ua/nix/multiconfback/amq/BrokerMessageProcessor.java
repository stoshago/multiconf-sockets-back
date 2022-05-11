package ua.nix.multiconfback.amq;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import ua.nix.multiconfback.service.NotificationService;
import ua.nix.multiconfback.sockets.messages.WsMessage;

import static ua.nix.multiconfback.util.Constants.RECIPIENT_HEADER;

@Component
@AllArgsConstructor
public class BrokerMessageProcessor implements Processor {

    private final NotificationService notificationService;


    @Override
    public void process(Exchange exchange) {
        WsMessage message = parseMessage(exchange);
        String recipientUsername = (String) getHeader(exchange, RECIPIENT_HEADER);
        if (recipientUsername == null) {
            notificationService.notifyAll(message);
            return;
        }
        notificationService.notifyUser(recipientUsername, message);
    }

    private WsMessage parseMessage(Exchange exchange) {
        String jsonBody = exchange.getIn().getBody(String.class);
        return new Gson().fromJson(jsonBody, WsMessage.class);
    }

    private Object getHeader(Exchange exchange, String headerName) {
        return exchange.getIn().getHeader(headerName);
    }
}
