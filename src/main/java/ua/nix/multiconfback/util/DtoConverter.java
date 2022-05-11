package ua.nix.multiconfback.util;

import ua.nix.multiconfback.api.model.DetailedListDto;
import ua.nix.multiconfback.api.model.TodoItemDto;
import ua.nix.multiconfback.api.model.TodoListDto;
import ua.nix.multiconfback.api.model.UserDto;
import ua.nix.multiconfback.model.TodoItem;
import ua.nix.multiconfback.model.TodoList;
import ua.nix.multiconfback.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class DtoConverter {

    private DtoConverter() {
    }

    public static TodoListDto convertTodoList(TodoList list) {
        return new TodoListDto(
                list.getId(),
                list.getTitle(),
                list.getIcon(),
                convertUser(list.getCreatedBy()),
                list.getCreatedDate(),
                list.isPublic()
        );
    }

    public static List<TodoListDto> convertTodoLists(List<TodoList> list) {
        return list.stream().map(DtoConverter::convertTodoList).collect(Collectors.toList());
    }

    public static DetailedListDto convertDetailedTodoList(TodoList list) {
        return DetailedListDto.builder()
                .id(list.getId())
                .title(list.getTitle())
                .icon(list.getIcon())
                .isPublic(list.isPublic())
                .createdBy(convertUser(list.getCreatedBy()))
                .createdDate(list.getCreatedDate())
                .items(list.getItems()
                        .stream()
                        .map(DtoConverter::convertTodoItem)
                        .collect(Collectors.toList()))
                .build();
    }

    public static List<DetailedListDto> convertDetailedTodoLists(List<TodoList> list) {
        return list.stream().map(DtoConverter::convertDetailedTodoList).collect(Collectors.toList());
    }

    public static TodoItemDto convertTodoItem(TodoItem item) {
        return TodoItemDto.builder()
                .id(item.getId())
                .listId(item.getList().getId())
                .title(item.getTitle())
                .details(item.getDetails())
                .completed(item.isCompleted())
                .build();
    }

    public static UserDto convertUser(User user) {
        return new UserDto(
                user.getId(),
                user.getDisplayName()
        );
    }

}
