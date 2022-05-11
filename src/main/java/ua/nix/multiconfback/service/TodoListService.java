package ua.nix.multiconfback.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.nix.multiconfback.model.TodoList;
import ua.nix.multiconfback.model.User;

import java.util.List;

@Service
@Transactional
public interface TodoListService extends CrudRepository<TodoList, String> {

    List<TodoList> findAllByIsPublicTrueOrderByCreatedDate();
    List<TodoList> findAllByCreatedByAndIsPublicFalseOrderByCreatedDate(User createdBy);

    void deleteAllByCreatedByAndIsPublicFalse(User user);

}
