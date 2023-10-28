package pl.HomeworkJustClick.Backend.comment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.HomeworkJustClick.Backend.BaseTestEntity;
import pl.HomeworkJustClick.Backend.user.UserRepository;

import java.nio.charset.StandardCharsets;
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
        "classpath:db/init_file.sql",
        "classpath:db/init_comment_file_img.sql",
        "classpath:db/init_comment_file_text.sql"
})
public class CommentControllerTest extends BaseTestEntity {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    private static Stream<Arguments> prepareValidData() {
        return Stream.of(
                Arguments.of("title", "desc", 20, 1),
                Arguments.of("", "desc", 20, 1),
                Arguments.of(null, "desc", 20, 1),
                Arguments.of("A".repeat(255), "A".repeat(255), 20, 1)
        );
    }

    private static Stream<Arguments> prepareInvalidData() {
        return Stream.of(
                Arguments.of("title", "", 20, 1),
                Arguments.of("", "", 20, 1),
                Arguments.of("title", "desc", 20, 999),
                Arguments.of("A".repeat(256), "desc", 20, 1),
                Arguments.of("title", "A".repeat(256), 20, 1),
                Arguments.of("title", null, 20, 1)
        );
    }

    @Test
    void shouldFindAllComments() throws Exception {
        mockMvc.perform(get("/api/comment").param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(12))
                .andReturn();
    }

    @Test
    void shouldGetAllUserComments() throws Exception {
        var userId = commentRepository.findAll().get(0).getUser().getId();
        var commentsSize = commentRepository.getCommentsByUserId(userId, Pageable.ofSize(20)).getTotalElements();
        mockMvc.perform(get("/api/comment/byUser/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(commentsSize))
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("prepareValidData")
    void shouldCreateComment(String title, String description, Integer defaultColor, Integer userId) throws Exception {
        var commentDto = createCommentDto(title, description, defaultColor, userId);
        var body = objectMapper.writeValueAsString(commentDto);
        mockMvc.perform(post("/api/comment")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("prepareInvalidData")
    void shouldNotCreateComment(String title, String description, Integer defaultColor, Integer userId) throws Exception {
        var commentDto = createCommentDto(title, description, defaultColor, userId);
        var body = objectMapper.writeValueAsString(commentDto);
        mockMvc.perform(post("/api/comment")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("prepareValidData")
    void shouldUpdateComment(String title, String description, Integer defaultColor, Integer userId) throws Exception {
        var commentId = commentRepository.findAll().get(0).getId();
        var commentDto = createCommentDto(title, description, defaultColor, userId);
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
        assertEquals(updatedComment.getDefaultColor(), commentDto.getDefaultColor());
        assertEquals(updatedComment.getUser().getId(), commentDto.getUserId());
    }

    @ParameterizedTest
    @MethodSource("prepareInvalidData")
    void shouldNotUpdateComment(String title, String description, Integer defaultColor, Integer userId) throws Exception {
        var commentId = commentRepository.findAll().get(0).getId();
        var commentDto = createCommentDto(title, description, defaultColor, userId);
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
        assertNotEquals(updatedComment.getDefaultColor(), commentDto.getDefaultColor());
        assertNotEquals(updatedComment.getUser().getId(), commentDto.getUserId());
    }

    @Test
    void shouldDeleteComment() throws Exception {
        var commentId = commentRepository.findAll().get(0).getId();
        mockMvc.perform(delete("/api/comment/" + commentId))
                .andExpect(status().isOk())
                .andReturn();
        assertFalse(commentRepository.findById(commentId).isPresent());
    }

    @Test
    void shouldNotDeleteComment() throws Exception {
        mockMvc.perform(delete("/api/comment/999"))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    private CommentDto createCommentDto(String title, String description, Integer defaultColor, Integer userId) {
        if (userId == 1) {
            userId = userRepository.findAll().get(0).getId();
        }
        return CommentDto.builder()
                .title(title)
                .description(description)
                .defaultColor(defaultColor)
                .userId(userId)
                .build();
    }
}
