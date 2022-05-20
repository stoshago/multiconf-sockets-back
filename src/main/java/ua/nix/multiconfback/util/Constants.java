package ua.nix.multiconfback.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String TOKEN_HEADER_SUFFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";

    public static final String AUTH_ATTRIBUTE = "auth";

    // TOPICS
    public static final String ITEM_COMPLETED_TOPIC = "item-completed";
    public static final String ITEM_ADDED_TOPIC = "item-added";
    public static final String ITEM_DELETED_TOPIC = "item-deleted";
    public static final String LIST_ADDED_TOPIC = "list-added";
    public static final String LIST_DELETED_TOPIC = "list-deleted";
    public static final String PRIVATE_LISTS_UPDATED_TOPIC = "private-lists-update";

    public static final String AMQ_TOPIC_PREFIX = "activeMqComponent:topic:";
    public static final String RECIPIENT_HEADER = "recipient-username";
    public static final String PROCESSOR_HEADER = "processor-name";

}
