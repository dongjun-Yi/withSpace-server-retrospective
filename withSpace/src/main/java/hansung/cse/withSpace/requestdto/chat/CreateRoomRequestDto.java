package hansung.cse.withSpace.requestdto.chat;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Optional;

@Data
public class CreateRoomRequestDto {
    private String roomName;
}
