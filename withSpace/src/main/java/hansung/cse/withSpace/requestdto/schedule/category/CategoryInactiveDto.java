package hansung.cse.withSpace.requestdto.schedule.category;

import hansung.cse.withSpace.domain.space.schedule.EndStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryInactiveDto {
    @NotNull(message = "종료 상태가 전달되지 않았습니다.")
    private EndStatus endStatus;
}
