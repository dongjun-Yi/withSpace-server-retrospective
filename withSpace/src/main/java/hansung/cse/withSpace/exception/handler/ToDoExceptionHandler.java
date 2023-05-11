package hansung.cse.withSpace.exception.handler;

import hansung.cse.withSpace.exception.todo.ToDoActiveException;
import hansung.cse.withSpace.exception.todo.ToDoNotFoundException;
import hansung.cse.withSpace.responsedto.ErrorBasicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ToDoExceptionHandler {
    @ExceptionHandler({ ToDoNotFoundException.class})
    public ResponseEntity<ErrorBasicResponse> handleToDoNotFoundException(ToDoNotFoundException ex) {
        ErrorBasicResponse errorResponse = new ErrorBasicResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({ToDoActiveException.class})
    public ResponseEntity<ErrorBasicResponse> handleToDoActiveException(ToDoActiveException ex) {
        ErrorBasicResponse errorResponse = new ErrorBasicResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
