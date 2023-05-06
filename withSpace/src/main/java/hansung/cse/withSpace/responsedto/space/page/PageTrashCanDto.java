package hansung.cse.withSpace.responsedto.space.page;

import hansung.cse.withSpace.domain.space.Page;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.responsedto.schedule.ScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageTrashCanDto {
    private Long pageId;
    private String pageTitle;
    private Long parentId;
    private List<PageDto> childPageList = new ArrayList<>();

    public PageTrashCanDto(Page page) {
        this.pageId = page.getId();
        this.pageTitle = page.getTitle();
        this.parentId = page.getParentPage() != null ? page.getParentPage().getId() : null;
        this.childPageList = page.getChildPages().stream()
                .filter(childPage -> !childPage.equals(page.getParentPage())) // Filter out parent page
                .map(PageDto::new)
                .collect(Collectors.toList());
    }

}
