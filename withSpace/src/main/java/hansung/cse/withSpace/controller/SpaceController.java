package hansung.cse.withSpace.controller;


import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.Team;
import hansung.cse.withSpace.domain.space.MemberSpace;
import hansung.cse.withSpace.domain.space.Page;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.domain.space.TeamSpace;
import hansung.cse.withSpace.requestdto.space.page.PageCreateRequestDto;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.space.SpaceDto;
import hansung.cse.withSpace.responsedto.space.page.PageDto;
import hansung.cse.withSpace.service.MemberService;
import hansung.cse.withSpace.service.PageService;
import hansung.cse.withSpace.service.SpaceService;
import hansung.cse.withSpace.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class SpaceController {
    private static final int SUCCESS = 200;
    private static final int CREATED = 201;

    private final SpaceService spaceService;
    private final PageService pageService;

    private final MemberService memberService;
    private final TeamService teamService;

    @PostMapping("/space/{spaceId}/page") //페이지 생성
    @PreAuthorize("@customSecurityUtil.isSpaceOwner(#spaceId)")
    public ResponseEntity<BasicResponse> createPage(@PathVariable Long spaceId, @RequestBody PageCreateRequestDto pageCreateRequestDto) {
        Space space = spaceService.findOne(spaceId);

        Long pageId = pageService.makePage(spaceId, pageCreateRequestDto);
        Page page = pageService.findOne(pageId);

        BasicResponse basicResponse = new BasicResponse<>(1, "페이지 생성 성공",new PageDto(page));

        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @GetMapping("/space/{spaceId}") //스페이스 조회
    @PreAuthorize("@customSecurityUtil.isSpaceOwner(#spaceId)")
    public ResponseEntity<SpaceDto> getSpace(@PathVariable Long spaceId) {
        Space space = spaceService.findOne(spaceId);
        return ResponseEntity.ok(new SpaceDto(space));
    }


    //제거는 어차피 멤버나 팀 사라지면 스페이스 싹 날아가니깐 생략



}
