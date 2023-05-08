package hansung.cse.withSpace.validation.team;

import hansung.cse.withSpace.domain.Team;
import hansung.cse.withSpace.repository.team.TeamRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UniqueTeamNameValidator implements ConstraintValidator<UniqueTeamName, Object> {

    private final TeamRepository teamRepository;
    @Override
    public void initialize(UniqueTeamName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String teamName = (String) value;
        Optional<Team> team = teamRepository.findByTeamName(teamName);
        return team.isEmpty();
    }
}
