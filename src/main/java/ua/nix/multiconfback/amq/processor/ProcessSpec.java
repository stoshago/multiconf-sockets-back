package ua.nix.multiconfback.amq.processor;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ProcessSpec {

    private String processorName;
    private Map<String, Object> specData;

}
