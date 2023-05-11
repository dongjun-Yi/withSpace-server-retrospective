package hansung.cse.withSpace.responsedto.schedule.category;

import hansung.cse.withSpace.domain.space.schedule.Category;
import hansung.cse.withSpace.domain.space.schedule.EndStatus;
import hansung.cse.withSpace.domain.space.schedule.PublicSetting;
import hansung.cse.withSpace.responsedto.schedule.todo.ToDoDto;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CategoryDto {
    private Long categoryId;
    private String title;
    private PublicSetting publicSetting;
    private int color;
    private boolean end;
    private EndStatus endStatus;

    private int toDoCount;

    private List<ToDoDto> toDoList;

    public CategoryDto(Category category) {
        categoryId = category.getId();
        title = category.getTitle();
        publicSetting = category.getPublicSetting();
        color = category.getColor();
        end = category.isEnd();
        endStatus = category.getEndStatus();
        toDoList = category.getTodoList().stream()
                .map(toDo -> new ToDoDto(toDo))
                .collect(Collectors.toList());
        toDoCount = toDoList.size();
    }
}
