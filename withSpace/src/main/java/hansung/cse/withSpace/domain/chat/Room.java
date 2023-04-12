package hansung.cse.withSpace.domain.chat;

import hansung.cse.withSpace.domain.space.Space;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {   //채팅방
    @Id
    @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE) //채팅내역
    private List<Message> messageList = new ArrayList<>();

    private String roomName; //채팅방 이름

    //친구와의 채팅방
    //private Long friendSpaceId=null;
    private Long friendRoomId=null;
    private Long memberId=null;
    private Long friendId=null;

    public Room(String roomName, Space space) { //팀에서 채팅방 생성시
        this.roomName = roomName;
        this.space = space;
    }
    public Room(Space space, String roomName, Long memberId, Long friendId) { //개인채팅방 생성시
        this.roomName = roomName;
        this.space = space;
        this.memberId = memberId;
        this.friendId = friendId;
    }

    public void setFriendRoomId(Long friendRoomId) {
        this.friendRoomId=friendRoomId;
    }


//    public void roomSpaceChange(Space space) {
//
//    }

}
