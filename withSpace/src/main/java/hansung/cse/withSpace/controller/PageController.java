package hansung.cse.withSpace.controller;

import hansung.cse.withSpace.domain.space.Page;
import hansung.cse.withSpace.requestdto.space.page.PageUpdateContentRequestDto;
import hansung.cse.withSpace.requestdto.space.page.PageUpdateTitleRequestDto;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.space.page.*;
import hansung.cse.withSpace.service.PageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PageController {
    private static final int SUCCESS = 200;
    private final PageService pageService;

    @GetMapping("/page/{pageId}") //페이지 조회
    public ResponseEntity<PageDetailDto> getPage(@PathVariable Long pageId, HttpServletRequest request) {
        //jwtAuthenticationFilter.isPageOwner(request, pageId); //접근권한 확인
        Page page = pageService.findOne(pageId);
        PageDetailDto pageDetailDto = new PageDetailDto(page);

        return ResponseEntity.ok(pageDetailDto);
    }

    @GetMapping("/page/{pageId}/hierarchy") //페이지 계층 조회
    public ResponseEntity<List<PageHierarchyDto>> getPageHierarchy(@PathVariable Long pageId,
                                                                   HttpServletRequest request) {
        //jwtAuthenticationFilter.isPageOwner(request, pageId); //접근권한 확인

        List<PageHierarchyDto> pageHierarchy = pageService.getPageHierarchy(pageId);

        return new ResponseEntity<>(pageHierarchy, HttpStatus.OK);
    }

    @PatchMapping("/page/{pageId}/title")  //페이지 제목 업데이트
    //@PreAuthorize("@customSecurityUtil.isPageOwner(#pageId)")
    public ResponseEntity<PageBaseResponse> updatePageTitle(@PathVariable Long pageId,
                                                            @RequestBody PageUpdateTitleRequestDto requestDto,
                                                            HttpServletRequest request) {
        //jwtAuthenticationFilter.isPageOwner(request, pageId); //접근권한 확인

        pageService.updatePageTitle(pageId, requestDto);

        PageBaseResponse pageBaseResponse = new PageBaseResponse(pageId, SUCCESS, "페이지 제목 변경 완료");

        return new ResponseEntity<>(pageBaseResponse, HttpStatus.OK);
    }

    @PatchMapping("/page/{pageId}/content")  //페이지 내용 업데이트
    public ResponseEntity<PageBaseResponse> updatePageContent(@PathVariable Long pageId,
                                                              @RequestBody PageUpdateContentRequestDto requestDto,
                                                              HttpServletRequest request) {
        //jwtAuthenticationFilter.isPageOwner(request, pageId); //접근권한 확인

        pageService.updatePageContent(pageId, requestDto);

        PageBaseResponse pageBaseResponse = new PageBaseResponse(pageId, SUCCESS, "페이지 내용 변경 완료");

        return new ResponseEntity<>(pageBaseResponse, HttpStatus.OK);
    }

    @PatchMapping("/page/{pageId}/trashcan") //페이지 휴지통 이동
    public ResponseEntity<BasicResponse> throwPage(@PathVariable Long pageId, HttpServletRequest request) {
        //jwtAuthenticationFilter.isPageOwner(request, pageId); //접근권한 확인
        PageTrashCanDto pageTrashCanDto = pageService.throwPage(pageId);
        BasicResponse basicResponse = new BasicResponse<>(1, "페이지 휴지통 이동 성공", pageTrashCanDto);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }


    @DeleteMapping("/page/{pageId}/trashcan") // (쓰레기통에 있는) 페이지 삭제
    public ResponseEntity<BasicResponse> deletePage(@PathVariable Long pageId, HttpServletRequest request) {
        //jwtAuthenticationFilter.isPageOwner(request, pageId); //접근권한 확인
        pageService.deletePage(pageId);
        BasicResponse basicResponse
                = new BasicResponse(1, "페이지 삭제 완료", null);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }
}
