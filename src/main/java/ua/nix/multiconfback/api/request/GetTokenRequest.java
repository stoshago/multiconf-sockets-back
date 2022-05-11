package ua.nix.multiconfback.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetTokenRequest {

    private String username;
    private String password;

}
