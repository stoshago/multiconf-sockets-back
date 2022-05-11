package ua.nix.multiconfback.api.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DetailedListDto extends TodoListDto {

    private List<TodoItemDto> items;

    @Builder
    public DetailedListDto(
            String id,
            String title,
            String icon,
            UserDto createdBy,
            long createdDate,
            boolean isPublic,
            List<TodoItemDto> items
    ) {
        super(id, title, icon, createdBy, createdDate, isPublic);
        this.setItems(items);
    }

}
