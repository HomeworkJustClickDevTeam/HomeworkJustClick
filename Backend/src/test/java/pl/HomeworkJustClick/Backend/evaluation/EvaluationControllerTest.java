package pl.HomeworkJustClick.Backend.evaluation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.HomeworkJustClick.Backend.BaseTestEntity;
import pl.HomeworkJustClick.Backend.group.GroupRepository;
import pl.HomeworkJustClick.Backend.groupteacher.GroupTeacherRepository;
import pl.HomeworkJustClick.Backend.solution.SolutionRepository;
import pl.HomeworkJustClick.Backend.solution.SolutionService;
import pl.HomeworkJustClick.Backend.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

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
public class EvaluationControllerTest extends BaseTestEntity {
    @Autowired
    EvaluationRepository evaluationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SolutionRepository solutionRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    SolutionService solutionService;
    @Autowired
    GroupTeacherRepository groupTeacherRepository;

    private static Stream<Arguments> prepareValidData() {
        return Stream.of(
                Arguments.of(1.0, 1.0, false, 1, 1, 1),
                Arguments.of(1.0, 1.0, null, 1, 1, 1)
        );
    }

    private static Stream<Arguments> prepareInvalidData() {
        return Stream.of(
                Arguments.of(null, 1.0, false, 1, 1, 1),
                Arguments.of(1.0, 1.0, false, 1, 9999, 1)
        );
    }

    @Test
    void shouldGetAllEvaluations() throws Exception {
        mockMvc.perform(get("/api/evaluations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(11))
                .andReturn();
    }

    @Test
    void shouldGetAllEvaluationsExtended() throws Exception {
        mockMvc.perform(get("/api/extended/evaluations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(11))
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("prepareValidData")
    void shouldCreateEvaluation(Double result, Double grade, Boolean reported, Integer userId, Integer solutionId, Integer groupId) throws Exception {
        var evaluationDto = createEvaluationDto(result, grade, reported, userId, solutionId, groupId);
        var body = objectMapper.writeValueAsString(evaluationDto);
        var expectedSize = evaluationRepository.findAll().size() + 1;
        mockMvc.perform(post("/api/evaluation")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andReturn();
        assertEquals(expectedSize, evaluationRepository.findAll().size());
    }

    @ParameterizedTest
    @MethodSource("prepareInvalidData")
    void shouldNotCreateEvaluation(Double result, Double grade, Boolean reported, Integer userId, Integer solutionId, Integer groupId) throws Exception {
        var evaluationDto = createEvaluationDto(result, grade, reported, userId, solutionId, groupId);
        var body = objectMapper.writeValueAsString(evaluationDto);
        var expectedSize = evaluationRepository.findAll().size();
        mockMvc.perform(post("/api/evaluation")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
        assertEquals(expectedSize, evaluationRepository.findAll().size());
    }

    @ParameterizedTest
    @MethodSource("prepareValidData")
    void shouldUpdateEvaluation(Double result, Double grade, Boolean reported, Integer userId, Integer solutionId, Integer groupId) throws Exception {
        var evaluationDto = createEvaluationDto(result, grade, reported, userId, solutionId, groupId);
        var body = objectMapper.writeValueAsString(evaluationDto);
        var expectedSize = evaluationRepository.findAll().size();
        var evaluationId = evaluationRepository.findAll().get(0).getId();
        mockMvc.perform(put("/api/evaluation/" + evaluationId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedSize, evaluationRepository.findAll().size());
    }

    @ParameterizedTest
    @MethodSource("prepareInvalidData")
    void shouldNotUpdateEvaluation(Double result, Double grade, Boolean reported, Integer userId, Integer solutionId, Integer groupId) throws Exception {
        var evaluationDto = createEvaluationDto(result, grade, reported, userId, solutionId, groupId);
        var body = objectMapper.writeValueAsString(evaluationDto);
        var expectedSize = evaluationRepository.findAll().size();
        var evaluationId = evaluationRepository.findAll().get(0).getId();
        mockMvc.perform(put("/api/evaluation/" + evaluationId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
        assertEquals(expectedSize, evaluationRepository.findAll().size());
    }

    @Test
    void shouldNotUpdateNotExistingEvaluation() throws Exception {
        var evaluationDto = createEvaluationDto(1.0, 1.0, false, 1, 1, 1);
        var body = objectMapper.writeValueAsString(evaluationDto);
        var evaluationId = evaluationRepository.findAll().get(0).getId();
        mockMvc.perform(put("/api/evaluation/" + 999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldDeleteEvaluation() throws Exception {
        var evaluationId = evaluationRepository.findAll().get(0).getId();
        var expectedSize = evaluationRepository.findAll().size() - 1;
        mockMvc.perform(delete("/api/evaluation/" + evaluationId))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedSize, evaluationRepository.findAll().size());
    }

    @Test
    void shouldNotDeleteNotExistingEvaluation() throws Exception {
        var expectedSize = evaluationRepository.findAll().size();
        mockMvc.perform(delete("/api/evaluation/" + 999))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(expectedSize, evaluationRepository.findAll().size());
    }

    @Test
    void shouldGetReportedEvaluationsByUserId() throws Exception {
        var userId = evaluationRepository.findAll().get(0).getUser().getId();
        mockMvc.perform(get("/api/evaluations/reportedByUserId/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andReturn();
    }

    @Test
    void shouldGetReportedEvaluationsByGroupId() throws Exception {
        var groupId = evaluationRepository.findAll().get(0).getGroup().getId();
        mockMvc.perform(get("/api/evaluations/reportedByGroupId/" + groupId))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldGetReportedEvaluationsByUserIdAndGroupId() throws Exception {
        var evaluation = evaluationRepository.findAll().get(0);
        var userId = evaluation.getUser().getId();
        var groupId = evaluation.getGroup().getId();
        mockMvc.perform(get("/api/evaluations/reportedByUserIdAndGroupId/" + userId + "/" + groupId))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(status().isOk())
                .andReturn();
    }

    private EvaluationDto createEvaluationDto(Double result, Double grade, Boolean reported, Integer userId, Integer solutionId, Integer groupId) {
        if (userId == 1) {
            userId = userRepository.findByEmail("anna_malinowska@gmail.com").get().getId();
        }
        if (groupId == 1) {
            groupId = groupRepository.getGroupsByTeacherId(userId).get(0).getId();
        }
        if (solutionId == 1) {
            solutionId = solutionRepository.getUncheckedSolutionsByGroup(groupId).get(0).getId();
        }
        return EvaluationDto.builder()
                .result(result)
                .grade(grade)
                .userId(userId)
                .solutionId(solutionId)
                .groupId(groupId)
                .build();
    }
}
