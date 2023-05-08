package hansung.cse.withSpace.repository.member;

import com.querydsl.jpa.impl.JPAQuery;
import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.QMember;
import hansung.cse.withSpace.domain.QTeam;
import hansung.cse.withSpace.domain.Team;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class MemberRepositoryImpl  extends QuerydslRepositorySupport implements MemberRepositoryCustom{

    public MemberRepositoryImpl() {
        super(Member.class);
    }

    @Override
    public List<Member> searchMembersByName(String query, int limit) {
        QMember member = QMember.member;


        JPAQuery<Member> jpaQuery = new JPAQuery<>(getEntityManager());
        List<Member> results = jpaQuery.from(member)
                .orderBy(member.memberName.asc())
                .fetch();

        return results;
    }

}
