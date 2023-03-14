package hansung.cse.withSpace.responsedto.schedule.category;

import hansung.cse.withSpace.domain.space.schedule.Category;
import hansung.cse.withSpace.responsedto.schedule.todo.ToDoDto;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CategoryDto {
    private Long id;
    private String title;

    private int toDoCount;

    private List<ToDoDto> toDoList;

    public CategoryDto(Category category) {
        id = category.getId();
        title = category.getTitle();
        toDoList = category.getTodoList().stream()
                .map(toDo -> new ToDoDto(toDo))
                .collect(Collectors.toList());
        toDoCount = toDoList.size();
    }
}
