package hansung.cse.withSpace.exception.handler;

import hansung.cse.withSpace.exception.category.CategoryActiveException;
import hansung.cse.withSpace.exception.category.CategoryNotFoundException;
import hansung.cse.withSpace.responsedto.ErrorBasicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CategoryExceptionHandler {

    @ExceptionHandler({CategoryNotFoundException.class})
    public ResponseEntity<ErrorBasicResponse> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        ErrorBasicResponse errorResponse = new ErrorBasicResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CategoryActiveException.class})
    public ResponseEntity<ErrorBasicResponse> handleCategoryActiveException(CategoryActiveException ex) {
        ErrorBasicResponse errorResponse = new ErrorBasicResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
