package hansung.cse.withSpace.validation.member;

import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.repository.MemberRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UniqueMemberNameValidator implements ConstraintValidator<UniqueMemberName, Object> {
    private final MemberRepository memberRepository;
    @Override
    public void initialize(UniqueMemberName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String memberName = (String) value;
        Optional<Member> member = memberRepository.findByMemberName(memberName);
        return member.isEmpty();
    }
}
