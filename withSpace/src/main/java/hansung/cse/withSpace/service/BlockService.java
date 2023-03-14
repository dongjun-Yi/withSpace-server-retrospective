package hansung.cse.withSpace.service;

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

    private final PageRepository pageRepository;
    private final BlockRepository blockRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long makeBlock(Long pageId, Long memberId) { //블럭 생성
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new EntityNotFoundException("페이지를 찾을 수 없습니다. pageId: " + pageId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다. memberId: " + memberId));

        Block block = new Block(page, member);


        blockRepository.save(block);

        return block.getId();
    }

    @Transactional
    public Long updateBlock(Long blockId, BlockUpdateRequestDto requestDto) { //블럭 업데이트
        Block block = blockRepository.findById(blockId)
                .orElseThrow(() -> new EntityNotFoundException("블록을 찾을 수 없습니다. blockId: " + blockId));

        LocalDateTime now = LocalDateTime.now();

        block.setUpdatedAt(now);

        if (requestDto.getMemberId() != null) {
            Member updatedBy = memberRepository.findById(requestDto.getMemberId())
                    .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다. memberId: " + requestDto.getMemberId()));
            block.setUpdatedBy(updatedBy);
        }

        if (requestDto.getUpdatedAt() != null) {
            block.setUpdatedAt(requestDto.getUpdatedAt());
        }

        if (requestDto.getContent() != null) {
            block.setContent(requestDto.getContent());
        }

        blockRepository.save(block);

        return block.getId();
    }
    @Transactional
    public void deleteBlock(Long blockId) {
        Optional<Block> optionalBlock = blockRepository.findById(blockId);
        Block block = optionalBlock.orElseThrow(()
                -> new EntityNotFoundException("블럭이 없습니다. blockId: " + blockId));
        blockRepository.delete(block);
    }

    public Optional<Block> findOne(Long blockId) {
        return blockRepository.findById(blockId);
    }

}
