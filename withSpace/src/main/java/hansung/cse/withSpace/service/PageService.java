package hansung.cse.withSpace.service;


import hansung.cse.withSpace.domain.space.Page;
import hansung.cse.withSpace.domain.space.Space;
import hansung.cse.withSpace.exception.page.PageDeletionNotAllowedException;
import hansung.cse.withSpace.exception.page.PageNotFoundException;
import hansung.cse.withSpace.repository.PageRepository;
import hansung.cse.withSpace.requestdto.space.page.PageCreateRequestDto;
import hansung.cse.withSpace.requestdto.space.page.PageUpdateContentRequestDto;
import hansung.cse.withSpace.requestdto.space.page.PageUpdateTitleRequestDto;
import hansung.cse.withSpace.responsedto.space.page.PageHierarchyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public List<PageHierarchyDto> getPageHierarchy(Long pageId) { //계층 조회
        Page page = findOne(pageId);

        List<PageHierarchyDto> pageHierarchy = new ArrayList<>();
        while (page != null) {
            PageHierarchyDto pageDto = new PageHierarchyDto(page.getId(), page.getTitle());
            pageHierarchy.add(pageDto);
            page = page.getParentPage();
        }
        Collections.reverse(pageHierarchy);
        return pageHierarchy;
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
        else{ //최상위 페이지인 경우
            space.setTopLevelPageCount(space.getTopLevelPageCount()+1);
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

        Space space = page.getSpace();

        System.out.println("space.getTopLevelPageCount() = " + space.getTopLevelPageCount());
        System.out.println("page.getParentPage() = " + page.getParentPage());
        System.out.println(space.getTopLevelPageCount() == 1 && page.getParentPage() == null);
        
        if (space.getTopLevelPageCount() == 1 && page.getParentPage() == null ) {
            //스페이스에 최상위 페이지가 하나 + 삭제하려는 페이지가 또 제일 최상단 부모페이지면 삭제 불가

            throw new PageDeletionNotAllowedException("최상위 페이지가 하나밖에 없는 경우에는 삭제가 불가능합니다.");

        }

        if (page.getParentPage() == null) { //최상위 페이지인경우
            space.setTopLevelPageCount(space.getTopLevelPageCount() - 1);
        }


        pageRepository.delete(page);
    }


}
