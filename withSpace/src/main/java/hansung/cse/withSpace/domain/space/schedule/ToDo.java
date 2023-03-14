package hansung.cse.withSpace.domain.space.schedule;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToDo {

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

    public ToDo(Category category, String description, Boolean completed, LocalDateTime date) {
        this.category = category;
        this.description = description;
        this.completed = completed;
        this.date = date;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void updateCompleted(Boolean completed) {
        this.completed = completed;
    }
}
