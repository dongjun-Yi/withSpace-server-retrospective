package hansung.cse.withSpace.domain.space;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Page {

    @Id
    @GeneratedValue
    @Column(name = "page_id")
    private Long id;

    @OneToMany(mappedBy = "parentPage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Page> childPages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_page_id")
    private Page parentPage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trashcan_id")
    private TrashCan trashCan;

//    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Block> blockList = new ArrayList<>();
    private String title;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long parentId;


    public Page(String title) {
        this.title = title;
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }
    public void addchildPage(Page childPage) {
        childPage.parentPage = this;
        //childPage.setParentPage(this);
        this.childPages.add(childPage);
        childPage.parentId = this.getId();
    }

    public void makeRelationPageSpace(Page page, Space space) {
        this.space = space;
        if (page.getParentPage() == null) { //페이지가 제일 상위페이지었다면
            space.setTopLevelPageCount(space.getTopLevelPageCount() + 1);
        }
        space.getPageList().add(page);
    }

    @Transactional
    public void removeRelationPageSpace(){  // 페이지와 스페이스의 연결고리를 끊어줌(엔티티 삭제 X)
        space.getPageList().remove(this);
        this.space = null;
    }

    public void putTrashCan(TrashCan trashCan) {
        trashCan.getPageList().add(this);
        this.trashCan = trashCan;
        removeRelationPageSpace(); //스페이스와 페이지 연관관계 제거
        //space.removePage(this);
    }

    public void outTrashCan(TrashCan trashCan) {
        trashCan.getPageList().remove(this);
        Space space = trashCan.getSpace();
        this.trashCan = null;
        makeRelationPageSpace(this, space); //스페이스와 페이지 연관관계 연결
    }

    public void updatePageTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePageContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

//    public void makeChildParentRelation(Page parentPage) {
//        this.parentPage = parentPage;
//        this.parentId = parentPage.getId();
//
//    }

}
