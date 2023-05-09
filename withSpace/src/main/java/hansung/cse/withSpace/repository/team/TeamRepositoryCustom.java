package hansung.cse.withSpace.repository.team;

import hansung.cse.withSpace.domain.Team;

import java.util.List;

public interface TeamRepositoryCustom {
    List<Team> searchTeamsByName(String query, int limit);
}