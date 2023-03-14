package hansung.cse.withSpace.requestdto.schedule.todo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoRequestDto {
    private Long categoryId;
    private String description;
    private Boolean completed;
}
