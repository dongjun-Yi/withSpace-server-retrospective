package hansung.cse.withSpace.responsedto.schedule.todo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoEasyResponse {
    private String uuid;
    private Integer status;
    private String message;
}
