package hansung.cse.withSpace.controller;


import hansung.cse.withSpace.domain.space.*;
import hansung.cse.withSpace.requestdto.space.page.PageCreateRequestDto;
import hansung.cse.withSpace.requestdto.space.page.PageRestoreRequestDto;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.space.SpaceDto;
import hansung.cse.withSpace.responsedto.space.page.PageDto;
import hansung.cse.withSpace.responsedto.space.page.PageTrashCanDto;
import hansung.cse.withSpace.service.MemberService;
import hansung.cse.withSpace.service.PageService;
import hansung.cse.withSpace.service.SpaceService;
import hansung.cse.withSpace.service.TeamService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    @PreAuthorize("@jwtAuthenticationFilter.isSpaceOwner(#request, #spaceId)")
    public ResponseEntity<BasicResponse> createPage(@PathVariable Long spaceId,
                                                    @RequestBody PageCreateRequestDto pageCreateRequestDto,
                                                    HttpServletRequest request) {
        //jwtAuthenticationFilter.isSpaceOwner( request, spaceId); //접근권한 확인
        Space space = spaceService.findOne(spaceId);
        Long pageId = pageService.makePage(spaceId, pageCreateRequestDto);
        Page page = pageService.findOne(pageId);

        BasicResponse basicResponse = new BasicResponse<>(1, "페이지 생성 성공", new PageDto(page));

        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @GetMapping("/space/{spaceId}") //스페이스 조회
    @PreAuthorize("@jwtAuthenticationFilter.isSpaceOwner(#request, #spaceId)")
    public ResponseEntity<SpaceDto> getSpace(@PathVariable Long spaceId,
                                             HttpServletRequest request) {
        Space space = spaceService.findOne(spaceId);
        return ResponseEntity.ok(new SpaceDto(space));
    }

    @GetMapping("/space/{spaceId}/trashcan") // 휴지통 조회
    @PreAuthorize("@jwtAuthenticationFilter.isSpaceOwner(#request, #spaceId)")
    public ResponseEntity<List<PageTrashCanDto>> getTrashCanPage(@PathVariable Long spaceId,
                                                                 HttpServletRequest request) {
        TrashCan trashCan = spaceService.findOne(spaceId).getTrashCan();
        List<PageTrashCanDto> pageTrashCanDtoList = new ArrayList<>();

        for (Page page : trashCan.getPageList()) {
            if (page.getParentPage() == null) {
                pageTrashCanDtoList.add(new PageTrashCanDto(page));
            }
        }
        return new ResponseEntity<>(pageTrashCanDtoList, HttpStatus.OK);
    }

    @PatchMapping("/space/{spaceId}/trashcan/{pageId}/restore") // 휴지통의 특정 페이지 복구
    @PreAuthorize("@jwtAuthenticationFilter.isSpaceOwner(#request, #spaceId)")
    public ResponseEntity<BasicResponse> restorePage(@PathVariable Long spaceId,
                                                     @PathVariable Long pageId,
                                                     @RequestBody PageRestoreRequestDto pageRestoreRequestDto,
                                                     HttpServletRequest request) {
        pageService.restorePageAndChildren(pageId, spaceId, pageRestoreRequestDto.getCurrentPageId());
        BasicResponse basicResponse = new BasicResponse<>(1, "페이지 복구 성공", null);


        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @DeleteMapping("/space/{spaceId}/trashcan/{pageId}") // 휴지통의 페이지 삭제
    @PreAuthorize("@jwtAuthenticationFilter.isSpaceOwner(#request, #spaceId)")
    public ResponseEntity<BasicResponse> deleteTrashCanPage(@PathVariable Long spaceId,
                                                            @PathVariable Long pageId,
                                                            HttpServletRequest request) {
        pageService.deletePage(pageId);
        BasicResponse basicResponse = new BasicResponse<>(1, "페이지 삭제 성공", null);

        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }
}
