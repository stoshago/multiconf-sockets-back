package ua.nix.multiconfback.util;

public interface Constants {

    String TOKEN_HEADER_SUFFIX = "Bearer ";
    String AUTH_HEADER = "Authorization";

    String AUTH_ATTRIBUTE = "auth";

    // TOPICS
    String ITEM_COMPLETED_TOPIC = "item-completed";
    String ITEM_ADDED_TOPIC = "item-added";
    String ITEM_DELETED_TOPIC = "item-deleted";
    String LIST_ADDED_TOPIC = "list-added";
    String LIST_DELETED_TOPIC = "list-deleted";

    String AMQ_TOPIC_PREFIX = "activeMqComponent:topic:";
    String RECIPIENT_HEADER = "recipient-username";

}
