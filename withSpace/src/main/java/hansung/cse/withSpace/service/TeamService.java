package hansung.cse.withSpace.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import hansung.cse.withSpace.domain.*;
import hansung.cse.withSpace.domain.space.TeamSpace;
import hansung.cse.withSpace.domain.space.schedule.Schedule;
import hansung.cse.withSpace.exception.team.TeamNotFoundException;
import hansung.cse.withSpace.repository.MemberTeamRepository;
import hansung.cse.withSpace.repository.ScheduleRepository;
import hansung.cse.withSpace.repository.SpaceRepository;
import hansung.cse.withSpace.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;

    private final MemberTeamRepository memberTeamRepository;
    private final SpaceRepository spaceRepository;
    private final ScheduleRepository scheduleRepository;

    public Team findOne(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀 조회 실패"));
    }


    @Transactional
    public Long makeTeam(Member member, String teamName) { //팀 생성 - 팀을 생성하는 회원에게는 바로 팀 부여

        Team team = new Team(teamName, member.getId());

        teamRepository.save(team);

        makeMemberTeamRelation(member, team); //멤버팀 연관관계 생성

        //팀 생성시 스페이스 생성 + 부여
        TeamSpace teamSpace = new TeamSpace(team);
        team.setTeamSpace(teamSpace);
        spaceRepository.save(teamSpace);

        //스페이스 생성했으니 바로 스케줄도 만들어서 줌..
        Schedule schedule = new Schedule(teamSpace);
        scheduleRepository.save(schedule);


        return team.getId();
    }

    @Transactional
    public Long join(Member member, Long teamId) { //팀 가입
        Team team = findOne(teamId);

        MemberTeamId memberTeamId = new MemberTeamId(member.getId(), teamId);
        Optional<MemberTeam> memberTeamOptional = memberTeamRepository.findById(memberTeamId);
        MemberTeam memberTeam = memberTeamOptional.orElse(null);

        if (memberTeam == null) { //이미 가입되어있는 경우
            makeMemberTeamRelation(member, team);
        }

        return teamId;
    }

    @Transactional
    public void makeMemberTeamRelation(Member member, Team team) {

        // 멤버-팀 관계 생성
        MemberTeam memberTeam = new MemberTeam(member, team);
        team.setMemberCount(team.getMemberCount() + 1);
        memberTeamRepository.save(memberTeam);

        // 멤버 - 멤버팀 - 팀 이어주기
        member.getMemberTeams().add(memberTeam);
        team.getMemberTeams().add(memberTeam);


    }


    @Transactional
    public void deleteTeam(Long teamId) {
        Team team = findOne(teamId);

        QMemberTeam memberTeam = QMemberTeam.memberTeam;
        BooleanExpression teamIdEquals = memberTeam.team.id.eq(teamId);
        Iterable<MemberTeam> memberTeams = memberTeamRepository.findAll(teamIdEquals);
        memberTeamRepository.deleteAll(memberTeams);


        teamRepository.delete(team);
    }

}
