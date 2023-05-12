package hansung.cse.withSpace.repository;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.domain.friend.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {

    @Query("select f from FriendShip f where f.friend.id = :memberId")
    public Optional<FriendShip> findFriendByMemberId(@Param("memberId") Long memberId);

    @Query("select f.friend from FriendShip f where f.member.id=:memberId and f.status=:status")
    List<Member> findFriendListByMemberId(@Param("memberId") Long memberId, @Param("status") FriendStatus status);

    @Query("select f from FriendShip f where f.member.id=:memberId and f.friend.id=:friendId")
    public Optional<FriendShip> findFriendShip(@Param("memberId") Long memberId, @Param("friendId") Long friendId);

    FriendShip findByMemberIdAndFriendIdAndStatus(Long memberId, Long friendId, FriendStatus status);

}
