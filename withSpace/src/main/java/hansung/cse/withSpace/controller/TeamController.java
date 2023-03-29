package hansung.cse.withSpace.controller;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.Team;
import hansung.cse.withSpace.exception.member.MemberNotFoundException;
import hansung.cse.withSpace.exception.member.MemberUpdateException;
import hansung.cse.withSpace.exception.team.TeamNotFoundException;
import hansung.cse.withSpace.requestdto.team.CreateTeamRequestDto;
import hansung.cse.withSpace.requestdto.team.JoinTeamRequestDto;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.team.CreateTeamResponse;
import hansung.cse.withSpace.responsedto.team.TeamListDto;
import hansung.cse.withSpace.service.MemberService;
import hansung.cse.withSpace.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TeamController {
    private static final int SUCCESS = 200;
    private static final int CREATED = 201;
    private final TeamService teamService;
    private final MemberService memberService;

    @PostMapping("/team") //팀생성
    public ResponseEntity<CreateTeamResponse> createTeam(@RequestBody CreateTeamRequestDto teamRequest) {
        Member member = memberService.findOne(teamRequest.getMemberId());

        Long teamId = teamService.makeTeam(member, teamRequest.getTeamName());
        CreateTeamResponse createTeamResponse = new CreateTeamResponse(teamId, CREATED, "팀 생성 완료");
        return new ResponseEntity<>(createTeamResponse, HttpStatus.CREATED);
    }

    @PostMapping("/team/{teamId}/members") //팀 가입
    public ResponseEntity<BasicResponse> joinTeam(@PathVariable Long teamId, @RequestBody JoinTeamRequestDto teamRequest) {
        Member member = memberService.findOne(teamRequest.getMemberId());
        teamService.join(member, teamId);

        Team team = teamService.findOne(teamId);

        TeamListDto teamListDto = new TeamListDto(team);

        BasicResponse basicResponse = new BasicResponse(1, "팀 가입 성공", teamListDto);

        return new ResponseEntity<>(basicResponse, HttpStatus.CREATED);
    }

    @GetMapping("/team/{teamId}") //팀 조회
    public ResponseEntity<BasicResponse> getTeam(@PathVariable Long teamId) {
        Team team = teamService.findOne(teamId);

        TeamListDto teamListDto = new TeamListDto(team);

        BasicResponse basicResponse = new BasicResponse(1, "팀 조회 성공", teamListDto);

        return new ResponseEntity<>(basicResponse, HttpStatus.CREATED);

    }

    @DeleteMapping("/team/{teamId}") //팀 삭제
    public ResponseEntity<BasicResponse> deleteTeam(@PathVariable Long teamId) {
        Team team = teamService.findOne(teamId);

        teamService.deleteTeam(team.getId());

        BasicResponse basicResponse = new BasicResponse(1, "팀 삭제 성공", null);

        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }
}
