package ua.nix.multiconfback.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RewriteTodoList {

    private String title;
    private String icon;
    private List<RewriteTodoItem> items;

}
