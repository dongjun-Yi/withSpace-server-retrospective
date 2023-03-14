package hansung.cse.withSpace.domain.space;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import hansung.cse.withSpace.domain.space.schedule.Schedule;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
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


    public Space() {
        //this.schedule = new Schedule(this); //스페이스 생성시 바로 만들어서 넣어줌
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
