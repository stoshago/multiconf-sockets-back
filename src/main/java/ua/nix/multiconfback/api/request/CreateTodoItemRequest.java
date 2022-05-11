package ua.nix.multiconfback.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTodoItemRequest {

    private String title;
    private String details;

}
