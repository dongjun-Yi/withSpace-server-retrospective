package hansung.cse.withSpace.exception.handler;

import hansung.cse.withSpace.exception.RequiredValueMissingException;
import hansung.cse.withSpace.exception.chat.RoomNotFoundException;
import hansung.cse.withSpace.exception.todo.ToDoNotFoundException;
import hansung.cse.withSpace.responsedto.ErrorBasicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RoomExceptionHandler {
    @ExceptionHandler({ RoomNotFoundException.class})
    public ResponseEntity<ErrorBasicResponse> handleToDoNotFoundException(RoomNotFoundException ex) {
        ErrorBasicResponse errorResponse = new ErrorBasicResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(RequiredValueMissingException.class)
    public ResponseEntity<ErrorBasicResponse> handleRequiredValueMissingException(RequiredValueMissingException ex) {
        ErrorBasicResponse errorResponse = new ErrorBasicResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
