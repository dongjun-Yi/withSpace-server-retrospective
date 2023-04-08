package hansung.cse.withSpace.responsedto.chat;

import hansung.cse.withSpace.domain.chat.Message;
import hansung.cse.withSpace.domain.chat.Room;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GetRoomResponseDto {
    private String roomName;
    private List<MessageResponseDto> messageList;
    public GetRoomResponseDto(Room room) {
        roomName = room.getRoomName();
        messageList = room.getMessageList().stream()
                .map(MessageResponseDto::new)
                .collect(Collectors.toList());
    }
}
