package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.domain.friend.FriendStatus;
import hansung.cse.withSpace.exception.friend.FriendRequestException;
import hansung.cse.withSpace.repository.FriendShipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class FriendShipService {

    private final RoomService roomService;
    private final FriendShipRepository friendShipRepository;


    @Transactional
    public Long sendFriendRequest(FriendShip friendShip) throws FriendRequestException {
        if (checkIfFriendRequestPresent(friendShip))
            throw new FriendRequestException("이미 친구신청을 보냈습니다.");
        FriendShip saveFriendRequest = friendShipRepository.save(friendShip);
        checkFriendShipStatus(friendShip);
        return saveFriendRequest.getId();
    }

    private boolean checkIfFriendRequestPresent(FriendShip friendShip) {
        Optional<FriendShip> friendRequest = friendShipRepository.findFriendShip(friendShip.getMember().getId(),
                friendShip.getFriend().getId());
        if (friendRequest.isPresent())
            return true;
        return false;
    }

    //id로 두 멤버가 친구인지 확인
    public boolean isFriend(Long memberId, Long friendId) {
        FriendShip friendShip1 = friendShipRepository.findByMemberIdAndFriendIdAndStatus(memberId, friendId, FriendStatus.FRIEND);
        FriendShip friendShip2 = friendShipRepository.findByMemberIdAndFriendIdAndStatus(friendId, memberId, FriendStatus.FRIEND);

        if (friendShip1 != null && friendShip2 != null &&
                friendShip1.getStatus() == FriendStatus.FRIEND && friendShip2.getStatus() == FriendStatus.FRIEND) {
            return true;
        }
        return false;
    }


    // 친구관계를 맺었는지 확인하는 함수
    @Transactional
    public void checkFriendShipStatus(FriendShip friendShip) {
        Optional<FriendShip> findFriendShip = friendShipRepository.findFriendRequest(friendShip.getMember().getId(),
                friendShip.getFriend().getId());
        if (findFriendShip.isPresent()) {  //상대쪽에서 보내놓은 친구요청이 있는 경우
            findFriendShip.get().setStatus(FriendStatus.FRIEND);
            friendShip.setStatus(FriendStatus.FRIEND);

            //두 회원이 팀을 맺게 됐으므로 채팅방을 생성해줌
            Member member = friendShip.getMember();
            Member friend = friendShip.getFriend();
            roomService.makePersonalChattingRoom(member, friend);

        } else { //상대쪽에서 보내놓은 친구요청이 없는 경우
            friendShip.setStatus(FriendStatus.PENDING);
        }
    }

    public List<Member> findFriendList(Member member) {
        List<Member> friendList = friendShipRepository.findFriendListByMemberId(member.getId(), FriendStatus.FRIEND);
        return friendList;
    }

    @Transactional
    public void deleteFriendShip(Long memberId, Long friendId) {
        Optional<FriendShip> findFriendShip1 = friendShipRepository.findFriendShip(memberId, friendId);
        Optional<FriendShip> findFriendShip2 = friendShipRepository.findFriendShip(friendId, memberId);

        friendShipRepository.deleteById(findFriendShip1.get().getId());
        friendShipRepository.deleteById(findFriendShip2.get().getId());
    }

    public List<Member> findFriendReceiveList(Long memberId) {
        List<Member> friendReceiveList = friendShipRepository.findFriendReceiveList(memberId, FriendStatus.PENDING);
        return friendReceiveList;
    }

    public Optional<FriendShip> findFriendShipWithId(Long memberId, Long friendShipId) {
        return friendShipRepository.findFriendShip(memberId, friendShipId);
    }

    // 친구신청 거절은 테이블에 있는 행을 삭제함
    @Transactional
    public void rejectFriendShip(Long memberId, Long friendId) {
        Optional<FriendShip> findFriendShip = friendShipRepository.findFriendShip(memberId, friendId);
        friendShipRepository.deleteById(findFriendShip.get().getId());
    }
}
