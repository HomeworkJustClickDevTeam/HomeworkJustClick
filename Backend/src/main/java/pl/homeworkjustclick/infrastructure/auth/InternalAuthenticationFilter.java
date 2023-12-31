package pl.homeworkjustclick.infrastructure.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.homeworkjustclick.assignment.AssignmentService;
import pl.homeworkjustclick.comment.CommentDto;
import pl.homeworkjustclick.comment.CommentService;
import pl.homeworkjustclick.commentfileimg.CommentFileImgDto;
import pl.homeworkjustclick.commentfileimg.CommentFileImgService;
import pl.homeworkjustclick.commentfiletext.CommentFileTextDto;
import pl.homeworkjustclick.commentfiletext.CommentFileTextService;
import pl.homeworkjustclick.evaluation.EvaluationDto;
import pl.homeworkjustclick.evaluation.EvaluationService;
import pl.homeworkjustclick.evaluationreport.EvaluationReportDto;
import pl.homeworkjustclick.evaluationreport.EvaluationReportService;
import pl.homeworkjustclick.groupstudent.GroupStudentService;
import pl.homeworkjustclick.groupteacher.GroupTeacherService;
import pl.homeworkjustclick.infrastructure.exception.InvalidUserException;
import pl.homeworkjustclick.report.AssignmentReportDto;
import pl.homeworkjustclick.report.GroupReportDto;
import pl.homeworkjustclick.solution.SolutionService;
import pl.homeworkjustclick.user.UserService;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class InternalAuthenticationFilter {
    private final UserService userService;
    private final GroupTeacherService groupTeacherService;
    private final GroupStudentService groupStudentService;
    private final AssignmentService assignmentService;
    private final SolutionService solutionService;
    private final EvaluationService evaluationService;
    private final CommentService commentService;
    private final CommentFileImgService commentFileImgService;
    private final CommentFileTextService commentFileTextService;
    private final EvaluationReportService evaluationReportService;
    private final ObjectMapper objectMapper;

    private static final String HTTP_METHOD_POST = "POST";
    private static final String HTTP_METHOD_DELETE = "DELETE";
    private static final String HTTP_METHOD_PUT = "PUT";

    public void filterInternal(HttpServletRequest request, String userMail, String body) throws JsonProcessingException {
        var path = request.getServletPath();
        var pathSplit = path.split("/");
        var user = userService.findByEmail(userMail);
        if (pathSplit.length < 2) {
            return;
        }
        if (pathSplit[2].equals("assignment")) {
            filterAssignment(request, user.getId(), path);
        }
        if (pathSplit[2].equals("solution")) {
            filterSolution(request, user.getId(), path);
        }
        if (pathSplit[2].equals("evaluation")) {
            filterEvaluation(request, user.getId(), body, path);
        }
        if (pathSplit[2].equals("comment")) {
            filterComment(request, user.getId(), body, path);
        }
        if (pathSplit[2].equals("comment_file_img")) {
            filterCommentFileImg(request, user.getId(), body, path);
        }
        if (pathSplit[2].equals("comment_file_text")) {
            filterCommentFileText(request, user.getId(), body, path);
        }
        if (pathSplit[2].equals("evaluation_report")) {
            filterEvaluationReport(request, user.getId(), body, path);
        }
        if (pathSplit[2].equals("report")) {
            filterReport(user.getId(), body, path);
        }
        if (pathSplit[2].equals("group")) {
            filterGroup(request, user.getId(), path);
        }
    }

    private void filterGroup(HttpServletRequest request, int userId, String path) {
        var pathSplit = path.split("/");
        if (request.getMethod().equals(HTTP_METHOD_POST) && pathSplit[2].equals("addTeacher")) {
            var groupId = pathSplit[pathSplit.length - 1];
            checkForTeacherInGroup(userId, Integer.parseInt(groupId));
        }
    }

    private void filterReport(int userId, String body, String path) throws JsonProcessingException {
        var pathSplit = path.split("/");
        if (pathSplit[pathSplit.length - 1].equals("assignment_csv")) {
            var report = objectMapper.readValue(body, AssignmentReportDto.class);
            var assignment = assignmentService.findById(report.getAssignmentId());
            var groupId = assignment.getGroup().getId();
            checkForTeacherInGroup(userId, groupId);
        }
        if (pathSplit[pathSplit.length - 1].equals("group_csv")) {
            var report = objectMapper.readValue(body, GroupReportDto.class);
            checkForTeacherInGroup(userId, report.getGroupId());
        }
    }

    private void filterEvaluationReport(HttpServletRequest request, int userId, String body, String path) throws JsonProcessingException {
        if (request.getMethod().equals(HTTP_METHOD_POST)) {
            filterStudentInEvaluationReportPost(userId, body);
        }
        if (request.getMethod().equals(HTTP_METHOD_DELETE) || request.getMethod().equals(HTTP_METHOD_PUT)) {
            filterFilterStudentInEvaluationReportDeleteOrPut(path, userId);
        }
    }

    private void filterFilterStudentInEvaluationReportDeleteOrPut(String path, int userId) {
        var pathSplit = path.split("/");
        var evaluationReport = evaluationReportService.findById(Integer.parseInt(pathSplit[pathSplit.length - 1]));
        var evaluation = evaluationService.findById(evaluationReport.getEvaluation().getId());
        var groupId = evaluation.getGroup().getId();
        checkForStudentInGroup(userId, groupId);
    }

    private void filterStudentInEvaluationReportPost(int userId, String body) throws JsonProcessingException {
        var evaluationReport = objectMapper.readValue(body, EvaluationReportDto.class);
        var evaluation = evaluationService.findById(evaluationReport.getEvaluationId());
        var groupId = evaluation.getGroup().getId();
        checkForStudentInGroup(userId, groupId);
    }

    private void filterCommentFileText(HttpServletRequest request, int userId, String body, String path) throws JsonProcessingException {
        if (request.getMethod().equals(HTTP_METHOD_POST)) {
            filterTeacherInCommentFileTextPost(userId, body);
        }
        if (request.getMethod().equals(HTTP_METHOD_DELETE) || request.getMethod().equals(HTTP_METHOD_PUT)) {
            filterTeacherInCommentFileTextDeleteOrPut(path, userId);
        }
    }

    private void filterTeacherInCommentFileTextDeleteOrPut(String path, int userId) {
        var pathSplit = path.split("/");
        if (pathSplit.length == 4) {
            var commentFileText = commentFileTextService.findById(Integer.parseInt(pathSplit[pathSplit.length - 1]));
            var comment = commentService.findById(commentFileText.getComment().getId());
            var assignmentId = comment.getAssignment().getId();
            var groupId = assignmentService.findById(assignmentId).getGroup().getId();
            checkForTeacherInGroup(userId, groupId);
        }
        if (pathSplit.length == 5 || pathSplit.length == 6) {
            var comment = commentService.findById(Integer.parseInt(pathSplit[4]));
            var assignmentId = comment.getAssignment().getId();
            var groupId = assignmentService.findById(assignmentId).getGroup().getId();
            checkForTeacherInGroup(userId, groupId);
        }
    }

    private void filterTeacherInCommentFileTextPost(int userId, String body) throws JsonProcessingException {
        var commentFileText = objectMapper.readValue(body, CommentFileTextDto.class);
        var comment = commentService.findById(commentFileText.getCommentId());
        var assignmentId = comment.getAssignment().getId();
        var groupId = assignmentService.findById(assignmentId).getGroup().getId();
        checkForTeacherInGroup(userId, groupId);
    }

    private void filterCommentFileImg(HttpServletRequest request, int userId, String body, String path) throws JsonProcessingException {
        if (request.getMethod().equals(HTTP_METHOD_POST)) {
            filterTeacherInCommentFileImgPost(userId, body);
        }
        if (request.getMethod().equals(HTTP_METHOD_DELETE) || request.getMethod().equals(HTTP_METHOD_PUT)) {
            filterTeacherInCommentFileImgDeleteOrPut(path, userId);
        }
    }

    private void filterTeacherInCommentFileImgDeleteOrPut(String path, int userId) {
        var pathSplit = path.split("/");
        if (pathSplit.length == 4) {
            var commentFileImg = commentFileImgService.findById(Integer.parseInt(pathSplit[pathSplit.length - 1]));
            var comment = commentService.findById(commentFileImg.getComment().getId());
            var assignmentId = comment.getAssignment().getId();
            var groupId = assignmentService.findById(assignmentId).getGroup().getId();
            checkForTeacherInGroup(userId, groupId);
        }
        if (pathSplit.length == 5 || pathSplit.length == 6) {
            var comment = commentService.findById(Integer.parseInt(pathSplit[4]));
            var assignmentId = comment.getAssignment().getId();
            var groupId = assignmentService.findById(assignmentId).getGroup().getId();
            checkForTeacherInGroup(userId, groupId);
        }
    }

    private void filterTeacherInCommentFileImgPost(int userId, String body) throws JsonProcessingException {
        var commentFileImg = objectMapper.readValue(body, CommentFileImgDto.class);
        var comment = commentService.findById(commentFileImg.getCommentId());
        var assignmentId = comment.getAssignment().getId();
        var groupId = assignmentService.findById(assignmentId).getGroup().getId();
        checkForTeacherInGroup(userId, groupId);
    }

    private void filterComment(HttpServletRequest request, int userId, String body, String path) throws JsonProcessingException {
        if (request.getMethod().equals(HTTP_METHOD_POST)) {
            filterTeacherInCommentPost(path, userId, body);
        }
        if (request.getMethod().equals(HTTP_METHOD_DELETE) || request.getMethod().equals(HTTP_METHOD_PUT)) {
            filterTeacherInCommentDeleteOrPut(path, userId);
        }
    }

    private void filterTeacherInCommentDeleteOrPut(String path, int userId) {
        var pathSplit = path.split("/");
        var commentId = Integer.parseInt(pathSplit[pathSplit.length - 1]);
        var comment = commentService.findById(commentId);
        var assignmentId = comment.getAssignment().getId();
        var groupId = assignmentService.findById(assignmentId).getGroup().getId();
        checkForTeacherInGroup(userId, groupId);
    }

    private void filterTeacherInCommentPost(String path, int userId, String body) throws JsonProcessingException {
        if (path.split("/").length == 3) {
            var comment = objectMapper.readValue(body, CommentDto.class);
            var assignmentId = comment.getAssignmentId();
            var assignment = assignmentService.findById(assignmentId);
            var groupId = assignment.getGroup().getId();
            checkForTeacherInGroup(userId, groupId);
        }
        if (path.split("/").length == 4) {
            var comments = objectMapper.readValue(body, CommentDto[].class);
            Arrays.stream(comments).toList().forEach(comment -> {
                var assignmentId = comment.getAssignmentId();
                var assignment = assignmentService.findById(assignmentId);
                var groupId = assignment.getGroup().getId();
                checkForTeacherInGroup(userId, groupId);
            });
        }
    }

    private void filterEvaluation(HttpServletRequest request, int userId, String body, String path) throws JsonProcessingException {
        if (request.getMethod().equals(HTTP_METHOD_POST)) {
            filterTeacherInEvaluationPost(userId, body);
        }
        if (request.getMethod().equals(HTTP_METHOD_DELETE) || request.getMethod().equals(HTTP_METHOD_PUT)) {
            filterTeacherInEvaluationDeleteOrPut(path, userId);
        }
    }

    private void filterTeacherInEvaluationDeleteOrPut(String path, int userId) {
        var pathSplit = path.split("/");
        var evaluationId = Integer.parseInt(pathSplit[pathSplit.length - 1]);
        var evaluation = evaluationService.findById(evaluationId);
        var groupId = evaluation.getGroup().getId();
        checkForTeacherInGroup(userId, groupId);
    }

    private void filterTeacherInEvaluationPost(int userId, String body) throws JsonProcessingException {
        var evaluation = objectMapper.readValue(body, EvaluationDto.class);
        var groupId = evaluation.getGroupId();
        checkForTeacherInGroup(userId, groupId);
    }

    private void filterSolution(HttpServletRequest request, Integer userId, String path) {
        if (request.getMethod().equals(HTTP_METHOD_POST)) {
            filterStudentInSolutionPost(path, userId);
        }
        if (request.getMethod().equals(HTTP_METHOD_DELETE) || request.getMethod().equals(HTTP_METHOD_PUT)) {
            filterStudentInSolutionDeleteOrPut(path, userId);
        }
    }

    private void filterStudentInSolutionDeleteOrPut(String path, Integer userId) {
        var pathSplit = path.split("/");
        var solutionId = Integer.parseInt(pathSplit[pathSplit.length - 1]);
        var solution = solutionService.findById(solutionId);
        var groupId = solution.getGroup().getId();
        checkForStudentInGroup(userId, groupId);
    }

    private void filterStudentInSolutionPost(String path, Integer userId) {
        var pathSplit = path.split("/");
        var assignmentId = Integer.parseInt(pathSplit[pathSplit.length - 1]);
        var assignment = assignmentService.findById(assignmentId);
        var groupId = assignment.getGroup().getId();
        checkForStudentInGroup(userId, groupId);
    }

    private void filterAssignment(HttpServletRequest request, Integer userId, String path) {
        if (request.getMethod().equals(HTTP_METHOD_POST)) {
            filterTeacherInAssignmentPost(path, userId);
        }
        if (request.getMethod().equals(HTTP_METHOD_DELETE) || request.getMethod().equals(HTTP_METHOD_PUT)) {
            filterTeacherInAssignmentDeleteOrPut(path, userId);
        }
    }

    private void filterTeacherInAssignmentPost(String path, Integer userId) {
        var pathSplit = path.split("/");
        var groupId = Integer.parseInt(pathSplit[pathSplit.length - 1]);
        checkForTeacherInGroup(userId, groupId);
    }

    private void filterTeacherInAssignmentDeleteOrPut(String path, Integer userId) {
        var pathSplit = path.split("/");
        var assigmentId = Integer.parseInt(pathSplit[pathSplit.length - 1]);
        var assignment = assignmentService.findById(assigmentId);
        var groupId = assignment.getGroup().getId();
        checkForTeacherInGroup(userId, groupId);
    }

    private void checkForTeacherInGroup(Integer userId, Integer groupId) {
        if (groupTeacherService.checkForTeacherInGroup(userId, groupId).equals(Boolean.FALSE)) {
            throw new InvalidUserException("User is not a teacher in group");
        }
    }

    private void checkForStudentInGroup(Integer userId, Integer groupId) {
        if (groupStudentService.checkForStudentInGroup(userId, groupId).equals(Boolean.FALSE)) {
            throw new InvalidUserException("User is not a student in group");
        }
    }
}
