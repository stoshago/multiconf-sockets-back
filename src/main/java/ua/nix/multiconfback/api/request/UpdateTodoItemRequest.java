package ua.nix.multiconfback.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateTodoItemRequest {
    private String id;
    private String title;
    private String details;
}
