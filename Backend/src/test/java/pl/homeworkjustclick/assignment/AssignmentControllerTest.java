package pl.homeworkjustclick.assignment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;
import pl.homeworkjustclick.evaluation.EvaluationRepository;
import pl.homeworkjustclick.file.File;
import pl.homeworkjustclick.file.FileRepository;
import pl.homeworkjustclick.group.GroupRepository;
import pl.homeworkjustclick.solution.SolutionRepository;
import pl.homeworkjustclick.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

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
        "classpath:db/init_evaluation.sql"
})
public class AssignmentControllerTest extends BaseTestEntity {
    @Autowired
    AssignmentRepository assignmentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SolutionRepository solutionRepository;
    @Autowired
    EvaluationRepository evaluationRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    FileRepository fileRepository;

    @Test
    void shouldGetAssignmentById() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(get("/api/assignment/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assignment.getId()))
                .andExpect(jsonPath("$.title").value(assignment.getTitle()))
                .andReturn();
    }

    @Test
    void shouldNotGetAssignmentById() throws Exception {
        mockMvc.perform(get("/api/assignment/{id}", 9999))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetExtendedAssignmentById() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/assignment/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assignment.getId()))
                .andExpect(jsonPath("$.title").value(assignment.getTitle()))
                .andReturn();
    }

    @Test
    void shouldNotGetExtendedAssignmentById() throws Exception {
        mockMvc.perform(get("/api/extended/assignment/{id}", 9999))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllAssignments() throws Exception {
        mockMvc.perform(get("/api/assignments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6))
                .andReturn();
    }


    @Test
    void shouldGetAllAssignmentsExtended() throws Exception {
        mockMvc.perform(get("/api/extended/assignments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6))
                .andReturn();
    }

    @Test
    void shouldNotGetAllUndoneAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        mockMvc.perform(get("/api/assignments/undoneByStudent/{id}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllUndoneAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var solutions = solutionRepository.getSolutionsByUser(student.getId());
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll(solutions);
        mockMvc.perform(get("/api/assignments/undoneByStudent/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetAllUndoneExtendedAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        mockMvc.perform(get("/api/extended/assignments/undoneByStudent/{id}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllUndoneExtendedAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var solutions = solutionRepository.getSolutionsByUser(student.getId());
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll(solutions);
        mockMvc.perform(get("/api/extended/assignments/undoneByStudent/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetAllUndoneAssignmentsByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/assignments/undoneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllUndoneAssignmentsByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        var solutions = solutionRepository.getSolutionsByUser(student.getId());
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll(solutions);
        mockMvc.perform(get("/api/assignments/undoneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetAllUndoneExtendedAssignmentsByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/extended/assignments/undoneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllUndoneExtendedAssignmentsByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        var solutions = solutionRepository.getSolutionsByUser(student.getId());
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll(solutions);
        mockMvc.perform(get("/api/extended/assignments/undoneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyUncheckedAssignmentsByGroup() throws Exception {
        var teacher = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByTeacherId(teacher.getId()).get(0);
        mockMvc.perform(get("/api/assignments/unchecked/{id}", group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllUncheckedAssignmentsByGroup() throws Exception {
        var teacher = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var group = groupRepository.getGroupsByTeacherId(teacher.getId()).get(0);
        mockMvc.perform(get("/api/assignments/unchecked/{id}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyUncheckedExtendedAssignmentsByGroup() throws Exception {
        var teacher = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByTeacherId(teacher.getId()).get(0);
        mockMvc.perform(get("/api/extended/assignments/unchecked/{id}", group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    @Test
    void shouldGetAllUncheckedExtendedAssignmentsByGroup() throws Exception {
        var teacher = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var group = groupRepository.getGroupsByTeacherId(teacher.getId()).get(0);
        mockMvc.perform(get("/api/extended/assignments/unchecked/{id}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyNonExpiredUndoneAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        mockMvc.perform(get("/api/assignments/nonExpiredUndoneByStudent/{id}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllNonExpiredUndoneAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        mockMvc.perform(get("/api/assignments/nonExpiredUndoneByStudent/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyNonExpiredUndoneExtendedAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        mockMvc.perform(get("/api/extended/assignments/nonExpiredUndoneByStudent/{id}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllNonExpiredUndoneExtendedAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        mockMvc.perform(get("/api/extended/assignments/nonExpiredUndoneByStudent/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyNonExpiredUndoneAssignmentsByStudentAndGroup() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/assignments/nonExpiredUndoneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllNonExpiredUndoneAssignmentsByStudentAndGroup() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        mockMvc.perform(get("/api/assignments/nonExpiredUndoneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyNonExpiredUndoneExtendedAssignmentsByStudentAndGroup() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/extended/assignments/nonExpiredUndoneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllNonExpiredUndoneExtendedAssignmentsByStudentAndGroup() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        mockMvc.perform(get("/api/extended/assignments/nonExpiredUndoneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyExpiredUndoneAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        mockMvc.perform(get("/api/assignments/expiredUndoneByStudent/{studentId}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    @Test
    void shouldGetAllExpiredUndoneAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        mockMvc.perform(get("/api/assignments/expiredUndoneByStudent/{studentId}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyExpiredUndoneExtendedAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        mockMvc.perform(get("/api/extended/assignments/expiredUndoneByStudent/{studentId}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    @Test
    void shouldGetAllExpiredUndoneExtendedAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        mockMvc.perform(get("/api/extended/assignments/expiredUndoneByStudent/{studentId}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyExpiredUndoneAssignmentsByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/assignments/expiredUndoneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    @Test
    void shouldGetAllExpiredUndoneAssignmentsByGroupAndStudent() throws Exception {
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(1);
        mockMvc.perform(get("/api/assignments/expiredUndoneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyExpiredUndoneExtendedAssignmentsByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/extended/assignments/expiredUndoneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    @Test
    void shouldGetAllExpiredUndoneExtendedAssignmentsByGroupAndStudent() throws Exception {
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(1);
        mockMvc.perform(get("/api/extended/assignments/expiredUndoneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();
    }

    @Test
    void shouldGetAllDoneAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        mockMvc.perform(get("/api/assignments/doneByStudent/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyDoneAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        mockMvc.perform(get("/api/assignments/doneByStudent/{id}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllDoneAssignmentsExtendedByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        mockMvc.perform(get("/api/extended/assignments/doneByStudent/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyDoneAssignmentsExtendedByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        mockMvc.perform(get("/api/extended/assignments/doneByStudent/{id}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllDoneAssignmentsByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/assignments/doneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyDoneAssignmentsByGroupAndStudent() throws Exception {
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/assignments/doneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllDoneExtendedAssignmentsByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/extended/assignments/doneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyDoneExtendedAssignmentsByGroupAndStudent() throws Exception {
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/extended/assignments/doneByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        mockMvc.perform(get("/api/assignments/byStudent/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        assignmentRepository.deleteAll();
        mockMvc.perform(get("/api/assignments/byStudent/{id}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllExtendedAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        mockMvc.perform(get("/api/extended/assignments/byStudent/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyExtendedAssignmentsByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        assignmentRepository.deleteAll();
        mockMvc.perform(get("/api/extended/assignments/byStudent/{id}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllAssignmentsByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/assignments/byGroupId/{id}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyAssignmentsByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        assignmentRepository.deleteAll();
        mockMvc.perform(get("/api/assignments/byGroupId/{id}", group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllExtendedAssignmentsByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/assignments/byGroupId/{id}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyExtendedAssignmentsByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        assignmentRepository.deleteAll();
        mockMvc.perform(get("/api/extended/assignments/byGroupId/{id}", group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllAssignmentsByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/assignments/allByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }


    @Test
    void shouldNotGetAnyAssignmentsByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        assignmentRepository.deleteAll();
        mockMvc.perform(get("/api/assignments/allByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllExtendedAssignmentsByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/extended/assignments/allByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }


    @Test
    void shouldNotGetAnyExtendedAssignmentsByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        assignmentRepository.deleteAll();
        mockMvc.perform(get("/api/extended/assignments/allByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldCheckForSolutionToAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(get("/api/assignment/checkForSolution/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("true"))
                .andReturn();
    }

    @Test
    void shouldCheckForSolutionToNotExistingAssignment() throws Exception {
        mockMvc.perform(get("/api/assignment/checkForSolution/{id}", 9999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldCheckForFileToAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        fileRepository.save(File.builder().assignment(assignment).build());
        mockMvc.perform(get("/api/assignment/checkForFile/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("true"))
                .andReturn();
    }

    @Test
    void shouldCheckForFileToNotExistingAssignment() throws Exception {
        mockMvc.perform(get("/api/assignment/checkForFile/{id}", 9999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldGetAssignmentsCalendar() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        mockMvc.perform(get("/api/assignments/calendarListByStudent/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldAddAssignmentWithUserAndGroup() throws Exception {
        var assignment = Assignment.builder()
                .taskDescription("desc")
                .completionDatetime(OffsetDateTime.now())
                .title("title")
                .visible(true)
                .maxPoints(10)
                .autoPenalty(0)
                .build();
        var body = objectMapper.writeValueAsString(assignment);
        var user = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByTeacherId(user.getId()).get(0);
        var size = assignmentRepository.findAll().size();
        mockMvc.perform(post("/api/assignment/withUserAndGroup/{userId}/{groupId}", user.getId(), group.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(size + 1, assignmentRepository.findAll().size());
    }

    @Test
    void shouldNotAddAssignmentWithNotExistingUserAndGroup() throws Exception {
        var assignment = Assignment.builder()
                .taskDescription("desc")
                .completionDatetime(OffsetDateTime.now())
                .title("title")
                .visible(true)
                .maxPoints(10)
                .autoPenalty(0)
                .build();
        var body = objectMapper.writeValueAsString(assignment);
        var user = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByTeacherId(user.getId()).get(0);
        var size = assignmentRepository.findAll().size();
        mockMvc.perform(post("/api/assignment/withUserAndGroup/{userId}/{groupId}", 9999, group.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(size, assignmentRepository.findAll().size());
    }

    @Test
    void shouldNotAddAssignmentWithUserAndNotExistingGroup() throws Exception {
        var assignment = Assignment.builder()
                .taskDescription("desc")
                .completionDatetime(OffsetDateTime.now())
                .title("title")
                .visible(true)
                .maxPoints(10)
                .autoPenalty(0)
                .build();
        var body = objectMapper.writeValueAsString(assignment);
        var user = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var size = assignmentRepository.findAll().size();
        mockMvc.perform(post("/api/assignment/withUserAndGroup/{userId}/{groupId}", user.getId(), 9999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(size, assignmentRepository.findAll().size());
    }

    @Test
    void shouldNotAddAssignmentWithNotExistingUserAndNotExistingGroup() throws Exception {
        var assignment = Assignment.builder()
                .taskDescription("desc")
                .completionDatetime(OffsetDateTime.now())
                .title("title")
                .visible(true)
                .maxPoints(10)
                .autoPenalty(0)
                .build();
        var body = objectMapper.writeValueAsString(assignment);
        var size = assignmentRepository.findAll().size();
        mockMvc.perform(post("/api/assignment/withUserAndGroup/{userId}/{groupId}", 9999, 9999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(size, assignmentRepository.findAll().size());
    }

    @Test
    void shouldUpdateAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        assignment.setTitle("test");
        assignment.setAutoPenalty(0);
        var body = objectMapper.writeValueAsString(assignment);
        mockMvc.perform(put("/api/assignment/{id}", assignment.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
        var updatedAssignment = assignmentRepository.findById(assignment.getId()).get();
        assertEquals("test", updatedAssignment.getTitle());
        assertEquals(0, updatedAssignment.getAutoPenalty());
    }

    @Test
    void shouldNotUpdateNotExistingAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        assignment.setTitle("test");
        assignment.setAutoPenalty(0);
        var body = objectMapper.writeValueAsString(assignment);
        mockMvc.perform(put("/api/assignment/{id}", 9999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldUpdateAssignmentVisibility() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        var visibility = !assignment.getVisible();
        var body = objectMapper.writeValueAsString(visibility);
        mockMvc.perform(put("/api/assignment/visibility/{id}", assignment.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
        var updatedAssignment = assignmentRepository.findById(assignment.getId()).get();
        assertEquals(visibility, updatedAssignment.getVisible());
    }

    @Test
    void shouldNotUpdateNotExistingAssignmentVisibility() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        var visibility = !assignment.getVisible();
        var body = objectMapper.writeValueAsString(visibility);
        mockMvc.perform(put("/api/assignment/visibility/{id}", 9999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldUpdateAssignmentTitle() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(put("/api/assignment/title/{id}", assignment.getId())
                        .content("123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
        var updatedAssignment = assignmentRepository.findById(assignment.getId()).get();
        assertEquals("123", updatedAssignment.getTitle());
    }

    @Test
    void shouldNotUpdateNotExistingAssignmentTitle() throws Exception {
        mockMvc.perform(put("/api/assignment/title/{id}", 9999)
                        .content("123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldUpdateAssignmentTaskDescription() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(put("/api/assignment/taskDescription/{id}", assignment.getId())
                        .content("123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
        var updatedAssignment = assignmentRepository.findById(assignment.getId()).get();
        assertEquals("123", updatedAssignment.getTaskDescription());
    }

    @Test
    void shouldNotUpdateNotExistingAssignmentTaskDescription() throws Exception {
        mockMvc.perform(put("/api/assignment/taskDescription/{id}", 9999)
                        .content("123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldUpdateAssignmentMaxPoints() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(put("/api/assignment/maxPoints/{id}", assignment.getId())
                        .content("99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
        var updatedAssignment = assignmentRepository.findById(assignment.getId()).get();
        assertEquals(99, updatedAssignment.getMaxPoints());
    }

    @Test
    void shouldNotUpdateNotExistingAssignmentMaxPoints() throws Exception {
        mockMvc.perform(put("/api/assignment/maxPoints/{id}", 9999)
                        .content("99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldUpdateAssignmentCompletionDatetime() throws Exception {
        var date = OffsetDateTime.now();
        var body = objectMapper.writeValueAsString(date);
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(put("/api/assignment/completionDatetime/{id}", assignment.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
        var updatedAssignment = assignmentRepository.findById(assignment.getId()).get();
        assertEquals(date.getSecond(), updatedAssignment.getCompletionDatetime().getSecond());
    }

    @Test
    void shouldNotUpdateNotExistingAssignmentCompletionDatetime() throws Exception {
        var date = OffsetDateTime.now();
        var body = objectMapper.writeValueAsString(date);
        mockMvc.perform(put("/api/assignment/completionDatetime/{id}", 9999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldUpdateAssignmentAutoPenalty() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(put("/api/assignment/autoPenalty/{id}", assignment.getId())
                        .content("25")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
        var updatedAssignment = assignmentRepository.findById(assignment.getId()).get();
        assertEquals(25, updatedAssignment.getAutoPenalty());
    }

    @Test
    void shouldNotUpdateNotExistingAssignmentAutoPenalty() throws Exception {
        mockMvc.perform(put("/api/assignment/autoPenalty/{id}", 9999)
                        .content("25")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldUpdateAssignmentUser() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(put("/api/assignment/user/{userId}/{id}", assignment.getUser().getId(), assignment.getId()))
                .andExpect(status().isOk())
                .andReturn();
        var updatedAssignment = assignmentRepository.findById(assignment.getId()).get();
        assertEquals(assignment.getUser().getId(), updatedAssignment.getUser().getId());
    }

    @Test
    void shouldNotUpdateNotExistingAssignmentUser() throws Exception {
        mockMvc.perform(put("/api/assignment/user/{userId}/{id}", 9999, 9999))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldUpdateAssignmentGroup() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(put("/api/assignment/group/{groupId}/{id}", assignment.getGroup().getId(), assignment.getId()))
                .andExpect(status().isOk())
                .andReturn();
        var updatedAssignment = assignmentRepository.findById(assignment.getId()).get();
        assertEquals(assignment.getGroup().getId(), updatedAssignment.getGroup().getId());
    }

    @Test
    void shouldNotUpdateNotExistingAssignmentGroup() throws Exception {
        mockMvc.perform(put("/api/assignment/group/{userId}/{id}", 9999, 9999))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldDeleteAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        var size = assignmentRepository.findAll().size();
        mockMvc.perform(delete("/api/assignment/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(size - 1, assignmentRepository.findAll().size());
    }

    @Test
    void shouldNotDeleteNotExistingAssignment() throws Exception {
        var size = assignmentRepository.findAll().size();
        mockMvc.perform(delete("/api/assignment/{id}", 9999))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(size, assignmentRepository.findAll().size());
    }

}
