package ua.nix.multiconfback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExchangeBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.nix.multiconfback.sockets.messages.WsMessage;

import static ua.nix.multiconfback.util.Constants.AMQ_TOPIC_PREFIX;
import static ua.nix.multiconfback.util.Constants.RECIPIENT_HEADER;

@Slf4j
@RequiredArgsConstructor
@Service
public class BrokerNotificationService {

    @Value("${amq.broker.topic}")
    private String topic;

    private final ProducerTemplate producerTemplate;
    private final AuthService authService;

    public void notifyAll(WsMessage socketMessage) {
        log.info(String.format(
                "sending with convertAndSend() to topic <%s> and all users",
                topic));
        CamelContext camelContext = producerTemplate.getCamelContext();
        Exchange exchange = new ExchangeBuilder(camelContext)
                .withBody(socketMessage)
                .build();
        producerTemplate.send(getTopicDestination(topic), exchange);
    }

    public void notifyUser(WsMessage socketMessage, String username) {
        log.info(String.format(
                "sending with convertAndSend() to topic <%s> and user <%s>",
                topic,
                username));
        CamelContext camelContext = producerTemplate.getCamelContext();
        Exchange exchange = new ExchangeBuilder(camelContext)
                .withHeader(RECIPIENT_HEADER, username)
                .withBody(socketMessage)
                .build();
        producerTemplate.send(getTopicDestination(topic), exchange);
    }

    private String getTopicDestination(String topic) {
        return AMQ_TOPIC_PREFIX + topic;
    }
}
