package ua.nix.multiconfback.amq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SocketCamelRoute extends RouteBuilder {

    @Value("${amq.broker.topic}")
    private String topic;

    private final BrokerMessageProcessor messageProcessor;

    @Override
    public void configure() {
        from("activemq:topic:" + topic)
                .log(">>> Read message: ${body}")
                .process(messageProcessor);
    }

}
