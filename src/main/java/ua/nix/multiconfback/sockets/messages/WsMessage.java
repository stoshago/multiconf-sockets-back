package ua.nix.multiconfback.sockets.messages;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WsMessage {

    private String topic;
    private Object data;

}
