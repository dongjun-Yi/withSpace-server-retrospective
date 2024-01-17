package hansung.cse.withSpace.requestdto.friendship;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestDto {
    @NotNull(message = "친구의 번호는 필수입니다.")
    private Long friendId;
}
