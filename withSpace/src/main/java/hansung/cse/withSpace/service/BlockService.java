package hansung.cse.withSpace.service;

import hansung.cse.withSpace.exception.block.BlockNotFoundException;
import hansung.cse.withSpace.exception.member.MemberNotFoundException;
import hansung.cse.withSpace.exception.page.PageNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.domain.space.Page;
import hansung.cse.withSpace.domain.space.Block;
import hansung.cse.withSpace.repository.BlockRepository;
import hansung.cse.withSpace.repository.MemberRepository;
import hansung.cse.withSpace.repository.PageRepository;
import hansung.cse.withSpace.requestdto.space.page.block.BlockUpdateRequestDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;

//    private final PageRepository pageRepository;
//
//    private final MemberRepository memberRepository;

    private final MemberService memberService;
    private final PageService pageService;

    public Block findOne(Long blockId) {
        return blockRepository.findById(blockId).orElseThrow(()
                -> new BlockNotFoundException("해당하는 블록이 존재하지 않습니다."));
    }

    @Transactional
    public Long makeBlock(Long pageId, Long memberId) { //블럭 생성
        Page page = pageService.findOne(pageId);
        Member member = memberService.findOne(memberId);

        Block block = new Block(page, member);

        blockRepository.save(block);

        return block.getId();
    }

    @Transactional
    public Long updateBlock(Long blockId, BlockUpdateRequestDto requestDto) { //블럭 업데이트
        Block block = findOne(blockId);

//        LocalDateTime now = LocalDateTime.now();

//        block.setUpdatedAt(now);

        Member updatedBy = memberService.findOne(requestDto.getMemberId());
        String content = requestDto.getContent();

        block.update(updatedBy, content);


//
//        if (requestDto.getMemberId() != null) {
//            Member updatedBy = memberService.findOne(requestDto.getMemberId());
//            block.setUpdatedBy(updatedBy);
//        }
//
//
//
//        if (requestDto.getContent() != null) {
//            block.setContent(requestDto.getContent());
//        }

        blockRepository.save(block);

        return block.getId();
    }
    @Transactional
    public void deleteBlock(Long blockId) {
        Block block = findOne(blockId);
        blockRepository.delete(block);
    }


}
