package ua.nix.multiconfback.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.nix.multiconfback.model.User;

@Service
public class AuthService {

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
