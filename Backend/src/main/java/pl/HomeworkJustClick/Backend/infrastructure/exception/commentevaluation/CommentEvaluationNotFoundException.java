package pl.HomeworkJustClick.Backend.infrastructure.exception.commentevaluation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "CommentEvaluation not found")
public class CommentEvaluationNotFoundException extends RuntimeException {
    public CommentEvaluationNotFoundException(String message) {
        super(message);
    }
}
