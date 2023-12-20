package pl.homeworkjustclick.notification;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;
import pl.homeworkjustclick.assignment.Assignment;
import pl.homeworkjustclick.evaluation.EvaluationDto;
import pl.homeworkjustclick.evaluation.EvaluationResponseExtendedDto;
import pl.homeworkjustclick.evaluationreport.EvaluationReportDto;
import pl.homeworkjustclick.group.GroupRepository;
import pl.homeworkjustclick.solution.SolutionRepository;
import pl.homeworkjustclick.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@Sql(scripts = {
        "classpath:db/cleanup_all.sql",
        "classpath:db/init_user.sql",
        "classpath:db/init_group.sql",
        "classpath:db/init_group_student.sql",
        "classpath:db/init_group_teacher.sql",
        "classpath:db/init_assignment.sql",
        "classpath:db/init_solution.sql"
})
class NotificationCreateServiceTest extends BaseTestEntity {
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    SolutionRepository solutionRepository;

    @Test
    void shouldCreateNotificationsWhenAssignmentIsCreated() throws Exception {
        var assignmentDto = createAssignment();
        var body = objectMapper.writeValueAsString(assignmentDto);
        var group = groupRepository.findAll().get(0);
        var teacher = userRepository.getTeachersByGroupId(group.getId()).get(0);
        mockMvc.perform(post("/api/assignment/withUserAndGroup/" + teacher.getId() + "/" + group.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(userRepository.getStudentsByGroupId(group.getId()).size(), notificationRepository.findAll().size());
    }

    @Test
    void shouldCreateNotificationWhenEvaluationIsCreated() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        var teacher = userRepository.getTeachersByGroupId(solution.getGroup().getId()).get(0);
        var evaluationDto = createEvaluationDto(solution.getGroup().getId(), teacher.getId(), solution.getId());
        var body = objectMapper.writeValueAsString(evaluationDto);
        mockMvc.perform(post("/api/evaluation")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andReturn();
        assertEquals(1, notificationRepository.findAllByUserId(solution.getUser().getId(), Pageable.ofSize(20)).getTotalElements());
    }

    @Test
    void shouldCreateNotificationWhenEvaluationIsReported() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        var teacher = userRepository.getTeachersByGroupId(solution.getGroup().getId()).get(0);
        var evaluationDto = createEvaluationDto(solution.getGroup().getId(), teacher.getId(), solution.getId());
        var body = objectMapper.writeValueAsString(evaluationDto);
        var evaluationResponse = mockMvc.perform(post("/api/evaluation")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andReturn();
        var evaluationResponseDto = objectMapper.readValue(evaluationResponse.getResponse().getContentAsString(), EvaluationResponseExtendedDto.class);
        var evaluationReportDto = EvaluationReportDto.builder().comment("comment").evaluationId(evaluationResponseDto.getId()).build();
        body = objectMapper.writeValueAsString(evaluationReportDto);
        mockMvc.perform(post("/api/evaluation_report")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andReturn();
        assertEquals(2, notificationRepository.findAll().size());
    }

    private EvaluationDto createEvaluationDto(Integer groupId, Integer teacherId, Integer solutionId) {
        return EvaluationDto.builder()
                .result(0.0)
                .userId(teacherId)
                .solutionId(solutionId)
                .groupId(groupId)
                .build();
    }

    private Assignment createAssignment() {
        return Assignment.builder()
                .taskDescription("test")
                .completionDatetime(OffsetDateTime.now())
                .title("test")
                .visible(true)
                .maxPoints(100)
                .autoPenalty(0)
                .build();
    }
}
