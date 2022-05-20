package ua.nix.multiconfback.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nix.multiconfback.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);

}
