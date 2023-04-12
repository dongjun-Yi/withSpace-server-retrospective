package hansung.cse.withSpace.domain.space;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

//    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Block> blockList = new ArrayList<>();
    private String title;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long parentId;


    public Page(String title) {
        this.title = title;
    }

    public void addchildPage(Page childPage) {
        childPage.parentPage = this;
        //childPage.setParentPage(this);
        this.childPages.add(childPage);
        childPage.parentId = this.getId();
    }

    public void makeRelationPageSpace(Page page, Space space) {
        this.space = space;
        space.getPageList().add(page);
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
