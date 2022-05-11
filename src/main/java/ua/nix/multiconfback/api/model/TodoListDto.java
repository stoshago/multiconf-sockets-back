package ua.nix.multiconfback.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoListDto {

    private String id;
    private String title;
    private String icon;
    private UserDto createdBy;
    private long createdDate;
    private boolean isPublic;

}
