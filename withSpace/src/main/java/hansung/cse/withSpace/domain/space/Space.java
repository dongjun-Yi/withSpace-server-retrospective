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


    public void makeRelation(Schedule schedule) {
        this.schedule = schedule;
    }


    //양방향 연관관계..
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "team_id")
//    private Team team;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;


}
