package pl.HomeworkJustClick.Backend.evaluationreport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.HomeworkJustClick.Backend.BaseTestEntity;
import pl.HomeworkJustClick.Backend.evaluation.EvaluationRepository;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        "classpath:db/init_evaluation_report.sql"
})
public class EvaluationReportTest extends BaseTestEntity {
    @Autowired
    EvaluationReportRepository repository;
    @Autowired
    EvaluationRepository evaluationRepository;

    @Test
    void shouldCreateEvaluationReport() throws Exception {
        var evaluationId = evaluationRepository.findAll().get(0).getId();
        var evaluationReportDto = createEvaluationReportDto("Test comment", evaluationId);
        var body = objectMapper.writeValueAsString(evaluationReportDto);
        mockMvc.perform(post("/api/evaluation_report")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comment").value("Test comment"))
                .andExpect(jsonPath("$.evaluation.id").value(evaluationId))
                .andReturn();
    }

    @Test
    void shouldCreateEvaluationReportWithEmptyComment() throws Exception {
        var evaluationId = evaluationRepository.findAll().get(0).getId();
        var evaluationReportDto = createEvaluationReportDto("", evaluationId);
        var body = objectMapper.writeValueAsString(evaluationReportDto);
        mockMvc.perform(post("/api/evaluation_report")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comment").value(""))
                .andExpect(jsonPath("$.evaluation.id").value(evaluationId))
                .andReturn();
    }

    @Test
    void shouldNotCreateEvaluationReportWithDuplicatedEvaluationId() throws Exception {
        var duplicatedId = repository.findAll().get(0).getEvaluation().getId();
        var evaluationReportDto = createEvaluationReportDto("", duplicatedId);
        var body = objectMapper.writeValueAsString(evaluationReportDto);
        mockMvc.perform(post("/api/evaluation_report")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void shouldNotCreateEvaluationReportWithNotExistingEvaluationId() throws Exception {
        var evaluationReportDto = createEvaluationReportDto("", 9999);
        var body = objectMapper.writeValueAsString(evaluationReportDto);
        mockMvc.perform(post("/api/evaluation_report")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldUpdateEvaluationReport() throws Exception {
        var evaluationReportId = repository.findAll().get(0).getId();
        var evaluationId = evaluationRepository.findAll().get(0).getId();
        var evaluationReportDto = createEvaluationReportDto("Test comment", evaluationId);
        var body = objectMapper.writeValueAsString(evaluationReportDto);
        mockMvc.perform(put("/api/evaluation_report/" + evaluationReportId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Test comment"))
                .andExpect(jsonPath("$.evaluation.id").value(evaluationId))
                .andReturn();
    }

    @Test
    void shouldUpdateEvaluationReportWithEmptyComment() throws Exception {
        var evaluationReportId = repository.findAll().get(0).getId();
        var evaluationId = evaluationRepository.findAll().get(0).getId();
        var evaluationReportDto = createEvaluationReportDto("", evaluationId);
        var body = objectMapper.writeValueAsString(evaluationReportDto);
        mockMvc.perform(put("/api/evaluation_report/" + evaluationReportId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value(""))
                .andExpect(jsonPath("$.evaluation.id").value(evaluationId))
                .andReturn();
    }

    @Test
    void shouldNotUpdateEvaluationReportWithDuplicatedEvaluationId() throws Exception {
        var evaluationReportId = repository.findAll().get(0).getId();
        var duplicatedId = repository.findAll().get(1).getEvaluation().getId();
        var evaluationReportDto = createEvaluationReportDto("", duplicatedId);
        var body = objectMapper.writeValueAsString(evaluationReportDto);
        mockMvc.perform(put("/api/evaluation_report/" + evaluationReportId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void shouldNotUpdateEvaluationReportWithNotExistingEvaluationId() throws Exception {
        var evaluationReportId = repository.findAll().get(0).getId();
        var evaluationReportDto = createEvaluationReportDto("", 9999);
        var body = objectMapper.writeValueAsString(evaluationReportDto);
        mockMvc.perform(put("/api/evaluation_report/" + evaluationReportId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldDeleteEvaluationReport() throws Exception {
        var expectedSize = repository.findAll().size() - 1;
        var evaluationReportId = repository.findAll().get(0).getId();
        mockMvc.perform(delete("/api/evaluation_report/" + evaluationReportId))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedSize, repository.findAll().size());
    }


    @Test
    void shouldNotDeleteNotExistingEvaluationReport() throws Exception {
        var expectedSize = repository.findAll().size();
        mockMvc.perform(delete("/api/evaluation_report/" + 9999))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(expectedSize, repository.findAll().size());
    }

    private EvaluationReportDto createEvaluationReportDto(String comment, Integer evaluationId) {
        return EvaluationReportDto.builder()
                .comment(comment)
                .evaluationId(evaluationId)
                .build();
    }
}
