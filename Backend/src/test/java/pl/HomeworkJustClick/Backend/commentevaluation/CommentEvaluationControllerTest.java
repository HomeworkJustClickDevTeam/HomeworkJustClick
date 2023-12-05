package pl.HomeworkJustClick.Backend.commentevaluation;

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
import pl.HomeworkJustClick.Backend.comment.CommentRepository;
import pl.HomeworkJustClick.Backend.evaluation.EvaluationRepository;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

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
public class CommentEvaluationControllerTest extends BaseTestEntity {
    @Autowired
    CommentEvaluationRepository commentEvaluationRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    EvaluationRepository evaluationRepository;

    private static Stream<Arguments> prepareValidData() {
        return Stream.of(
                Arguments.of(1, 1, ""),
                Arguments.of(1, 1, null),
                Arguments.of(1, 1, "A".repeat(255))
        );
    }

    private static Stream<Arguments> prepareInvalidData() {
        return Stream.of(
                Arguments.of(1, 1, "A".repeat(256)),
                Arguments.of(9999, 1, "desc"),
                Arguments.of(1, 9999, "desc"),
                Arguments.of(9999, 9999, "desc")
        );
    }

    @Test
    void shouldGetAllCommentEvaluations() throws Exception {
        mockMvc.perform(get("/api/comment_evaluation").param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(6))
                .andReturn();
    }

    @Test
    void shouldGetCommentEvaluationById() throws Exception {
        var commentEvaluation = commentEvaluationRepository.findAll().get(0);
        mockMvc.perform(get("/api/comment_evaluation/" + commentEvaluation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentEvaluation.getId()))
                .andExpect(jsonPath("$.comment.id").value(commentEvaluation.getComment().getId()))
                .andExpect(jsonPath("$.evaluation.id").value(commentEvaluation.getEvaluation().getId()))
                .andExpect(jsonPath("$.description").value(commentEvaluation.getDescription()));
    }

    @Test
    void shouldGetCommentEvaluationByCommentId() throws Exception {
        var comment = commentRepository.findAll().get(0);
        var commentEvaluationsSize = commentEvaluationRepository.getCommentEvaluationsByCommentId(comment.getId(), Pageable.ofSize(20)).getTotalElements();
        mockMvc.perform(get("/api/comment_evaluation/byCommentId/" + comment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(commentEvaluationsSize))
                .andReturn();
    }

    @Test
    void shouldGetCommentEvaluationByEvaluationId() throws Exception {
        var evaluation = evaluationRepository.findAll().get(0);
        var commentEvaluationsSize = commentEvaluationRepository.getCommentEvaluationsByEvaluationId(evaluation.getId(), Pageable.ofSize(20)).getTotalElements();
        mockMvc.perform(get("/api/comment_evaluation/byEvaluationId/" + evaluation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(commentEvaluationsSize))
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("prepareValidData")
    void shouldCreateCommentEvaluation(Integer commentId, Integer evaluationId, String description) throws Exception {
        var commentEvaluationDto = createCommentEvaluationDto(commentId, evaluationId, description);
        var body = objectMapper.writeValueAsString(commentEvaluationDto);
        mockMvc.perform(post("/api/comment_evaluation")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comment.id").value(commentEvaluationDto.getCommentId()))
                .andExpect(jsonPath("$.evaluation.id").value(commentEvaluationDto.getEvaluationId()))
                .andExpect(jsonPath("$.description").value(commentEvaluationDto.getDescription()))
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("prepareInvalidData")
    void shouldNotCreateCommentEvaluation(Integer commentId, Integer evaluationId, String description) throws Exception {
        var commentEvaluationDto = createCommentEvaluationDto(commentId, evaluationId, description);
        var body = objectMapper.writeValueAsString(commentEvaluationDto);
        mockMvc.perform(post("/api/comment_evaluation")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldDeleteCommentEvaluation() throws Exception {
        var commentEvaluationId = commentEvaluationRepository.findAll().get(0).getId();
        mockMvc.perform(delete("/api/comment_evaluation/" + commentEvaluationId))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldNotDeleteCommentEvaluation() throws Exception {
        mockMvc.perform(delete("/api/comment_evaluation/" + 999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    private CommentEvaluationDto createCommentEvaluationDto(Integer commentId, Integer evaluationId, String description) {
        if (commentId == 1) {
            commentId = commentRepository.findAll().get(0).getId();
        }
        if (evaluationId == 1) {
            evaluationId = evaluationRepository.findAll().get(0).getId();
        }
        return CommentEvaluationDto.builder()
                .description(description)
                .commentId(commentId)
                .evaluationId(evaluationId)
                .build();
    }
}
