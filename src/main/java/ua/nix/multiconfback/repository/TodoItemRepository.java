package ua.nix.multiconfback.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nix.multiconfback.model.TodoItem;

@Repository
public interface TodoItemRepository extends CrudRepository<TodoItem, String> {
}
