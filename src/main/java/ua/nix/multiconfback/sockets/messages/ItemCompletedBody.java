package ua.nix.multiconfback.sockets.messages;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemCompletedBody {

    private String itemId;
    private boolean isCompleted;

}
