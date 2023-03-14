package hansung.cse.withSpace.domain.space.schedule;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<ToDo> todoList = new ArrayList<>();

    private String title; //카테고리 제목

    public Category(Schedule schedule, String title) {
        this.schedule = schedule;
        this.title = title;
    }

    public void changeTitle(String title) {
        this.title = title;
    }
}
