package pl.homeworkjustclick.evaluationpanelassignment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;
import pl.homeworkjustclick.assignment.Assignment;
import pl.homeworkjustclick.assignment.AssignmentRepository;
import pl.homeworkjustclick.evaluationpanel.EvaluationPanelRepository;
import pl.homeworkjustclick.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@Sql(scripts = {
        "classpath:db/cleanup_all.sql",
        "classpath:db/init_user.sql",
        "classpath:db/init_group.sql",
        "classpath:db/init_group_student.sql",
        "classpath:db/init_group_teacher.sql",
        "classpath:db/init_assignment.sql",
        "classpath:db/init_evaluation_panel.sql",
        "classpath:db/init_evaluation_panel_assignment.sql"
})
class EvaluationPanelAssignmentControllerTest extends BaseTestEntity {
    @Autowired
    EvaluationPanelAssignmentRepository evaluationPanelAssignmentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EvaluationPanelRepository evaluationPanelRepository;
    @Autowired
    AssignmentRepository assignmentRepository;

    private static Stream<Arguments> prepareInvalidData() {
        return Stream.of(
                Arguments.of(null, 1),
                Arguments.of(1, null)
        );
    }

    @Test
    void shouldGetEvaluationPanelAssignmentByUserAndAssignment() throws Exception {
        var userId = userRepository.findByEmail("jan_kowalski@gmail.com").get().getId();
        var assignments = assignmentRepository.findAll();
        var assignmentId = 1;
        for (Assignment a : assignments) {
            if (a.getUser().getId() == userId) {
                assignmentId = a.getId();
            }
        }
        mockMvc.perform(get("/api/evaluation_panel_assignment/" + userId + "/" + assignmentId))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldNotGetEvaluationPanelAssignmentByNotExistingUser() throws Exception {
        var assignmentId = assignmentRepository.findAll().get(0).getId();
        mockMvc.perform(get("/api/evaluation_panel_assignment/" + 999 + "/" + assignmentId))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldNotGetEvaluationPanelAssignmentByNotExistingAssignment() throws Exception {
        var userId = userRepository.findAll().get(0).getId();
        mockMvc.perform(get("/api/evaluation_panel_assignment/" + userId + "/" + 999))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldCreateEvaluationPanelAssignment() throws Exception {
        var evaluationPanelId = evaluationPanelRepository.findAll().get(0).getId();
        var assignmentId = assignmentRepository.findAll().get(0).getId();
        var dto = createDto(evaluationPanelId, assignmentId);
        var body = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/api/evaluation_panel_assignment")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("prepareInvalidData")
    void shouldNotCreateEvaluationPanelAssignmentWithNullValues(Integer evaluationPanelId, Integer assignmentId) throws Exception {
        var evaluationPanelAssignmentDto = createDto(evaluationPanelId, assignmentId);
        var body = objectMapper.writeValueAsString(evaluationPanelAssignmentDto);
        mockMvc.perform(post("/api/evaluation_panel_assignment")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void shouldNotCreateDuplicatedEvaluationPanelAssignment() throws Exception {
        var evaluationPanelAssignment = evaluationPanelAssignmentRepository.findAll().get(0);
        var evaluationPanelAssignmentDto = createDto(evaluationPanelAssignment.getEvaluationPanel().getId(), evaluationPanelAssignment.getAssignment().getId());
        var body = objectMapper.writeValueAsString(evaluationPanelAssignmentDto);
        mockMvc.perform(post("/api/evaluation_panel_assignment")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void shouldNotCreateEvaluationPanelAssignmentWithNotExistingEvaluationPanel() throws Exception {
        var evaluationPanelAssignment = evaluationPanelAssignmentRepository.findAll().get(0);
        var evaluationPanelAssignmentDto = createDto(999, evaluationPanelAssignment.getAssignment().getId());
        var body = objectMapper.writeValueAsString(evaluationPanelAssignmentDto);
        mockMvc.perform(post("/api/evaluation_panel_assignment")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldNotCreateEvaluationPanelAssignmentWithNotExistingAssignment() throws Exception {
        var evaluationPanelAssignment = evaluationPanelAssignmentRepository.findAll().get(0);
        var evaluationPanelAssignmentDto = createDto(evaluationPanelAssignment.getEvaluationPanel().getId(), 999);
        var body = objectMapper.writeValueAsString(evaluationPanelAssignmentDto);
        mockMvc.perform(post("/api/evaluation_panel_assignment")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldDeleteEvaluationPanelAssignment() throws Exception {
        var evaluationPanelAssignmentId = evaluationPanelAssignmentRepository.findAll().get(0).getId();
        mockMvc.perform(delete("/api/evaluation_panel_assignment/" + evaluationPanelAssignmentId))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(evaluationPanelAssignmentRepository.findById(evaluationPanelAssignmentId).isEmpty());
    }

    @Test
    void shouldNotDeleteEvaluationPanelAssignment() throws Exception {
        mockMvc.perform(delete("/api/evaluation_panel_assignment/" + 999))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldUpdateEvaluationPanelAssignment() throws Exception {
        var evaluationPanelAssignmentId = evaluationPanelAssignmentRepository.findAll().get(0).getId();
        var evaluationPanelId = evaluationPanelRepository.findAll().get(0).getId();
        var assignmentId = assignmentRepository.findAll().get(0).getId();
        var dto = createDto(evaluationPanelId, assignmentId);
        var body = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put("/api/evaluation_panel_assignment/" + evaluationPanelAssignmentId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("prepareInvalidData")
    void shouldNotUpdateEvaluationPanelAssignmentWithNullValues(Integer evaluationPanelId, Integer assignmentId) throws Exception {
        var evaluationPanelAssignmentId = evaluationPanelAssignmentRepository.findAll().get(0).getId();
        var evaluationPanelAssignmentDto = createDto(evaluationPanelId, assignmentId);
        var body = objectMapper.writeValueAsString(evaluationPanelAssignmentDto);
        mockMvc.perform(put("/api/evaluation_panel_assignment/" + evaluationPanelAssignmentId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void shouldNotUpdateDuplicatedEvaluationPanelAssignment() throws Exception {
        var evaluationPanelAssignment = evaluationPanelAssignmentRepository.findAll().get(0);
        var evaluationPanelAssignmentId = evaluationPanelAssignmentRepository.findAll().get(1).getId();
        var evaluationPanelAssignmentDto = createDto(evaluationPanelAssignment.getEvaluationPanel().getId(), evaluationPanelAssignment.getAssignment().getId());
        var body = objectMapper.writeValueAsString(evaluationPanelAssignmentDto);
        mockMvc.perform(put("/api/evaluation_panel_assignment/" + evaluationPanelAssignmentId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void shouldNotUpdateEvaluationPanelAssignmentWithNotExistingEvaluationPanel() throws Exception {
        var evaluationPanelAssignmentId = evaluationPanelAssignmentRepository.findAll().get(0).getId();
        var evaluationPanelAssignment = evaluationPanelAssignmentRepository.findAll().get(0);
        var evaluationPanelAssignmentDto = createDto(999, evaluationPanelAssignment.getAssignment().getId());
        var body = objectMapper.writeValueAsString(evaluationPanelAssignmentDto);
        mockMvc.perform(put("/api/evaluation_panel_assignment/" + evaluationPanelAssignmentId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldNotUpdateEvaluationPanelAssignmentWithNotExistingAssignment() throws Exception {
        var evaluationPanelAssignmentId = evaluationPanelAssignmentRepository.findAll().get(0).getId();
        var evaluationPanelAssignment = evaluationPanelAssignmentRepository.findAll().get(0);
        var evaluationPanelAssignmentDto = createDto(evaluationPanelAssignment.getEvaluationPanel().getId(), 999);
        var body = objectMapper.writeValueAsString(evaluationPanelAssignmentDto);
        mockMvc.perform(put("/api/evaluation_panel_assignment/" + evaluationPanelAssignmentId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private EvaluationPanelAssignmentDto createDto(Integer evaluationPanelId, Integer assignmentId) {
        var evaluationPanelAssignment = new EvaluationPanelAssignmentDto();
        evaluationPanelAssignment.setEvaluationPanelId(evaluationPanelId);
        evaluationPanelAssignment.setAssignmentId(assignmentId);
        return evaluationPanelAssignment;
    }
}
