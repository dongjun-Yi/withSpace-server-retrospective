package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.domain.friend.FriendStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FriendShipServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private FriendShipService friendShipService;

    @DisplayName("친구관계를 맺고 싶은 회원에게 친구 신청을 보낼 수 있습니다.")
    @Test
    void sendFriendRequest() {
        //given
        Member memberA = new Member(UUID.randomUUID(), "memberA", "aaa@naver.com", "qwerzz");
        Member memberB = new Member(UUID.randomUUID(), "memberB", "bbb@naver.com", "zascq1");
        memberService.save(memberA);
        memberService.save(memberB);

        FriendShip friendShip = new FriendShip(memberA, memberB);

        //when
        Long friendShipId = friendShipService.sendFriendRequest(friendShip);

        //then
        assertThat(friendShipId).isNotNull();
        FriendShip findFriendShip = friendShipService.findFriendShipWithId(memberA.getId(), memberB.getId()).get();

        assertThat(findFriendShip.getStatus()).isEqualTo(FriendStatus.PENDING);
    }

    @DisplayName("친구 신청을 보낸 회원에게 친구신청을 보낼 경우 두 회원은 친구가 되어 FRIEND 상태가 된다.")
    @Test
    void sendFriendRequestWhoSendAlready() {
        //given
        Member memberA = new Member(UUID.randomUUID(), "memberA", "aaa@naver.com", "qwerzz");
        Member memberB = new Member(UUID.randomUUID(), "memberB", "bbb@naver.com", "zascq1");
        memberService.save(memberA);
        memberService.save(memberB);

        FriendShip friendShip1 = new FriendShip(memberA, memberB);
        FriendShip friendShip2 = new FriendShip(memberB, memberA);

        //when
        Long friendShipId1 = friendShipService.sendFriendRequest(friendShip1);
        Long friendShipId2 = friendShipService.sendFriendRequest(friendShip2);

        //then
        FriendShip findFriendShip = friendShipService.findFriendShipWithId(memberA.getId(), memberB.getId()).get();
        assertThat(findFriendShip.getStatus()).isEqualTo(FriendStatus.FRIEND);
    }
}