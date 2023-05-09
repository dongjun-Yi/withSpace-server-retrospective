package hansung.cse.withSpace.validation.team;

import hansung.cse.withSpace.validation.member.UniqueMemberNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueTeamNameValidator.class)
public @interface UniqueTeamName {
    String message() default "이미 존재하는 팀 이름입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
