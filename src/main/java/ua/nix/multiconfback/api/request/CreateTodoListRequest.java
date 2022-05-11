package ua.nix.multiconfback.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateTodoListRequest {

    private String title;
    private String icon;
    @JsonProperty("public")
    private boolean isPublic;

}
