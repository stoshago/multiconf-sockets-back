package ua.nix.multiconfback.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {

    private String token;
    private String username;
    private String displayName;

}
