package ua.nix.multiconfback.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.nix.multiconfback.model.TodoItem;
import ua.nix.multiconfback.model.TodoList;

@Service
@Transactional
public interface TodoItemService extends CrudRepository<TodoItem, String> {

    Iterable<TodoItem> findAllByListId(String listId);
    Iterable<TodoItem> findAllByList(TodoList list);

}
