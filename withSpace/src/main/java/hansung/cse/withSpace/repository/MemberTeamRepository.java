package hansung.cse.withSpace.repository;

import com.querydsl.core.types.Predicate;
import hansung.cse.withSpace.domain.MemberTeam;
import hansung.cse.withSpace.domain.MemberTeamId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberTeamRepository extends JpaRepository<MemberTeam, MemberTeamId>, QuerydslPredicateExecutor<MemberTeam> {
    @Query("SELECT mt FROM MemberTeam mt WHERE mt.team.id = :teamId")
    List<MemberTeam> findByTeamId(@Param("teamId") Long teamId);

    List<MemberTeam> findAll(Predicate predicate);
}