package hansung.cse.withSpace.controller;

import hansung.cse.withSpace.config.jwt.JwtAuthenticationFilter;
import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.Team;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.exception.member.MemberNotFoundException;
import hansung.cse.withSpace.exception.member.MemberUpdateException;
import hansung.cse.withSpace.exception.team.TeamNotFoundException;
import hansung.cse.withSpace.requestdto.team.CreateTeamRequestDto;
import hansung.cse.withSpace.requestdto.team.JoinTeamRequestDto;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.space.MemberSpaceDto;
import hansung.cse.withSpace.responsedto.space.TeamSpaceDto;
import hansung.cse.withSpace.responsedto.team.CreateTeamResponse;
import hansung.cse.withSpace.responsedto.team.TeamListDto;
import hansung.cse.withSpace.responsedto.team.TeamSearchByNameDto;
import hansung.cse.withSpace.service.MemberService;
import hansung.cse.withSpace.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TeamController {
    private static final int SUCCESS = 200;
    private static final int CREATED = 201;
    private final TeamService teamService;
    private final MemberService memberService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @PostMapping("/team") //팀생성
    public ResponseEntity<CreateTeamResponse> createTeam(@Validated @RequestBody CreateTeamRequestDto teamRequest) {
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

    @GetMapping("/team/name")
    public ResponseEntity<BasicResponse> getTeamByName(@RequestParam String teamName,
                                             @RequestParam(defaultValue = "10") int limit) {
        List<TeamSearchByNameDto> teamListDto= teamService.searchTeamsByName(teamName, limit);
        BasicResponse basicResponse = new BasicResponse(1, "팀 조회 성공", teamListDto);
        return new ResponseEntity<>(basicResponse, HttpStatus.CREATED);
    }

//    @GetMapping("/team/name/{teamName}") //팀 이름으로 조회
//    public ResponseEntity<BasicResponse> getTeamByName(@PathVariable String teamName) {
//        Team team = teamService.searchTeamByName(teamName);
//
//        TeamListDto teamListDto = new TeamListDto(team);
//
//        BasicResponse basicResponse = new BasicResponse(1, "팀 조회 성공", teamListDto);
//
//        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
//
//    }

    @GetMapping("/team/{teamId}/space") //팀 스페이스 조회
    @PreAuthorize("@jwtAuthenticationFilter.isMemberInTeam(#request, #teamId)") // 현재 로그인한 사용자가 team에 가입되어있는 경우에만 접근 허용
    public ResponseEntity<BasicResponse> getTeamSpace(@PathVariable Long teamId,
                                                      HttpServletRequest request) {
        Team team = teamService.findOne(teamId);
        TeamSpaceDto teamSpaceDto = new TeamSpaceDto(team);
        BasicResponse basicResponse = new BasicResponse<>(1, "스페이스 조회 성공", teamSpaceDto);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

//    @DeleteMapping("/team/{teamId}") //팀 삭제
//    @PreAuthorize("@jwtAuthenticationFilter.isTeamHost(#request, #teamId)")
//    public ResponseEntity<BasicResponse> deleteTeam(@PathVariable Long teamId,
//                                                    HttpServletRequest request) {
//        Team team = teamService.findOne(teamId);
//
//        teamService.deleteTeam(team.getId());
//
//        BasicResponse basicResponse = new BasicResponse(1, "팀 삭제 성공", null);
//
//        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
//    }

    @DeleteMapping("/team/{teamId}") //팀 나가기
    @PreAuthorize("@jwtAuthenticationFilter.isInTeam(#request, #teamId)")
    public ResponseEntity<BasicResponse> getOutTeam(@PathVariable Long teamId,
                                                    HttpServletRequest request) {
        //Team team = teamService.findOne(teamId);
        Member member = jwtAuthenticationFilter.findMemberByUUID(request);
        teamService.getOutTeam(teamId, member.getId());

        BasicResponse basicResponse = new BasicResponse(1, "팀 나가기 성공", null);

        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }
}
