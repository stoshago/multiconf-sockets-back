package ua.nix.multiconfback.sockets.messages;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDeletedBody {

    private String itemId;
    private String listId;

}
