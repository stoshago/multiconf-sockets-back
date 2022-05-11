package ua.nix.multiconfback.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTodoListRequest {

    private String title;
    private String icon;
    @JsonProperty("public")
    private boolean isPublic;

}
