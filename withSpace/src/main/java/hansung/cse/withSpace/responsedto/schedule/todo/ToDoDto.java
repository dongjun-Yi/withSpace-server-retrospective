package hansung.cse.withSpace.responsedto.schedule.todo;

import hansung.cse.withSpace.domain.space.schedule.ToDo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ToDoDto {
    private Long id;
    private String description;
    private Boolean completed;
    private LocalDateTime date;

    public ToDoDto(ToDo toDo) {
        id = toDo.getId();
        description = toDo.getDescription();
        completed = toDo.getCompleted();
        date = toDo.getDate();
    }
}
