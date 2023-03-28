package hansung.cse.withSpace.exception.member.join;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//가입시 필수정보가 없는 예외
public class RequiredValueMissingException extends RuntimeException {

    public RequiredValueMissingException(String message) {
        super(message);
    }
}
