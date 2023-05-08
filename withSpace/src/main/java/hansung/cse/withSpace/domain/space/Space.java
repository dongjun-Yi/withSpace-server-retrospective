package hansung.cse.withSpace.domain.space;

import hansung.cse.withSpace.domain.chat.Room;
import jakarta.persistence.*;
import lombok.*;
import hansung.cse.withSpace.domain.space.schedule.Schedule;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Space {

    @Id
    @GeneratedValue
    @Column(name = "space_id")
    private Long id;

    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL)
    private List<Page> pageList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "space"
            , cascade = CascadeType.ALL)
    private Schedule schedule;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "space"
            , cascade = CascadeType.ALL)
    private List<Room> roomList = new ArrayList<>(); //채팅방들
    private int topLevelPageCount = 0;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TrashCan trashCan;


    public void setTopLevelPageCount(int count) {
        this.topLevelPageCount = count;
    }
    public void makeRelation(Schedule schedule) {
        this.schedule = schedule;
    }

    public void makeTrashCanRelation(TrashCan trashCan) {
        this.trashCan = trashCan;
    }

    public void removePage(Page page) {
        pageList.remove(page);
    }

}
