package hansung.cse.withSpace.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import hansung.cse.withSpace.domain.space.TeamSpace;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    @OneToMany(mappedBy = "team")
    private List<MemberTeam> memberTeams = new ArrayList<>();

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL)
    private TeamSpace teamSpace;

    private int memberCount;

    private String teamName;

    private Long host;

    //연관관계 편의 메소드//
//    public void setTeamSpace(TeamSpace teamSpace) {
//        this.teamSpace = teamSpace;
//        teamSpace.setTeam(this);
//    }

    public Team(String teamName, Long id) { //팀 생성
        this.host = id; //팀 만든사람이 호스트(MemberId)
        this.teamName = teamName;
        memberCount = 0;

        //바로 스페이스 생성+부여
        TeamSpace teamSpace = new TeamSpace(this);
        this.teamSpace = teamSpace;
        //spaceRepository.save(teamSpace);

    }

    public void joinTeam(Team team) {
        this.memberCount = getMemberCount() +1;
    }
}
