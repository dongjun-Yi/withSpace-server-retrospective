package hansung.cse.withSpace.exception.member.join;


// 이메일 중복 예외

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}