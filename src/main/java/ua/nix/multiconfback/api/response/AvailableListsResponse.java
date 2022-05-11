package ua.nix.multiconfback.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ua.nix.multiconfback.api.model.TodoListDto;

import java.util.List;

@Getter
@AllArgsConstructor
public class AvailableListsResponse {

    private List<TodoListDto> privateLists;
    private List<TodoListDto> publicLists;

}
