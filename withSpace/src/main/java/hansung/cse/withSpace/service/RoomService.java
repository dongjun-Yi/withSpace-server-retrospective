package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.chat.Room;
import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.domain.friend.FriendStatus;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.exception.chat.RoomNotFoundException;
import hansung.cse.withSpace.exception.friend.NotFriendException;
import hansung.cse.withSpace.exception.member.MemberNotFoundException;
import hansung.cse.withSpace.repository.FriendShipRepository;
import hansung.cse.withSpace.repository.MemberRepository;
import hansung.cse.withSpace.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final FriendShipRepository friendShipRepository;
    private final FriendShipService friendShipService;

//    @Transactional
//    public Long makeRoom() {
//
//    }

    public Room findOne(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("채팅방을 찾을 수 없습니다."));
    }

    public void isFriend(Long memberId, Long friendId) {
        //서로 친구관계인지 확인
        FriendShip friendShip1 = friendShipRepository.findByMemberIdAndFriendIdAndStatus(memberId, friendId, FriendStatus.ACCEPTED);
        FriendShip friendShip2 = friendShipRepository.findByMemberIdAndFriendIdAndStatus(friendId, memberId, FriendStatus.ACCEPTED);

        boolean isFriend = (friendShip1 != null && friendShip2 != null);

        if(!isFriend){
            throw new NotFriendException("두 회원이 서로 친구가 아닙니다.");
        }

    }

    @Transactional
    public Long makePersonalChattingRoom(Space space, String roomName, Long memberId, Long friendId) { // 개인 채팅방 생성

        //서로 친구관계인지 확인
//        FriendShip friendShip = friendShipRepository.findByMemberIdAndFriendIdAndStatus(memberId, friendId, FriendStatus.ACCEPTED);
//        FriendShip friendShip2 = friendShipRepository.findByMemberIdAndFriendIdAndStatus(friendId, memberId, FriendStatus.ACCEPTED);
//        boolean isFriend = (friendShip != null && friendShip2 != null);
//
//        if(!isFriend){
//            throw new NotFriendException("두 회원이 서로 친구가 아닙니다.");
//        }

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
