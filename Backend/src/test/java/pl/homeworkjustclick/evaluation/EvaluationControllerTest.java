package pl.homeworkjustclick.evaluation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;
import pl.homeworkjustclick.assignment.AssignmentRepository;
import pl.homeworkjustclick.evaluationreport.EvaluationReportRepository;
import pl.homeworkjustclick.group.GroupRepository;
import pl.homeworkjustclick.groupteacher.GroupTeacherRepository;
import pl.homeworkjustclick.solution.SolutionRepository;
import pl.homeworkjustclick.solution.SolutionService;
import pl.homeworkjustclick.user.UserRepository;

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
class EvaluationControllerTest extends BaseTestEntity {
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
    @Autowired
    AssignmentRepository assignmentRepository;
    @Autowired
    EvaluationReportRepository evaluationReportRepository;

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

    @Test
    void shouldGetEvaluationById() throws Exception {
        var evaluation = evaluationRepository.findAll().get(0);
        mockMvc.perform(get("/api/evaluations/{id}", evaluation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(evaluation.getId()))
                .andExpect(jsonPath("$.result").value(evaluation.getResult()))
                .andReturn();
    }

    @Test
    void shouldNotGetEvaluationById() throws Exception {
        mockMvc.perform(get("/api/evaluations/{id}", 9999))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetEvaluationExtendedById() throws Exception {
        var evaluation = evaluationRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/evaluations/{id}", evaluation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(evaluation.getId()))
                .andExpect(jsonPath("$.result").value(evaluation.getResult()))
                .andReturn();
    }

    @Test
    void shouldNotGetEvaluationExtendedById() throws Exception {
        mockMvc.perform(get("/api/extended/evaluations/{id}", 9999))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetEvaluationsByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        mockMvc.perform(get("/api/evaluations/byStudent/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetEvaluationsByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/evaluations/byStudent/{id}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetExtendedEvaluationsByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        mockMvc.perform(get("/api/extended/evaluations/byStudent/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetExtendedEvaluationsByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/extended/evaluations/byStudent/{id}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetEvaluationsByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(get("/api/evaluations/byAssignment/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetEvaluationsByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        evaluationReportRepository.deleteAll();
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/evaluations/byAssignment/{id}", assignment.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetExtendedEvaluationsByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/evaluations/byAssignment/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetExtendedEvaluationsByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        evaluationReportRepository.deleteAll();
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/extended/evaluations/byAssignment/{id}", assignment.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetEvaluationBySolution() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        mockMvc.perform(get("/api/evaluation/bySolution/{id}", solution.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solutionId").value(solution.getId()))
                .andReturn();
    }

    @Test
    void shouldNotGetEvaluationBySolution() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        evaluationReportRepository.deleteAll();
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/evaluation/bySolution/{id}", solution.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetExtendedEvaluationBySolution() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/evaluation/bySolution/{id}", solution.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.solution.id").value(solution.getId()))
                .andReturn();
    }

    @Test
    void shouldNotGetExtendedEvaluationBySolution() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        evaluationReportRepository.deleteAll();
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/extended/evaluation/bySolution/{id}", solution.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetEvaluationsByGroupId() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/evaluation/byGroupId/{id}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetEvaluationsByGroupId() throws Exception {
        var group = groupRepository.findAll().get(0);
        evaluationReportRepository.deleteAll();
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/evaluation/byGroupId/{id}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andReturn();
    }

    @Test
    void shouldCheckForEvaluationToSolutionAndReturnTrue() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        mockMvc.perform(get("/api/evaluation/checkForEvaluationToSolution/{id}", solution.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("true"))
                .andReturn();
    }

    @Test
    void shouldCheckForEvaluationToSolutionAndReturnFalse() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        evaluationReportRepository.deleteAll();
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/evaluation/checkForEvaluationToSolution/{id}", solution.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldGetEvaluationsByStudentAndGroup() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/evaluations/byStudentAndGroup/{studentId}/{groupId}", student.getId(), group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetEvaluationsByStudentAndGroup() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        evaluationReportRepository.deleteAll();
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/evaluations/byStudentAndGroup/{studentId}/{groupId}", student.getId(), group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetExtendedEvaluationsByStudentAndGroup() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/extended/evaluations/byStudentAndGroup/{studentId}/{groupId}", student.getId(), group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetExtendedEvaluationsByStudentAndGroup() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        evaluationReportRepository.deleteAll();
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/extended/evaluations/byStudentAndGroup/{studentId}/{groupId}", student.getId(), group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldAddEvaluationWithTeacherAndSolution() throws Exception {
        var teacher = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByTeacherId(teacher.getId()).get(0);
        var solution = solutionRepository.getSolutionsByGroupId(group.getId()).get(0);
        evaluationReportRepository.deleteAll();
        evaluationRepository.deleteAll();
        var body = objectMapper.writeValueAsString(createEvaluationDto(5.0, 5.0, false, teacher.getId(), solution.getId(), group.getId()));
        mockMvc.perform(post("/api/evaluation/withUserAndSolution/{userId}/{solutionId}", teacher.getId(), solution.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value("5.0"))
                .andReturn();
        assertEquals(1, evaluationRepository.findAll().size());
    }

    @Test
    void shouldNotAddEvaluationWithStudentAndSolution() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        var solution = solutionRepository.getSolutionsByGroupId(group.getId()).get(0);
        evaluationReportRepository.deleteAll();
        evaluationRepository.deleteAll();
        var body = objectMapper.writeValueAsString(createEvaluationDto(5.0, 5.0, false, student.getId(), solution.getId(), group.getId()));
        mockMvc.perform(post("/api/evaluation/withUserAndSolution/{userId}/{solutionId}", student.getId(), solution.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals(0, evaluationRepository.findAll().size());
    }

    @Test
    void shouldNotAddDuplicatedEvaluationWithTeacherAndSolution() throws Exception {
        var size = evaluationRepository.findAll().size();
        var teacher = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByTeacherId(teacher.getId()).get(0);
        var solution = solutionRepository.getSolutionsByGroupId(group.getId()).get(0);
        var body = objectMapper.writeValueAsString(createEvaluationDto(5.0, 5.0, false, teacher.getId(), solution.getId(), group.getId()));
        mockMvc.perform(post("/api/evaluation/withUserAndSolution/{userId}/{solutionId}", teacher.getId(), solution.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals(size, evaluationRepository.findAll().size());
    }

    @Test
    void shouldNotAddEvaluationWithNotExistingTeacher() throws Exception {
        var teacher = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByTeacherId(teacher.getId()).get(0);
        var solution = solutionRepository.getSolutionsByGroupId(group.getId()).get(0);
        evaluationReportRepository.deleteAll();
        evaluationRepository.deleteAll();
        var body = objectMapper.writeValueAsString(createEvaluationDto(5.0, 5.0, false, teacher.getId(), solution.getId(), group.getId()));
        mockMvc.perform(post("/api/evaluation/withUserAndSolution/{userId}/{solutionId}", 9999, solution.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(0, evaluationRepository.findAll().size());
    }

    @Test
    void shouldNotAddEvaluationWithNotExistingSolution() throws Exception {
        var teacher = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByTeacherId(teacher.getId()).get(0);
        var solution = solutionRepository.getSolutionsByGroupId(group.getId()).get(0);
        evaluationReportRepository.deleteAll();
        evaluationRepository.deleteAll();
        var body = objectMapper.writeValueAsString(createEvaluationDto(5.0, 5.0, false, teacher.getId(), solution.getId(), group.getId()));
        mockMvc.perform(post("/api/evaluation/withUserAndSolution/{userId}/{solutionId}", teacher.getId(), 9999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(0, evaluationRepository.findAll().size());
    }

    @Test
    void shouldNotAddEvaluationWithNotExistingTeacherAndSolution() throws Exception {
        var teacher = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByTeacherId(teacher.getId()).get(0);
        var solution = solutionRepository.getSolutionsByGroupId(group.getId()).get(0);
        evaluationReportRepository.deleteAll();
        evaluationRepository.deleteAll();
        var body = objectMapper.writeValueAsString(createEvaluationDto(5.0, 5.0, false, teacher.getId(), solution.getId(), group.getId()));
        mockMvc.perform(post("/api/evaluation/withUserAndSolution/{userId}/{solutionId}", 9999, 9999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(0, evaluationRepository.findAll().size());
    }
}
