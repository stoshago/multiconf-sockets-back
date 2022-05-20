package ua.nix.multiconfback.util;

import lombok.RequiredArgsConstructor;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.nix.multiconfback.api.model.DetailedListDto;
import ua.nix.multiconfback.api.model.TodoItemDto;
import ua.nix.multiconfback.api.model.TodoListDto;
import ua.nix.multiconfback.api.model.UserDto;
import ua.nix.multiconfback.api.request.CreateTodoItemRequest;
import ua.nix.multiconfback.api.request.SignupRequest;
import ua.nix.multiconfback.model.TodoItem;
import ua.nix.multiconfback.model.TodoList;
import ua.nix.multiconfback.model.User;

import java.util.List;

@Mapper
public abstract class DtoMapper {

    @Autowired
    protected PasswordEncoder encoder;

    public abstract UserDto convertUser(User user);
    @Named("todoListMapping")
    public abstract TodoListDto convertTodoList(TodoList list);
    @Named("detailedTodoListMapping")
    public abstract DetailedListDto convertDetailedTodoList(TodoList list);
    @IterableMapping(qualifiedByName="todoListMapping")
    public abstract List<TodoListDto> convertTodoLists(List<TodoList> list);
    @IterableMapping(qualifiedByName="detailedTodoListMapping")
    public abstract List<DetailedListDto> convertDetailedTodoLists(List<TodoList> list);
    public abstract TodoItemDto convertTodoItem(TodoItem item);

    @Mappings({
            @Mapping(target = "login", source = "request.username"),
            @Mapping(target = "password", expression = "java(encoder.encode(request.getPassword()))")
    })
    public abstract User parseUser(SignupRequest request);
    public abstract TodoItem parseTodoItem(CreateTodoItemRequest request);

}
