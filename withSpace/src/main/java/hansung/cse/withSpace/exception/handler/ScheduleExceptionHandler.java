package hansung.cse.withSpace.exception.handler;

import hansung.cse.withSpace.exception.block.BlockNotFoundException;
import hansung.cse.withSpace.exception.schedule.ScheduleNotFoundException;
import hansung.cse.withSpace.responsedto.ErrorBasicResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ScheduleExceptionHandler {
    @ExceptionHandler({ScheduleNotFoundException.class})
    public ResponseEntity<ErrorBasicResponse> handleScheduleNotFoundException(ScheduleNotFoundException ex) {
        ErrorBasicResponse errorResponse = new ErrorBasicResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
