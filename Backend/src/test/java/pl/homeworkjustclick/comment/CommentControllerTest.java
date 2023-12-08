package pl.homeworkjustclick.comment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;
import pl.homeworkjustclick.assignment.AssignmentRepository;
import pl.homeworkjustclick.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@Sql(scripts = {
        "classpath:db/cleanup_all.sql",
        "classpath:db/init_user.sql",
        "classpath:db/init_group.sql",
        "classpath:db/init_group_student.sql",
        "classpath:db/init_group_teacher.sql",
        "classpath:db/init_assignment.sql",
        "classpath:db/init_solution.sql",
        "classpath:db/init_evaluation.sql",
        "classpath:db/init_comment.sql",
        "classpath:db/init_comment_evaluation.sql",
        "classpath:db/init_file.sql",
        "classpath:db/init_comment_file_img.sql",
        "classpath:db/init_comment_file_text.sql"
})
public class CommentControllerTest extends BaseTestEntity {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    UserRepository userRepository;

    private static Stream<Arguments> prepareValidData() {
        return Stream.of(
                Arguments.of("title", "desc", "#ffffff", 1, 1),
                Arguments.of("", "desc", "#ffffff", 1, 1),
                Arguments.of(null, "desc", "#ffffff", 1, 1),
                Arguments.of("A".repeat(255), "A".repeat(255), "A".repeat(10), 1, 1)
        );
    }

    private static Stream<Arguments> prepareInvalidData() {
        return Stream.of(
                Arguments.of("title", "", "#ffffff", 1, 1),
                Arguments.of("", "", "#ffffff", 1, 1),
                Arguments.of("", "", "", 1, 1),
                Arguments.of("title", "desc", "#ffffff", 999, 1),
                Arguments.of("title", "desc", "#ffffff", 1, 999),
                Arguments.of("A".repeat(256), "desc", "#ffffff", 1, 1),
                Arguments.of("title", "A".repeat(256), "#ffffff", 1, 1),
                Arguments.of("title", null, "#ffffff", 1, 1)
        );
    }

    @Test
    void shouldGetAllComments() throws Exception {
        mockMvc.perform(get("/api/comment").param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(12))
                .andReturn();
    }

    @Test
    void shouldGetCommentById() throws Exception {
        var comment = commentRepository.findAll().get(0);
        mockMvc.perform(get("/api/comment/" + comment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comment.getId()))
                .andExpect(jsonPath("$.title").value(comment.getTitle()))
                .andExpect(jsonPath("$.description").value(comment.getDescription()))
                .andExpect(jsonPath("$.color").value(comment.getColor()))
                .andExpect(jsonPath("$.counter").value(comment.getCounter()))
                .andReturn();
    }

    @Test
    void shouldGetAllCommentsByAssignment() throws Exception {
        var assignmentId = commentRepository.findAll().get(0).getAssignment().getId();
        var commentsSize = commentRepository.getCommentsByAssignmentIdAndVisible(assignmentId, true, Pageable.ofSize(20)).getTotalElements();
        mockMvc.perform(get("/api/comment/byAssignment/" + assignmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(commentsSize))
                .andReturn();
    }

    @Test
    void shouldGetAllCommentsByUser() throws Exception {
        var userId = commentRepository.findAll().get(0).getUser().getId();
        var commentsSize = commentRepository.getCommentsByUserIdAndVisible(userId, true, Pageable.ofSize(20)).getTotalElements();
        mockMvc.perform(get("/api/comment/byUser/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(commentsSize))
                .andReturn();
    }

    @Test
    void shouldGetAllCommentsByUserAndAssignment() throws Exception {
        var comment = commentRepository.findAll().get(0);
        var userId = comment.getUser().getId();
        var assignmentId = comment.getAssignment().getId();
        var commentsSize = commentRepository.getCommentsByUserIdAndAssignmentIdAndVisible(userId, assignmentId, true, Pageable.ofSize(20)).getTotalElements();
        mockMvc.perform(get("/api/comment/byUserAndAssignment/" + userId + "/" + assignmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(commentsSize))
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("prepareValidData")
    void shouldCreateComment(String title, String description, String color, Integer assignmentId, Integer userId) throws Exception {
        var commentDto = createCommentDto(title, description, color, assignmentId, userId);
        var body = objectMapper.writeValueAsString(commentDto);
        mockMvc.perform(post("/api/comment")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(commentDto.getTitle()))
                .andExpect(jsonPath("$.description").value(commentDto.getDescription()))
                .andExpect(jsonPath("$.color").value(commentDto.getColor()))
                .andExpect(jsonPath("$.counter").value(0))
                .andExpect(jsonPath("$.assignment.id").value(commentDto.getAssignmentId()))
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("prepareInvalidData")
    void shouldNotCreateComment(String title, String description, String color, Integer assignmentId, Integer userId) throws Exception {
        var commentDto = createCommentDto(title, description, color, assignmentId, userId);
        var body = objectMapper.writeValueAsString(commentDto);
        mockMvc.perform(post("/api/comment")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldCreateCommentList() throws Exception {
        var expectedSize = commentRepository.findAll().size() + 4;
        var commentsList = List.of(
                createCommentDto("title", "desc", "#ffffff", 1, 1),
                createCommentDto("", "desc", "#ffffff", 1, 1),
                createCommentDto(null, "desc", "#ffffff", 1, 1),
                createCommentDto("A".repeat(255), "A".repeat(255), "A".repeat(10), 1, 1)
        );
        var body = objectMapper.writeValueAsString(commentsList);
        mockMvc.perform(post("/api/comment/list")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andReturn();
        assertEquals(expectedSize, commentRepository.findAll().size());
    }

    @Test
    void shouldNotCreateCommentList() throws Exception {
        var expectedSize = commentRepository.findAll().size();
        var commentsList = List.of(
                createCommentDto("title", "", "#ffffff", 1, 1),
                createCommentDto("", "", "#ffffff", 1, 1),
                createCommentDto("", "", "", 1, 1),
                createCommentDto("title", "desc", "#ffffff", 999, 1),
                createCommentDto("title", "desc", "#ffffff", 1, 999),
                createCommentDto("A".repeat(256), "desc", "#ffffff", 1, 1),
                createCommentDto("title", "A".repeat(256), "#ffffff", 1, 1),
                createCommentDto("title", null, "#ffffff", 1, 1)
        );
        var body = objectMapper.writeValueAsString(commentsList);
        mockMvc.perform(post("/api/comment/list")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
        assertEquals(expectedSize, commentRepository.findAll().size());
    }

    @ParameterizedTest
    @MethodSource("prepareValidData")
    void shouldUpdateComment(String title, String description, String color, Integer assignmentId, Integer userId) throws Exception {
        var commentId = commentRepository.findAll().get(0).getId();
        var commentDto = createCommentDto(title, description, color, assignmentId, userId);
        var body = objectMapper.writeValueAsString(commentDto);
        mockMvc.perform(put("/api/comment/" + commentId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
        var updatedComment = commentRepository.findById(commentId).get();
        assertEquals(updatedComment.getTitle(), commentDto.getTitle());
        assertEquals(updatedComment.getDescription(), commentDto.getDescription());
        assertEquals(updatedComment.getColor(), commentDto.getColor());
        assertEquals(updatedComment.getAssignment().getId(), commentDto.getAssignmentId());
        assertEquals(updatedComment.getUser().getId(), commentDto.getUserId());
    }

    @ParameterizedTest
    @MethodSource("prepareInvalidData")
    void shouldNotUpdateComment(String title, String description, String color, Integer assignmentId, Integer userId) throws Exception {
        var commentId = commentRepository.findAll().get(0).getId();
        var commentDto = createCommentDto(title, description, color, assignmentId, userId);
        var body = objectMapper.writeValueAsString(commentDto);
        mockMvc.perform(put("/api/comment/" + commentId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
        var updatedComment = commentRepository.findById(commentId).get();
        assertNotEquals(updatedComment.getTitle(), commentDto.getTitle());
        assertNotEquals(updatedComment.getDescription(), commentDto.getDescription());
        assertNotEquals(updatedComment.getColor(), commentDto.getColor());
        assertNotEquals(updatedComment.getAssignment().getId(), commentDto.getAssignmentId());
        assertNotEquals(updatedComment.getUser().getId(), commentDto.getUserId());
    }

    @Test
    void shouldNotUpdateNotExistingComment() throws Exception {
        var commentDto = createCommentDto("title", "desc", "#ffffff", 1, 1);
        var body = objectMapper.writeValueAsString(commentDto);
        mockMvc.perform(put("/api/comment/" + 999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldDeleteComment() throws Exception {
        var commentId = commentRepository.findAll().get(0).getId();
        mockMvc.perform(delete("/api/comment/" + commentId))
                .andExpect(status().isOk())
                .andReturn();
        assertFalse(commentRepository.findById(commentId).get().getVisible());
    }

    @Test
    void shouldNotDeleteComment() throws Exception {
        mockMvc.perform(delete("/api/comment/999"))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    private CommentDto createCommentDto(String title, String description, String color, Integer assignmentId, Integer userId) {
        if (assignmentId == 1) {
            assignmentId = assignmentRepository.findAll().get(0).getId();
        }
        if (userId == 1) {
            userId = userRepository.findAll().get(0).getId();
        }
        return CommentDto.builder()
                .title(title)
                .description(description)
                .color(color)
                .assignmentId(assignmentId)
                .userId(userId)
                .build();
    }
}
