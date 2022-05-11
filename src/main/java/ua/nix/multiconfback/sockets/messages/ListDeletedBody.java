package ua.nix.multiconfback.sockets.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListDeletedBody {

    private String listId;
    private boolean isPublic;

}
