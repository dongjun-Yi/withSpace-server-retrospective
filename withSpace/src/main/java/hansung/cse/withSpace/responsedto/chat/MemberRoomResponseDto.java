package hansung.cse.withSpace.responsedto.chat;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.chat.Room;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberRoomResponseDto { //회원이 들어가있는 채팅방을 보여주는 DTO
    private Long chatRoomId;
    private String roomName;
    private String type;
    private List<String> memberName = new ArrayList<>();

    public MemberRoomResponseDto(Room room) {
        this.chatRoomId = room.getId();
        this.roomName = room.getRoomName();
        this.memberName = room.getMemberNames();
        if (room.getSpace() == null) {
            type = "개인 채팅방";
        } else {
            type = "팀 채팅방";
        }
    }
}