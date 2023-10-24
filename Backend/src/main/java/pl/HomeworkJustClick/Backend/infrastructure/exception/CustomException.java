package pl.HomeworkJustClick.Backend.infrastructure.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class CustomException {
//    @ExceptionHandler(value = {CommentNotFoundException.class})
//    public ResponseEntity<ErrorMessage> resourceNotFoundException(CommentNotFoundException ex, WebRequest request) {
//        ErrorMessage message = new ErrorMessage(
//                ex.getMessage());
//
//        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
//    }
}
