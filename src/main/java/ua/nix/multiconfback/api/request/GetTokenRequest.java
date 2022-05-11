package ua.nix.multiconfback.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetTokenRequest {

    private String username;
    private String password;

}
