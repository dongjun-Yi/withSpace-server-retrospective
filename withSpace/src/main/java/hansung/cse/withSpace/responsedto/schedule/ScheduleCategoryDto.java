package hansung.cse.withSpace.responsedto.schedule;

import hansung.cse.withSpace.domain.space.schedule.Schedule;
import hansung.cse.withSpace.responsedto.schedule.category.CategoriesDto;
import hansung.cse.withSpace.responsedto.schedule.category.CategoryDto;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ScheduleCategoryDto {
    private Long scheduleId;
    private Long spaceId;
    private int categoryCount;
    private List<CategoriesDto> categories;
    public ScheduleCategoryDto(Schedule schedule) {
        scheduleId = schedule.getId();
        spaceId = schedule.getSpace().getId();
        categories = schedule.getCategories().stream()
                .map(CategoriesDto::new)
                .collect(Collectors.toList());
        categoryCount = categories.size();

    }
}
