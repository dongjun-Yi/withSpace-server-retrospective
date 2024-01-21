package hansung.cse.withSpace.repository;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.domain.friend.FriendStatus;
import hansung.cse.withSpace.service.FriendShipService;
import hansung.cse.withSpace.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class FriendShipRepositoryTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private FriendShipService friendShipService;

    @Autowired
    private FriendShipRepository friendShipRepository;

    @DisplayName("친구의 목록을 조회할 수 있다.")
    @Test
    void findFriendList() {
        //given
        Member memberA = new Member(UUID.randomUUID(), "memberA", "aaa@naver.com", "awqxxc");
        Member memberB = new Member(UUID.randomUUID(), "memberB", "bbb@naver.com", "zxv31ia");
        Member memberC = new Member(UUID.randomUUID(), "memberC", "ccc@naver.com", "pzjvv1");

        memberService.save(memberA);
        memberService.save(memberB);
        memberService.save(memberC);

        FriendShip friendShip1 = new FriendShip(memberA, memberB);
        FriendShip friendShip2 = new FriendShip(memberB, memberA);
        FriendShip friendShip3 = new FriendShip(memberA, memberC);
        FriendShip friendShip4 = new FriendShip(memberC, memberA);

        friendShipService.sendFriendRequest(friendShip1);
        friendShipService.sendFriendRequest(friendShip2);
        friendShipService.sendFriendRequest(friendShip3);
        friendShipService.sendFriendRequest(friendShip4);

        //when
        List<Member> memberAFriendList = friendShipRepository.findFriendListByMemberId(memberA.getId(), FriendStatus.FRIEND);

        //then
        assertThat(memberAFriendList).hasSize(2)
                .extracting("memberName", "email")
                .containsExactlyInAnyOrder(
                        tuple("memberB", "bbb@naver.com"),
                        tuple("memberC", "ccc@naver.com")
                );
    }
}