package hansung.cse.withSpace.exception.handler;

import hansung.cse.withSpace.exception.friend.FriendRequestException;
import hansung.cse.withSpace.exception.friend.NotFriendException;
import hansung.cse.withSpace.responsedto.ErrorBasicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FriendExceptionHandler {

    @ExceptionHandler(FriendRequestException.class)
    public ResponseEntity<ErrorBasicResponse> handleFriendAddException(FriendRequestException ex) {
        ErrorBasicResponse errorResponse = new ErrorBasicResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFriendException.class)
    public ResponseEntity<ErrorBasicResponse> handleNotFriendException(NotFriendException ex) {
        ErrorBasicResponse errorResponse = new ErrorBasicResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
