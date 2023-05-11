package hansung.cse.withSpace.domain.space.schedule;

import hansung.cse.withSpace.exception.todo.ToDoActiveException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EasyToDo {
    @Id
    @GeneratedValue
    @Column(name = "todo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String description;
    private Boolean completed;
    private LocalDateTime date;
    private boolean active;
    private UUID easyMake; //간편등록시에만 사용됨

    private LocalDateTime start; //간편등록시에만 사용됨

    private LocalDateTime end; //간편등록시에만 사용됨



    public EasyToDo(Category category, String description, Boolean completed,
                LocalDateTime date, boolean active, UUID uuid) {
        this.category = category;
        this.description = description;
        this.completed = completed;
        this.date = date;
        this.active = active;
        this.easyMake = uuid;
    }


    public void changeDescription(String description) {
        this.description = description;
    }

    public void updateCompleted(Boolean completed) {
        this.completed = completed;
    }

    public void changeActive() {
        if (this.active) {
            throw new ToDoActiveException("이미 활성화중인 투두입니다.");
        }
        this.active = true;
    }

    public void changeDate(LocalDateTime start, LocalDateTime end ) {
        this.start = start;
        this.end = end;
    }

}
