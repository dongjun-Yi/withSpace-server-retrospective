package hansung.cse.withSpace.controller;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.space.Block;
import hansung.cse.withSpace.domain.space.Page;
import hansung.cse.withSpace.requestdto.space.page.PageUpdateRequestDto;
import hansung.cse.withSpace.requestdto.space.page.block.BlockCreateRequestDto;
import hansung.cse.withSpace.requestdto.space.page.block.BlockUpdateRequestDto;
import hansung.cse.withSpace.responsedto.BasicResponse;
import hansung.cse.withSpace.responsedto.space.page.PageBaseResponse;
import hansung.cse.withSpace.responsedto.space.page.PageDetailDto;
import hansung.cse.withSpace.responsedto.space.page.block.BlockDto;
import hansung.cse.withSpace.service.BlockService;
import hansung.cse.withSpace.service.MemberService;
import hansung.cse.withSpace.service.PageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PageController {
    private static final int SUCCESS = 200;
    private static final int CREATED = 201;
    private final PageService pageService;
    private final BlockService blockService;
    private final MemberService memberService;

    //페이지 생성은 SpaceController에서


    @PostMapping("/page/{pageId}/block") //블록 생성
    public ResponseEntity<BasicResponse> createBlock(@PathVariable Long pageId, @RequestBody BlockCreateRequestDto blockCreateRequestDto) {
        Long memberId = blockCreateRequestDto.getMemberId();
        Long blockId = blockService.makeBlock(pageId, memberId);
        Block block = blockService.findOne(blockId);

        BasicResponse basicResponse = new BasicResponse(1, "블럭 생성 성공", new BlockDto(block));

        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @GetMapping("/page/{pageId}") //페이지 조회
    public ResponseEntity<PageDetailDto> getPage(@PathVariable Long pageId) {
        Page page = pageService.findOne(pageId);
        PageDetailDto pageDetailDto = new PageDetailDto(page);

        return ResponseEntity.ok(pageDetailDto);
    }

    @PatchMapping("/page/{pageId}")  //페이지 제목 변경
    public ResponseEntity<PageBaseResponse> updatePage(@PathVariable Long pageId, @RequestBody PageUpdateRequestDto requestDto){

        pageService.updatePage(pageId, requestDto);

        PageBaseResponse pageBaseResponse = new PageBaseResponse(pageId, SUCCESS, "페이지 제목 변경 완료");

        return new ResponseEntity<>(pageBaseResponse, HttpStatus.OK);
    }

    @PatchMapping("/block/{blockId}") //블럭 업데이트
    public ResponseEntity<BasicResponse> updateBlock(@PathVariable Long blockId, @RequestBody BlockUpdateRequestDto requestDto) {
//        Optional<Block> optionalBeforeBlock = blockService.findOne(blockId);
//        Block beforUpdateBlock = optionalBeforeBlock.orElseThrow(() -> new EntityNotFoundException("블럭을 찾을 수 없습니다. blockId: " + blockId));

        Member member = memberService.findOne(requestDto.getMemberId());

        // 업데이트
        Long updateBlockId = blockService.updateBlock(blockId, requestDto);
        Block block = blockService.findOne(updateBlockId);
//        Optional<Block> optionalBlock = blockService.findOne(blockId);
//        Block block = optionalBlock.orElseThrow(() -> new EntityNotFoundException("블럭을 찾을 수 없습니다. blockId: " + blockId));

        BasicResponse basicResponse = new BasicResponse(1, "블럭 업데이트 성공", new BlockDto(block));

        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @DeleteMapping("/page/{pageId}") // 페이지 삭제
    public ResponseEntity<BasicResponse> deletePage(@PathVariable Long pageId) {
        pageService.deletePage(pageId);

        BasicResponse basicResponse
                = new BasicResponse(1, "페이지 삭제 완료", null);

        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @DeleteMapping("/block/{blockId}") //블록 삭제
    public ResponseEntity<BasicResponse> deleteBlock(@PathVariable Long blockId) {
        blockService.deleteBlock(blockId);

        BasicResponse basicResponse = new BasicResponse(1, "블럭 삭제 성공", null);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }


}
