package hansung.cse.withSpace.exception.handler;


import hansung.cse.withSpace.exception.space.SpaceNotFoundException;
import hansung.cse.withSpace.responsedto.ErrorBasicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SpaceExceptionHandler {

    @ExceptionHandler({SpaceNotFoundException.class})
    public ResponseEntity<ErrorBasicResponse> handleSpaceNotFoundException(SpaceNotFoundException ex) {
        ErrorBasicResponse errorResponse = new ErrorBasicResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
