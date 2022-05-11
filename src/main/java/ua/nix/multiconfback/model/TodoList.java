package ua.nix.multiconfback.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TodoList extends AbstractIdModel {

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "icon")
    private String icon;
    @Column(name = "is_public", nullable = false)
    private boolean isPublic;
    @ManyToOne
    @JoinColumn(name = "create_by")
    @CreatedBy
    private User createdBy;
    @CreatedDate
    private long createdDate;
    @OneToMany(
            mappedBy = "list",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("createdDate")
    @ToString.Exclude
    private List<TodoItem> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoList)) return false;
        if (!super.equals(o)) return false;
        TodoList todoList = (TodoList) o;
        return isPublic == todoList.isPublic && title.equals(todoList.title) && Objects.equals(icon, todoList.icon) && createdBy.equals(todoList.createdBy) && items.equals(todoList.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, icon, isPublic, createdBy, items);
    }

}
