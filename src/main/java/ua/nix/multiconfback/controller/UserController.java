package ua.nix.multiconfback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nix.multiconfback.model.User;
import ua.nix.multiconfback.repository.UserRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/all")
    public Iterable<User> userList() {
        return userRepository.findAll();
    }

}
