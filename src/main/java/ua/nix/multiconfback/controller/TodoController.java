package ua.nix.multiconfback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nix.multiconfback.api.model.DetailedListDto;
import ua.nix.multiconfback.api.model.TodoItemDto;
import ua.nix.multiconfback.api.model.TodoListDto;
import ua.nix.multiconfback.api.request.CreateTodoItemRequest;
import ua.nix.multiconfback.api.request.CreateTodoListRequest;
import ua.nix.multiconfback.api.response.AvailableListsResponse;
import ua.nix.multiconfback.exceptions.DataNotFoundException;
import ua.nix.multiconfback.exceptions.PermissionDeniedException;
import ua.nix.multiconfback.model.TodoItem;
import ua.nix.multiconfback.model.TodoList;
import ua.nix.multiconfback.service.AuthService;
import ua.nix.multiconfback.service.TodoItemService;
import ua.nix.multiconfback.service.TodoListService;
import ua.nix.multiconfback.util.DtoConverter;

import java.util.List;

@RestController
@RequestMapping("/api/todo/")
public class TodoController {

    private final TodoListService todoListService;
    private final TodoItemService todoItemService;
    private final AuthService authService;

    @Autowired
    public TodoController(
            TodoListService todoListService,
            TodoItemService todoItemService,
            AuthService authService) {
        this.todoListService = todoListService;
        this.todoItemService = todoItemService;
        this.authService = authService;
    }

    @GetMapping("/list/all")
    public AvailableListsResponse getAllLists() {
        List<TodoList> privateLists = todoListService.findAllByCreatedByAndPublicFalseOrderByCreatedDate(authService.getCurrentUser());
        List<TodoList> publicLists = todoListService.findAllByPublicTrueOrderByCreatedDate();
        return new AvailableListsResponse(
                DtoConverter.convertTodoLists(privateLists),
                DtoConverter.convertTodoLists(publicLists)
        );
    }

    @PostMapping("/list")
    public TodoListDto createList(@RequestBody CreateTodoListRequest request) {
        TodoList list = new TodoList();
        list.setTitle(request.getTitle());
        list.setIcon(request.getIcon());
        list.setPublic(request.isPublic());
        list = todoListService.save(list);
        return DtoConverter.convertTodoList(list);
    }

    @GetMapping("/list/{listId}")
    @Transactional
    public DetailedListDto getList(@PathVariable String listId) {
        TodoList list = findListSecured(listId);
        return DtoConverter.convertDetailedTodoList(list);
    }

    @DeleteMapping("/list/{listId}")
    @Transactional
    public void deleteList(@PathVariable String listId) {
        TodoList list = findListSecured(listId);
        todoListService.delete(list);
    }

    @PostMapping("/list/{listId}")
    public TodoItemDto createListItem(
            @PathVariable String listId,
            @RequestBody CreateTodoItemRequest request
    ) {
        TodoList list = findListSecured(listId);

        TodoItem item = new TodoItem();
        item.setTitle(request.getTitle());
        item.setDetails(request.getDetails());
        item.setList(list);
        item = todoItemService.save(item);

        return DtoConverter.convertTodoItem(item);
    }

    @PutMapping("/item/{itemId}/complete/{isCompleted}")
    @Transactional
    public void setListItemCompleted(
            @PathVariable String itemId,
            @PathVariable boolean isCompleted
    ) {
        TodoItem item = findItemSecured(itemId);
        item.setCompleted(isCompleted);
        todoItemService.save(item);
    }

    @DeleteMapping("/item/{itemId}")
    @Transactional
    public void deleteItem(@PathVariable String itemId) {
        TodoItem item = findItemSecured(itemId);
        todoItemService.delete(item);
    }

    private TodoItem findItemSecured(String itemId) {
        String currentUserId = authService.getCurrentUser().getId();
        TodoItem item = todoItemService.findById(itemId)
                .orElseThrow(() -> new DataNotFoundException(
                        String.format("Item [id:%s] was not found", itemId)
                ));
        TodoList assignedList = item.getList();
        if (!assignedList.isPublic() && !currentUserId.equals(assignedList.getCreatedBy().getId())) {
            throw new PermissionDeniedException();
        }
        return item;
    }

    private TodoList findListSecured(String listId) {
        String currentUserId = authService.getCurrentUser().getId();
        TodoList list = findList(listId);
        if (!list.isPublic() && !currentUserId.equals(list.getCreatedBy().getId())) {
            throw new PermissionDeniedException();
        }
        return list;
    }

    private TodoList findList(String listId) {
        return todoListService.findById(listId)
                .orElseThrow(() -> new DataNotFoundException(
                        String.format("List [id:%s] was not found", listId)
                ));
    }

}
