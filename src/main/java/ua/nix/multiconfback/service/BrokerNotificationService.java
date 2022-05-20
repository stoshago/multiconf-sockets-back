package ua.nix.multiconfback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExchangeBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.nix.multiconfback.amq.processor.ProcessSpec;
import ua.nix.multiconfback.amq.processor.PullPrivateListsProcessor;
import ua.nix.multiconfback.api.model.TodoItemDto;
import ua.nix.multiconfback.api.model.TodoListDto;
import ua.nix.multiconfback.sockets.messages.ItemCompletedBody;
import ua.nix.multiconfback.sockets.messages.ItemDeletedBody;
import ua.nix.multiconfback.sockets.messages.ListDeletedBody;
import ua.nix.multiconfback.sockets.messages.WsMessage;

import java.util.Map;

import static ua.nix.multiconfback.util.Constants.AMQ_TOPIC_PREFIX;
import static ua.nix.multiconfback.util.Constants.ITEM_ADDED_TOPIC;
import static ua.nix.multiconfback.util.Constants.ITEM_COMPLETED_TOPIC;
import static ua.nix.multiconfback.util.Constants.ITEM_DELETED_TOPIC;
import static ua.nix.multiconfback.util.Constants.LIST_ADDED_TOPIC;
import static ua.nix.multiconfback.util.Constants.LIST_DELETED_TOPIC;
import static ua.nix.multiconfback.util.Constants.PROCESSOR_HEADER;
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
        log.info("sending with convertAndSend() to topic <{}> and all users", topic);
        CamelContext camelContext = producerTemplate.getCamelContext();
        Exchange exchange = new ExchangeBuilder(camelContext)
                .withBody(socketMessage)
                .build();
        producerTemplate.send(getTopicDestination(topic), exchange);
    }

    public void notifyUser(WsMessage socketMessage, String username) {
        log.info("sending with convertAndSend() to topic <{}> and user <{}>", topic, username);
        CamelContext camelContext = producerTemplate.getCamelContext();
        Exchange exchange = new ExchangeBuilder(camelContext)
                .withHeader(RECIPIENT_HEADER, username)
                .withBody(socketMessage)
                .build();
        producerTemplate.send(getTopicDestination(topic), exchange);
    }

    public void notifyListItemCompleted(String itemId, boolean isCompleted, boolean notifyAll) {
        WsMessage message = WsMessage.builder()
                .topic(ITEM_COMPLETED_TOPIC)
                .data(ItemCompletedBody.builder()
                        .itemId(itemId)
                        .isCompleted(isCompleted)
                        .build())
                .build();
        notify(message, notifyAll);
    }

    public void notifyListItemAdded(TodoItemDto itemDto, boolean notifyAll) {
        WsMessage message = WsMessage.builder()
                .topic(ITEM_ADDED_TOPIC)
                .data(itemDto)
                .build();
        notify(message, notifyAll);
    }

    public void notifyListItemDeleted(String listId, String itemId, boolean notifyAll) {
        WsMessage message = WsMessage.builder()
                .topic(ITEM_DELETED_TOPIC)
                .data(ItemDeletedBody.builder()
                        .listId(listId)
                        .itemId(itemId)
                        .build())
                .build();
        notify(message, notifyAll);
    }

    public void notifyListDeleted(String listId, boolean isPublic) {
        WsMessage message = WsMessage.builder()
                .topic(LIST_DELETED_TOPIC)
                .data(new ListDeletedBody(listId, isPublic))
                .build();
        if (isPublic) {
            notifyAll(message);
        } else {
            notifyCurrentUser(message);
        }
    }

    public void notifyListAdded(TodoListDto list, boolean notifyAll) {
        WsMessage message = WsMessage.builder()
                .topic(LIST_ADDED_TOPIC)
                .data(list)
                .build();
        notify(message, notifyAll);
    }

    public void notifyPrivateListsUpdated() {
        notifySpec(ProcessSpec.builder()
                .specData(Map.of("username", authService.getCurrentUser().getUsername()))
                .processorName(PullPrivateListsProcessor.class.getSimpleName())
                .build());
    }

    public void notifySpec(ProcessSpec spec) {
        CamelContext camelContext = producerTemplate.getCamelContext();

        String username = authService.getCurrentUser().getUsername();
        Exchange exchange = new ExchangeBuilder(camelContext)
                .withHeader(PROCESSOR_HEADER, spec.getProcessorName())
                .withHeader(RECIPIENT_HEADER, username)
                .withBody(spec.getSpecData())
                .build();

        // simulate long term process update
        producerTemplate.send(getTopicDestination(topic), exchange);
    }


    private void notify(WsMessage message, boolean notifyAll) {
        if (notifyAll) {
            notifyAll(message);
        } else {
            notifyCurrentUser(message);
        }
    }

    private void notifyCurrentUser(WsMessage message) {
        String username = authService.getCurrentUser().getUsername();
        notifyUser(message, username);
    }

    private String getTopicDestination(String topic) {
        return AMQ_TOPIC_PREFIX + topic;
    }
}
