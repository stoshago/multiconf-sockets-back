package ua.nix.multiconfback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TodoItem extends AbstractIdModel {

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "details")
    private String details;
    @Column(name = "is_completed")
    private boolean completed;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "todo_list_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ToString.Exclude
    private TodoList list;

    @CreatedDate
    private long createdDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoItem)) return false;
        if (!super.equals(o)) return false;
        TodoItem item = (TodoItem) o;
        return completed == item.completed && createdDate == item.createdDate && title.equals(item.title) && details.equals(item.details) && list.equals(item.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, details, completed, list, createdDate);
    }
}
