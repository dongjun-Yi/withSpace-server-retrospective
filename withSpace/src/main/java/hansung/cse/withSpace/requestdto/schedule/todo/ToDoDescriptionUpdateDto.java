package hansung.cse.withSpace.requestdto.schedule.todo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoDescriptionUpdateDto {
    private String description;
}
