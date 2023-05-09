package hansung.cse.withSpace.repository.member;



import hansung.cse.withSpace.domain.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> searchMembersByName(String query, int limit);
}
