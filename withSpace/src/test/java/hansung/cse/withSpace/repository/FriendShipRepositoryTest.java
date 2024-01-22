package hansung.cse.withSpace.repository;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.domain.friend.FriendStatus;
import hansung.cse.withSpace.repository.member.MemberRepository;
import hansung.cse.withSpace.service.FriendShipService;
import hansung.cse.withSpace.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class FriendShipRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FriendShipService friendShipService;

    @Autowired
    private FriendShipRepository friendShipRepository;

    @DisplayName("친구의 목록을 조회할 수 있다.")
    @Test
    void findFriendList() {
        //given
        Member memberA = makeMember(UUID.randomUUID(), "memberA", "aaa@naver.com", "awqxxc");
        Member memberB = makeMember(UUID.randomUUID(), "memberB", "bbb@naver.com", "zxv31ia");
        Member memberC = makeMember(UUID.randomUUID(), "memberC", "ccc@naver.com", "pzjvv1");

        memberRepository.saveAll(List.of(memberA, memberB, memberC));

        FriendShip friendShip1 = sendFriendShipRequest(memberA, memberB);
        FriendShip friendShip2 = sendFriendShipRequest(memberB, memberA);
        FriendShip friendShip3 = sendFriendShipRequest(memberA, memberC);
        FriendShip friendShip4 = sendFriendShipRequest(memberC, memberA);

        sendRequestFriendShips(List.of(friendShip1, friendShip2, friendShip3, friendShip4));

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

    private void sendRequestFriendShips(List<FriendShip> friendShips) {
        for (FriendShip friendShip : friendShips) {
            friendShipService.sendFriendRequest(friendShip);
        }
    }

    private static FriendShip sendFriendShipRequest(Member memberA, Member memberB) {
        return new FriendShip(memberA, memberB);
    }

    private Member makeMember(UUID randomUUID, String name, String email, String password) {
        return new Member(randomUUID, name, email, password);
    }

}