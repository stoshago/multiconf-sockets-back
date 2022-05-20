package ua.nix.multiconfback.util;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ua.nix.multiconfback.api.model.DetailedListDto;
import ua.nix.multiconfback.api.model.TodoItemDto;
import ua.nix.multiconfback.api.model.TodoListDto;
import ua.nix.multiconfback.api.model.UserDto;
import ua.nix.multiconfback.model.TodoItem;
import ua.nix.multiconfback.model.TodoList;
import ua.nix.multiconfback.model.User;

import java.util.List;

@Mapper
public interface DtoMapper {

    UserDto convertUser(User user);
    @Named("todoListMapping")
    TodoListDto convertTodoList(TodoList list);
    @Named("detailedTodoListMapping")
    DetailedListDto convertDetailedTodoList(TodoList list);
    @IterableMapping(qualifiedByName="todoListMapping")
    List<TodoListDto> convertTodoLists(List<TodoList> list);
    @IterableMapping(qualifiedByName="detailedTodoListMapping")
    List<DetailedListDto> convertDetailedTodoLists(List<TodoList> list);
    TodoItemDto convertTodoItem(TodoItem item);

}
