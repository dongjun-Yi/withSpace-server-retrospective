package hansung.cse.withSpace.responsedto.memberTeam;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.MemberTeam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberTeamResponseDto {


    private Long userId;
    private String memberName;
    private boolean status;

    public MemberTeamResponseDto(MemberTeam memberTeam) {
        Member member = memberTeam.getMember();
        this.userId = member.getId();
        this.memberName = member.getMemberName();

        //this.status = member.getStatus();
        this.status = member.getStatus() != null && member.getStatus().booleanValue();
    }
}
