package pl.HomeworkJustClick.Backend.infrastructure.exception.commentfiletext;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "CommentFileText not found")
public class CommentFileTextNotFoundException extends RuntimeException {
    public CommentFileTextNotFoundException(String message) {
        super(message);
    }
}
