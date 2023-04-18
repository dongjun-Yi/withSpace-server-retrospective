package hansung.cse.withSpace.domain.space;


import hansung.cse.withSpace.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamSpace extends Space {

    //@Id @GeneratedValue
    //private Long id;

//    @Id
//    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    private Team team;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "space_id")
//    private Space space;



    public TeamSpace(Team team) {
        this.team = team;
    }
}