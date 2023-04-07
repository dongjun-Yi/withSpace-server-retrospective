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
    public Long makeRoom(Space space, String roomName) { //채팅방 생성
        Room room = new Room(roomName, space);
        roomRepository.save(room);
        return room.getId();
    }
}
