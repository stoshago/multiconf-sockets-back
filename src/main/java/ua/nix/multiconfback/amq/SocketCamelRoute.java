package ua.nix.multiconfback.amq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.nix.multiconfback.amq.processor.PullPrivateListsProcessor;

import static ua.nix.multiconfback.util.Constants.PROCESSOR_HEADER;

@Slf4j
@RequiredArgsConstructor
@Component
public class SocketCamelRoute extends RouteBuilder {

    @Value("${amq.broker.topic}")
    private String topic;

    private final BrokerMessageProcessor messageProcessor;
    private final PullPrivateListsProcessor pullPrivateListsProcessor;

    @Override
    public void configure() {
        from("activemq:topic:" + topic)
                .log(">>> Read message: ${body}")
                .choice()
                                    .when(compareProcessors(PullPrivateListsProcessor.class.getSimpleName()))
                                        .log("Handling by PullPrivateListsProcessor")
                                        .process(pullPrivateListsProcessor)
                                        .endChoice()
                                    .otherwise()
                                        .log( "No processor found for ${in.headers." + PROCESSOR_HEADER + "} header")
                                        .end()
                .process(messageProcessor);
    }

    private Predicate compareProcessors(String processorName) {
        return header(PROCESSOR_HEADER).isEqualTo(processorName);
    }

}
