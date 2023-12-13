package pl.homeworkjustclick.infrastructure.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.homeworkjustclick.comment.CommentDto;
import pl.homeworkjustclick.evaluation.EvaluationDto;
import pl.homeworkjustclick.infrastructure.exception.InvalidUserException;
import pl.homeworkjustclick.user.UserService;

@Component
@RequiredArgsConstructor
public class UserRequestValidator {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public void validateUserInRequest(HttpServletRequest request, String userMail, String body) throws JsonProcessingException {
        var user = userService.findByEmail(userMail);
        if (!request.getMethod().equals("POST") && !request.getMethod().equals("PUT")) {
            return;
        }
        var pathSplit = request.getServletPath().split("/");
        if (pathSplit.length > 4 && pathSplit[2].equals("assignment") && pathSplit[3].equals("withUserAndGroup")) {
            if (Integer.parseInt(pathSplit[4]) != user.getId()) {
                throwException();
            }
        }
        if (pathSplit.length > 4 && pathSplit[2].equals("solution") && pathSplit[3].equals("withUserAndAssignment")) {
            if (Integer.parseInt(pathSplit[4]) != user.getId()) {
                throwException();
            }
        }
        if (pathSplit[2].equals("evaluation")) {
            var evaluationDto = objectMapper.readValue(body, EvaluationDto.class);
            if (evaluationDto.getUserId() != user.getId()) {
                throwException();
            }
        }
        if (pathSplit[2].equals("comment")) {
            var commentDto = objectMapper.readValue(body, CommentDto.class);
            if (commentDto.getUserId() != user.getId()) {
                throwException();
            }
        }
        if (pathSplit.length > 4 && pathSplit[2].equals("group") && pathSplit[3].equals("withTeacher")) {
            if (Integer.parseInt(pathSplit[4]) != user.getId()) {
                throwException();
            }
        }
    }

    private void throwException() {
        throw new InvalidUserException("Given user id invalid");
    }
}
