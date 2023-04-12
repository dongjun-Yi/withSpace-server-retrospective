package hansung.cse.withSpace.service;


import hansung.cse.withSpace.domain.space.Page;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.exception.page.PageNotFoundException;
import hansung.cse.withSpace.repository.PageRepository;
import hansung.cse.withSpace.requestdto.space.page.PageCreateRequestDto;
import hansung.cse.withSpace.requestdto.space.page.PageUpdateContentRequestDto;
import hansung.cse.withSpace.requestdto.space.page.PageUpdateTitleRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PageService {

    private final PageRepository pageRepository;
    private final SpaceService spaceService;

    public Page findOne(Long pageId) {
        return pageRepository.findById(pageId).orElseThrow(()
                -> new PageNotFoundException("페이지를 찾을 수 없습니다."));

    }

    @Transactional
    public Long makePage(Long spaceId, PageCreateRequestDto pageCreateRequestDto) {
        Space space = spaceService.findOne(spaceId);
        Page page = new Page(pageCreateRequestDto.getTitle());

        //setPageSpace(page, space);
        page.makeRelationPageSpace(page, space);

        //parent 페이지가 있는 경우
        if (pageCreateRequestDto.getParentPageId() != null && pageCreateRequestDto.getParentPageId().isPresent()) {
            Long parentPageId = pageCreateRequestDto.getParentPageId().orElse(null);
            Page parentPage = pageRepository.findById(parentPageId)
                    .orElseThrow(() -> new PageNotFoundException("부모 페이지 찾기 실패."));
            parentPage.addchildPage(page);


            //pageRepository.save(parentPage);

        }

        pageRepository.save(page);

        return page.getId();
    }


//    @Transactional
//    public void setPageSpace(Page page, Space space) {
//        page.setSpace(space);
//        space.getPageList().add(page);
//    }



    @Transactional
    public void updatePageTitle(Long pageId, PageUpdateTitleRequestDto requestDto) {
        Page page = findOne(pageId);
        page.updatePageTitle(requestDto.getTitle());

//        if (requestDto.getTitle() != null) {
//            page.setTitle(requestDto.getTitle());
//        }
//
//        page.setUpdatedAt(LocalDateTime.now());
    }
    @Transactional
    public void updatePageContent(Long pageId, PageUpdateContentRequestDto requestDto) {
        Page page = findOne(pageId);
        page.updatePageContent(requestDto.getContent());
    }

    @Transactional
    public void deletePage(Long pageId) {
        Page page = findOne(pageId);
        pageRepository.delete(page);
    }


}
