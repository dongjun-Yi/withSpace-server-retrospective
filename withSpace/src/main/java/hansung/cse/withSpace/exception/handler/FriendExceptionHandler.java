package hansung.cse.withSpace.exception.handler;

import hansung.cse.withSpace.exception.friend.FriendAddException;
import hansung.cse.withSpace.exception.member.join.DuplicateEmailException;
import hansung.cse.withSpace.responsedto.ErrorBasicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FriendExceptionHandler {

    @ExceptionHandler(FriendAddException.class)
    public ResponseEntity<ErrorBasicResponse> handleFriendAddException(FriendAddException ex) {
        ErrorBasicResponse errorResponse = new ErrorBasicResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
