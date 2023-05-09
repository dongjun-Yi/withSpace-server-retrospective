package hansung.cse.withSpace.responsedto.member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberSearchByNameDto {
    private Long memberId;
    private String memberName;

}
