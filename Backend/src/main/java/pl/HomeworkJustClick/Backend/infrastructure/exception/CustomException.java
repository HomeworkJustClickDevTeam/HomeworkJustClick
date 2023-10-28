package pl.HomeworkJustClick.Backend.infrastructure.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import pl.HomeworkJustClick.Backend.infrastructure.exception.comment.CommentNotFoundException;
import pl.HomeworkJustClick.Backend.infrastructure.exception.commentevaluation.CommentEvaluationNotFoundException;
import pl.HomeworkJustClick.Backend.infrastructure.exception.commentfileimg.CommentFileImgNotFoundException;
import pl.HomeworkJustClick.Backend.infrastructure.exception.commentfiletext.CommentFileTextNotFoundException;
import pl.HomeworkJustClick.Backend.infrastructure.exception.evaluation.EvaluationNotFoundException;
import pl.HomeworkJustClick.Backend.infrastructure.exception.file.FileNotFoundException;
import pl.HomeworkJustClick.Backend.infrastructure.exception.user.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class CustomException {
    @ExceptionHandler(value = {CommentNotFoundException.class, CommentEvaluationNotFoundException.class,
            CommentFileImgNotFoundException.class, CommentFileTextNotFoundException.class,
            EvaluationNotFoundException.class, FileNotFoundException.class,
            UserNotFoundException.class})
    public ResponseEntity<Object> resourceNotFoundException(CommentNotFoundException ex, WebRequest request) {
        ex.printStackTrace();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
