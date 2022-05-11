package ua.nix.multiconfback.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequest {

    private String username;
    private String password;
    private String displayName;

}
