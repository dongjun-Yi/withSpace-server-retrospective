package hansung.cse.withSpace.responsedto.space;

import hansung.cse.withSpace.domain.space.schedule.Schedule;
import hansung.cse.withSpace.responsedto.schedule.category.CategoryDto;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SpaceScheduleDto {
    private Long id;
    private int categoryCount;

    private List<CategoryDto> categories;

    public SpaceScheduleDto(Schedule schedule) {
        id = schedule.getId();
        categories = schedule.getCategories().stream()
                .map(category -> new CategoryDto(category))
                .collect(Collectors.toList());
        categoryCount = categories.size();
    }
}
