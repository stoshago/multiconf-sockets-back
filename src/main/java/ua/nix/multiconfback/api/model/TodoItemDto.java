package ua.nix.multiconfback.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TodoItemDto {

    private String id;
    private String title;
    private String details;
    private String listId;
    private boolean completed;

}
