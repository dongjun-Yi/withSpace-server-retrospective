package hansung.cse.withSpace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberIdDto { // 웹소켓 heartbeat에서 memberId들 찾을때 씀
    private Long memberId;


}