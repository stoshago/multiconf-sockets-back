package ua.nix.multiconfback.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTodoItemRequest {
    private String id;
    private String title;
    private String details;
}
