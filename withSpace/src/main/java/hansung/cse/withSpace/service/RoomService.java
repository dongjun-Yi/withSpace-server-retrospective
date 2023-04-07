package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.chat.Room;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.exception.chat.RoomNotFoundException;
import hansung.cse.withSpace.exception.member.MemberNotFoundException;
import hansung.cse.withSpace.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

//    @Transactional
//    public Long makeRoom() {
//
//    }

    public Room findOne(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("채팅방을 찾을 수 없습니다."));
    }

    @Transactional
    public Long makePersonalChattingRoom(Space space, String roomName, Long memberId, Long friendId) { // 개인 채팅방 생성
        Room room = new Room(space, roomName, memberId, friendId);
        roomRepository.save(room);
        return room.getId();
    }
    @Transactional
    public Long makeTeamChattingRoom(Space space, String roomName) { // 팀 채팅방 생성
        Room room = new Room(roomName, space);
        roomRepository.save(room);
        return room.getId();
    }

    @Transactional
    public void makeRoomFriendRelation(Long myRoomId, Long friendRoomId) { //서로 친구 채팅방 연결
        Room myRoom = roomRepository.findById(myRoomId)
                .orElseThrow(() -> new RoomNotFoundException("내 채팅방을 찾을 수 없습니다."));
        Room friendRoom = roomRepository.findById(friendRoomId)
                .orElseThrow(() -> new RoomNotFoundException("친구의 채팅방을 찾을 수 없습니다."));
        myRoom.setFriendRoomId(friendRoomId);
        friendRoom.setFriendRoomId(myRoomId);
    }
}
