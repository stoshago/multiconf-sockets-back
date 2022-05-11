package ua.nix.multiconfback.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nix.multiconfback.api.request.GetTokenRequest;
import ua.nix.multiconfback.api.request.SignupRequest;
import ua.nix.multiconfback.api.response.MessageResponse;
import ua.nix.multiconfback.api.response.TokenResponse;
import ua.nix.multiconfback.model.User;
import ua.nix.multiconfback.security.JwtService;
import ua.nix.multiconfback.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, PasswordEncoder encoder, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/sign-in")
    public TokenResponse signIn(@RequestBody GetTokenRequest getTokenRequest) {
        Authentication authentication = authorize(getTokenRequest.getUsername(), getTokenRequest.getPassword());
        User user = (User) authentication.getPrincipal();
        return TokenResponse.builder()
                .token(jwtService.generateToken(authentication))
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .build();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignupRequest request) {
        if (userService.existsByLogin(request.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        User user = convertFromDto(request);
        userService.save(user);

        Authentication authUser = authorize(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(
                TokenResponse.builder()
                        .token(jwtService.generateToken(authUser))
                        .username(user.getUsername())
                        .displayName(user.getDisplayName())
                        .build()
        );
    }

    private User convertFromDto(SignupRequest request) {
        User user = new User();
        user.setLogin(request.getUsername());
        user.setDisplayName(request.getDisplayName());
        user.setPassword(encoder.encode(request.getPassword()));
        return user;
    }

    private Authentication authorize(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

}
