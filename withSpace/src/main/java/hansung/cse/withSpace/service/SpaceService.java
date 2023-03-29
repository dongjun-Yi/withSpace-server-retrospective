package hansung.cse.withSpace.service;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.Team;
import hansung.cse.withSpace.domain.space.MemberSpace;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.domain.space.TeamSpace;
import hansung.cse.withSpace.repository.SpaceRepository;
import hansung.cse.withSpace.exception.space.SpaceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;

    public Space findOne(Long spaceId) {
        return spaceRepository.findById(spaceId).orElseThrow(()
                -> new SpaceNotFoundException("스페이스 조회 실패: 해당하는 스페이스가 존재하지 않습니다."));
    }

//    public Space assignSpace(Object obj) { //스페이스 할당
//        if (obj instanceof Member) {
//            MemberSpace memberSpace = new MemberSpace((Member) obj);
//            memberSpace = spaceRepository.save(memberSpace);
//            ((Member) obj).setMemberSpace(memberSpace);
//            return memberSpace;
//        } else if (obj instanceof Team) {
//            TeamSpace teamSpace = new TeamSpace((Team) obj);
//            teamSpace = spaceRepository.save(teamSpace);
//            ((Team) obj).setTeamSpace(teamSpace);
//            return teamSpace;
//        } else {
//            // 여기오면 안됨
//            return null;
//        }
//    }


}
