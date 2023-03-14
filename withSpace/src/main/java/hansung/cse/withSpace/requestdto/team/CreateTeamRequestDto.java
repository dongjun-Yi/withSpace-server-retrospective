package hansung.cse.withSpace.requestdto.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamRequestDto {
    private Long memberId;
    private String teamName;
}
