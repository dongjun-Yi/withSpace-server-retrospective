package hansung.cse.withSpace.exception.handler;

import hansung.cse.withSpace.exception.page.PageDeletionNotAllowedException;
import hansung.cse.withSpace.exception.page.PageNotFoundException;
import hansung.cse.withSpace.responsedto.ErrorBasicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PageExceptionHandler {
    @ExceptionHandler({PageNotFoundException.class})
    public ResponseEntity<ErrorBasicResponse> handlePageNotFoundException(PageNotFoundException ex) {
        ErrorBasicResponse errorResponse = new ErrorBasicResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({PageDeletionNotAllowedException.class})
    public ResponseEntity<ErrorBasicResponse> handlePageDeletionNotAllowedException(PageDeletionNotAllowedException ex) {
        ErrorBasicResponse errorResponse = new ErrorBasicResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
}
