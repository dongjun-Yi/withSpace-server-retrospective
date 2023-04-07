package hansung.cse.withSpace.domain.chat;

import hansung.cse.withSpace.domain.space.Space;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "room") //채팅내역
    private List<Message> messageList = new ArrayList<>();

    private String roomName; //채팅방 이름

    public Room(String roomName, Space space) {
        this.roomName = roomName;
        this.space = space;
    }

}
