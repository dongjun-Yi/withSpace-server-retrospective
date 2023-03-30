package hansung.cse.withSpace.responsedto.space;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.Team;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.responsedto.space.page.PageDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TeamSpaceDto {

    private Long spaceId;
    private List<PageDto> pageList = new ArrayList<>();

    public TeamSpaceDto(Team team) {
        this.spaceId = team.getTeamSpace().getId();
        this.pageList = team.getTeamSpace().getPageList().stream()
                .filter(page -> page.getParentPage() == null)
                .map(page -> new PageDto(page))
                .collect(Collectors.toList());
    }

}
