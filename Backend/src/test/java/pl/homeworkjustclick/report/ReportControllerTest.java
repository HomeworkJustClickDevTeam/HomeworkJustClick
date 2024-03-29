package pl.homeworkjustclick.report;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;
import pl.homeworkjustclick.assignment.AssignmentRepository;
import pl.homeworkjustclick.group.GroupRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;

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
        "classpath:db/init_solution.sql",
        "classpath:db/init_evaluation.sql"
})
class ReportControllerTest extends BaseTestEntity {
    @Autowired
    AssignmentRepository assignmentRepository;
    @Autowired
    GroupRepository groupRepository;

    @Test
    void shouldGetAssignmentReport() throws Exception {
        var assignmentReportDto = AssignmentReportDto.builder()
                .assignmentId(assignmentRepository.findAll().get(0).getId())
                .maxResult(true)
                .minResult(true)
                .avgResult(true)
                .late(true)
                .hist(List.of(0, 50, 60, 70, 80, 90, 100))
                .build();
        var body = objectMapper.writeValueAsString(assignmentReportDto);
        mockMvc.perform(post("/api/report/assignment")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldGetGroupReport() throws Exception {
        var groupReportDto = GroupReportDto.builder()
                .groupId(groupRepository.findAll().get(0).getId())
                .maxResult(true)
                .minResult(true)
                .avgResult(true)
                .late(true)
                .hist(List.of(0, 50, 60, 70, 80, 90, 100))
                .build();
        var body = objectMapper.writeValueAsString(groupReportDto);
        mockMvc.perform(post("/api/report/group")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldGetAssignmentReportCsv() throws Exception {
        var assignmentReportDto = AssignmentReportDto.builder()
                .assignmentId(assignmentRepository.findAll().get(0).getId())
                .maxResult(true)
                .minResult(true)
                .avgResult(true)
                .late(true)
                .hist(List.of(0, 50, 60, 70, 80, 90, 100))
                .build();
        var body = objectMapper.writeValueAsString(assignmentReportDto);
        mockMvc.perform(post("/api/report/assignment_csv")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andReturn();
    }

    @Test
    void shouldGetGroupReportCsv() throws Exception {
        var groupReportDto = GroupReportDto.builder()
                .groupId(groupRepository.findAll().get(0).getId())
                .maxResult(true)
                .minResult(true)
                .avgResult(true)
                .late(true)
                .hist(List.of(0, 50, 60, 70, 80, 90, 100))
                .build();
        var body = objectMapper.writeValueAsString(groupReportDto);
        mockMvc.perform(post("/api/report/group_csv")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andReturn();
    }
}
