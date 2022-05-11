package ua.nix.multiconfback.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private String id;
    private String displayName;

}
