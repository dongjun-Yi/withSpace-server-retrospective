package hansung.cse.withSpace.exception.jwt;

import org.springframework.web.bind.annotation.RestControllerAdvice;


public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException  (String message) {
        super(message);
    }
}