package hansung.cse.withSpace.repository;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.domain.friend.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {

    @Query("select f.friend from FriendShip f where f.member.id=:memberId and f.status=:status")
    List<Member> findFriendListByMemberId(@Param("memberId") Long memberId, @Param("status") FriendStatus status);

    @Query("select f from FriendShip f where f.member.id=:memberId and f.friend.id=:friendId")
    public Optional<FriendShip> findFriendShip(@Param("memberId") Long memberId, @Param("friendId") Long friendId);

    @Query("select f from FriendShip f where f.member.id=:friendId and f.friend.id=:memberId")
    public Optional<FriendShip> findFriendRequest(@Param("memberId") Long memberId, @Param("friendId") Long friendId);


    FriendShip findByMemberIdAndFriendIdAndStatus(Long memberId, Long friendId, FriendStatus status);

    @Query("select f.member from FriendShip f where f.friend.id=:friendId and f.status=:status")
    List<Member> findFriendReceiveList(@Param("friendId") Long friendId, @Param("status") FriendStatus status);

}
