package pl.homeworkjustclick.infrastructure.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ExceptionHandler {
    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String PATH = "path";

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> resourceNotFoundException(EntityNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, 404);
        body.put(ERROR, "Not Found");
        body.put(MESSAGE, ex.getMessage());
        body.put(PATH, ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {InvalidArgumentException.class})
    public ResponseEntity<Object> invalidArgumentException(InvalidArgumentException ex, WebRequest request) {
        log.error(ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, 400);
        body.put(ERROR, "Bad request");
        body.put(MESSAGE, ex.getMessage());
        body.put(PATH, ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {InvalidUserException.class})
    public ResponseEntity<Object> invalidUserException(InvalidUserException ex, WebRequest request) {
        log.error(ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, 400);
        body.put(ERROR, "Bad request");
        body.put(MESSAGE, ex.getMessage());
        body.put(PATH, ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {InternalException.class})
    public ResponseEntity<Object> internalException(InternalException ex, WebRequest request) {
        log.error(ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, 500);
        body.put(ERROR, "Internal server error");
        body.put(MESSAGE, ex.getMessage());
        body.put(PATH, ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
