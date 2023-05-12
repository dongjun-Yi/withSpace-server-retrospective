package hansung.cse.withSpace.responsedto.schedule;

import hansung.cse.withSpace.domain.space.schedule.PublicSetting;
import hansung.cse.withSpace.domain.space.schedule.Schedule;
import hansung.cse.withSpace.responsedto.schedule.category.CategoriesDto;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ScheduleFriendDto {
    private Long scheduleId;
    private Long spaceId;
    private int categoryCount;
    private List<CategoriesDto> categories;
    public ScheduleFriendDto(Schedule schedule) {
        scheduleId = schedule.getId();
        spaceId = schedule.getSpace().getId();
        categories = schedule.getCategories().stream()
                .filter(category -> category.getPublicSetting().equals(PublicSetting.PUBLIC)
                        || category.getPublicSetting().equals(PublicSetting.FRIEND))
                .map(CategoriesDto::new)
                .collect(Collectors.toList());

        categoryCount = categories.size();

    }
}
