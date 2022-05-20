package ua.nix.multiconfback.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.nix.multiconfback.model.TodoItem;

@Repository
@Transactional
public interface TodoItemRepository extends CrudRepository<TodoItem, String> {
}
