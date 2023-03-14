package hansung.cse.withSpace.domain.space;


import hansung.cse.withSpace.domain.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamSpace extends Space {

    //@Id @GeneratedValue
    //private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    private Team team;

    public TeamSpace(Team team) {
        this.team = team;
    }
}