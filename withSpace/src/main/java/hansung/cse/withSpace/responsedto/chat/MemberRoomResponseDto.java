package hansung.cse.withSpace.responsedto.chat;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.chat.Room;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.domain.space.TeamSpace;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberRoomResponseDto { //회원이 들어가있는 채팅방을 보여주는 DTO

    private Long chatRoomId;
    private String roomName;
    private String type;
    private Long id; //팀 또는 상대방 id
    private List<String> memberName = new ArrayList<>();

    public MemberRoomResponseDto(Room room, Long memberId) {
        this.chatRoomId = room.getId();
        this.roomName = room.getRoomName();
        this.memberName = room.getMemberNames();

        if (room.getSpace() == null) {
            type = "개인 채팅방";
            this.id = room.getFriendId();

        } else {
            type = "팀 채팅방";
            this.id = room.getTeamId();
        }
    }
}