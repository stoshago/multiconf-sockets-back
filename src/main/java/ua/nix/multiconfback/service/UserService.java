package ua.nix.multiconfback.service;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import ua.nix.multiconfback.model.User;

@Service
public interface UserService extends CrudRepository<User, String> {

    User findByLogin(String login);
    boolean existsByLogin(String login);

}
