package pl.HomeworkJustClick.Backend.infrastructure.exception.commentfileimg;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "CommentFileImg not found")
public class CommentFileImgNotFoundException extends RuntimeException {
    public CommentFileImgNotFoundException(String message) {
        super(message);
    }
}
