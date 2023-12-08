package pl.homeworkjustclick.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "JwtToken not valid")
public class JwtNotValidException extends RuntimeException {
    public JwtNotValidException(String errorMessage) {
        super(errorMessage);
    }
}
