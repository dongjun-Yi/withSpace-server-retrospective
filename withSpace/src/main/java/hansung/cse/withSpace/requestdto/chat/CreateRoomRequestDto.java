package hansung.cse.withSpace.requestdto.chat;

import lombok.Data;

import java.util.Optional;

@Data
public class CreateRoomRequestDto {
    private Optional<Long> memberId = Optional.empty();
    private Optional<Long> friendId = Optional.empty();
    private  Optional<Long> teamId = Optional.empty();
    private String roomName;
}
