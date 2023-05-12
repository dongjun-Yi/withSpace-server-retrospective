package hansung.cse.withSpace.responsedto.schedule.easy;

import hansung.cse.withSpace.domain.space.schedule.EasyToDo;
import hansung.cse.withSpace.domain.space.schedule.ToDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EasyToDoDto {

    private String description;
    private LocalDateTime start;
    private LocalDateTime end;

    public EasyToDoDto(EasyToDo todo) {
        this.description = todo.getDescription();
        this.start = todo.getStart();
        this.end = todo.getEnd();
    }
}
