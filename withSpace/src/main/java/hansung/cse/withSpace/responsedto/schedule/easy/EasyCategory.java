package hansung.cse.withSpace.responsedto.schedule.easy;

import hansung.cse.withSpace.domain.space.schedule.Category;
import hansung.cse.withSpace.domain.space.schedule.EasyToDo;
import hansung.cse.withSpace.domain.space.schedule.ToDo;
import hansung.cse.withSpace.repository.ToDoRepository;
import hansung.cse.withSpace.responsedto.schedule.todo.ToDoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EasyCategory {
    private Long id;
    private String title;
    private EasyToDoDto todo;
    private int color;

    public EasyCategory(Category category, EasyToDo todo) {
        this.id = category.getId();
        this.title = category.getTitle();
        this.color = category.getColor();
        if(todo != null){
            this.todo = new EasyToDoDto(todo);
        }
    }
}
