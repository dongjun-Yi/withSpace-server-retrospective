package hansung.cse.withSpace.requestdto.chat;

import lombok.Data;

import java.util.Optional;

@Data
public class CreateRoomRequestDto {
    private Optional<Long> memberId;
    private Optional<Long> friendId;
    private  Optional<Long> teamId;
    private String roomName;
}
