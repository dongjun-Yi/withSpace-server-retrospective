package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.domain.friend.FriendStatus;
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

    private final FriendShipRepository friendShipRepository;

    @Transactional
    public Long addFriend(FriendShip friendShip) {
        FriendShip saveFriendRequest = friendShipRepository.save(friendShip);
        ValidateFriendShip(friendShip);
        return saveFriendRequest.getId();
    }

    //친구관계를 맺었는지 확인하는 함수
    public void ValidateFriendShip(FriendShip friendShip) {
        Optional<FriendShip> findFriendShip = friendShipRepository.findFriendByMemberId(friendShip.getMember().getId());
        if (!findFriendShip.isEmpty()) {
            findFriendShip.get().setStatus(FriendStatus.ACCEPTED);
            friendShip.setStatus(FriendStatus.ACCEPTED);
        } else {
            friendShip.setStatus(FriendStatus.PENDING);
        }
    }

    public List<Member> findFriendList(Member member) {
        List<Member> friendList = friendShipRepository.findFriendListByMemberId(member.getId(), FriendStatus.ACCEPTED);
        return friendList;
    }

    @Transactional
    public void deleteFriendShip(Long memberId, Long friendId) {
        Optional<FriendShip> findFriendShip1 = friendShipRepository.findFriendShip(memberId, friendId);
        Optional<FriendShip> findFriendShip2 = friendShipRepository.findFriendShip(friendId, memberId);

        friendShipRepository.deleteById(findFriendShip1.get().getId());
        friendShipRepository.deleteById(findFriendShip2.get().getId());
    }
}