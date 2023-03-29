package hansung.cse.withSpace.exception.handler;

import hansung.cse.withSpace.exception.block.BlockNotFoundException;
import hansung.cse.withSpace.responsedto.ErrorBasicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BlockExceptionHandler {
    @ExceptionHandler({BlockNotFoundException.class})
    public ResponseEntity<ErrorBasicResponse> handleBlockNotFoundException(BlockNotFoundException ex) {
        ErrorBasicResponse errorResponse = new ErrorBasicResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
