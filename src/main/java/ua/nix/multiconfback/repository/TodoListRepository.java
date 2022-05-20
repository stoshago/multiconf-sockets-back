package ua.nix.multiconfback.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nix.multiconfback.model.TodoList;
import ua.nix.multiconfback.model.User;

import java.util.List;

@Repository
public interface TodoListRepository extends CrudRepository<TodoList, String> {

    List<TodoList> findAllByIsPublicTrueOrderByCreatedDate();

    List<TodoList> findAllByCreatedByAndIsPublicFalseOrderByCreatedDate(User createdBy);

    void deleteAllByCreatedByAndIsPublicFalse(User user);

}
