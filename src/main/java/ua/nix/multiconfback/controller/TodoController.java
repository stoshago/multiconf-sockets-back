package ua.nix.multiconfback.controller;

import lombok.RequiredArgsConstructor;
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
import ua.nix.multiconfback.api.model.RewriteTodoList;
import ua.nix.multiconfback.api.request.CreateTodoItemRequest;
import ua.nix.multiconfback.api.request.CreateTodoListRequest;
import ua.nix.multiconfback.api.response.AvailableListsResponse;
import ua.nix.multiconfback.exceptions.DataNotFoundException;
import ua.nix.multiconfback.exceptions.PermissionDeniedException;
import ua.nix.multiconfback.model.TodoItem;
import ua.nix.multiconfback.model.TodoList;
import ua.nix.multiconfback.model.User;
import ua.nix.multiconfback.service.AuthService;
import ua.nix.multiconfback.service.BrokerNotificationService;
import ua.nix.multiconfback.service.TodoItemService;
import ua.nix.multiconfback.service.TodoListService;
import ua.nix.multiconfback.util.DtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo/")
public class TodoController {

    private final TodoListService todoListService;
    private final TodoItemService todoItemService;
    private final AuthService authService;
    private final BrokerNotificationService notificationService;
    private final DtoMapper dtoMapper;

    @GetMapping("/list/all")
    public AvailableListsResponse getAllLists() {
        List<TodoList> privateLists = todoListService.findAllByCreatedByAndIsPublicFalseOrderByCreatedDate(authService.getCurrentUser());
        List<TodoList> publicLists = todoListService.findAllByIsPublicTrueOrderByCreatedDate();
        return new AvailableListsResponse(
                dtoMapper.convertTodoLists(privateLists),
                dtoMapper.convertTodoLists(publicLists)
        );
    }

    @PostMapping("/list")
    public void createList(@RequestBody CreateTodoListRequest request) {
        TodoList list = new TodoList();
        list.setTitle(request.getTitle());
        list.setIcon(request.getIcon());
        list.setPublic(request.isPublic());
        list = todoListService.save(list);

        notificationService.notifyListAdded(dtoMapper.convertTodoList(list), list.isPublic());
    }

    @GetMapping("/list/{listId}")
    @Transactional
    public DetailedListDto getList(@PathVariable String listId) {
        TodoList list = findListSecured(listId);
        return dtoMapper.convertDetailedTodoList(list);
    }

    @DeleteMapping("/list/{listId}")
    @Transactional
    public void deleteList(@PathVariable String listId) {
        TodoList list = findListSecured(listId);
        todoListService.delete(list);

        notificationService.notifyListDeleted(listId, list.isPublic());
    }

    @PostMapping("/list/{listId}")
    public void createListItem(
            @PathVariable String listId,
            @RequestBody CreateTodoItemRequest request
    ) {
        TodoList list = findListSecured(listId);

        TodoItem item = new TodoItem();
        item.setTitle(request.getTitle());
        item.setDetails(request.getDetails());
        item.setList(list);
        item = todoItemService.save(item);

        notificationService.notifyListItemAdded(dtoMapper.convertTodoItem(item), list.isPublic());
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

        notificationService.notifyListItemCompleted(item.getId(), item.isCompleted(), item.getList().isPublic());
    }

    @DeleteMapping("/item/{itemId}")
    @Transactional
    public void deleteItem(@PathVariable String itemId) {
        TodoItem item = findItemSecured(itemId);
        TodoList list = item.getList();

        list.getItems().remove(item);
        todoItemService.delete(item);

        notificationService.notifyListItemDeleted(list.getId(), item.getId(), list.isPublic());
    }

    @PostMapping("/list/rewriteMyLists")
    public List<DetailedListDto> rewriteAllPrivateLists(@RequestBody List<RewriteTodoList> listsRequest) {
        // clear all lists
        User currentUser = authService.getCurrentUser();
        todoListService.deleteAllByCreatedByAndIsPublicFalse(currentUser);

        // create new lists
        List<TodoList> newLists = listsRequest.stream().map(listReq -> {
            TodoList newList = new TodoList();
            newList.setTitle(listReq.getTitle());
            newList.setIcon(listReq.getIcon());
            newList.setPublic(false);
            newList.setItems(listReq.getItems().stream().map(itemReq -> {
                TodoItem newItem = new TodoItem();
                newItem.setTitle(itemReq.getTitle());
                newItem.setDetails(itemReq.getDetails());
                newItem.setCompleted(itemReq.isCompleted());
                newItem.setList(newList);
                return newItem;
            }).collect(Collectors.toList()));
            return newList;
        }).collect(Collectors.toList());

        // save new lists
        todoListService.saveAll(newLists);
        List<DetailedListDto> response = dtoMapper.convertDetailedTodoLists(newLists);

        notificationService.notifyPrivateListsUpdated();

        return response;
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
