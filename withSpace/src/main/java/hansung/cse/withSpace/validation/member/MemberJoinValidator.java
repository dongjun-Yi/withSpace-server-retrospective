//package hansung.cse.withSpace.validation.member;
//
//import hansung.cse.withSpace.exception.member.join.DuplicateEmailException;
//import hansung.cse.withSpace.exception.member.join.InvalidPasswordException;
//import hansung.cse.withSpace.exception.RequiredValueMissingException;
//import hansung.cse.withSpace.requestdto.member.MemberJoinRequestDto;
//import hansung.cse.withSpace.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.Errors;
//import org.springframework.validation.Validator;
//
//@RequiredArgsConstructor
//@Component //회원가입 Validator
//public class MemberJoinValidator implements Validator {
//    private final MemberService memberService;
//    private static final int MIN_PASSWORD_LENGTH = 6; //비밀번호 최소길이
//    @Override
//    public boolean supports(Class<?> clazz) {
//        // supports 메소드 - 어떤 클래스 타입이 현재의 Validator 구현체로 유효성 검사가 가능한지를 판단하는 역할
//        return MemberJoinRequestDto.class.isAssignableFrom(clazz);
//    }
//    @Override
//    public void validate(Object target, Errors errors) {
//        MemberJoinRequestDto requestDto = (MemberJoinRequestDto) target;
//
//        //필수 정보가 없는 경우
//        if (requestDto.getEmail() == null || requestDto.getEmail().trim().isEmpty() ||
//                requestDto.getMemberName() == null || requestDto.getMemberName().trim().isEmpty() ||
//                requestDto.getPassword() == null || requestDto.getPassword().trim().isEmpty()) {
//            throw new RequiredValueMissingException("필수 정보가 없습니다.");
//        }
//        // 비밀번호를 너무 짧게 입력한 경우
//        if (requestDto.getPassword().length() < MIN_PASSWORD_LENGTH) {
//            throw new InvalidPasswordException("비밀번호는 최소 " + MIN_PASSWORD_LENGTH + "자 이상이어야 합니다.");
//        }
//        // email 중복 검사
//        if (memberService.existsByEmail(requestDto.getEmail())) {
//            throw new DuplicateEmailException("이미 존재하는 email 입니다.");
//        }
//    }
//}