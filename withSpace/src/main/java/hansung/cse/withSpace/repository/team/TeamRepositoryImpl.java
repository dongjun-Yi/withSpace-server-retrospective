package hansung.cse.withSpace.repository.team;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import hansung.cse.withSpace.domain.QTeam;
import hansung.cse.withSpace.domain.Team;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class TeamRepositoryImpl extends QuerydslRepositorySupport implements TeamRepositoryCustom {

    public TeamRepositoryImpl() {
        super(Team.class);
    }

    @Override
    public List<Team> searchTeamsByName(String query, int limit) {
        QTeam team = QTeam.team;


        JPAQuery<Team> jpaQuery = new JPAQuery<>(getEntityManager());
        List<Team> results = jpaQuery.from(team)
                .orderBy(team.teamName.asc())
                .fetch();

        return results;
    }


}
