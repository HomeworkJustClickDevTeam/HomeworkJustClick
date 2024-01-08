package pl.homeworkjustclick.solution;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;
import pl.homeworkjustclick.assignment.AssignmentRepository;
import pl.homeworkjustclick.evaluation.EvaluationRepository;
import pl.homeworkjustclick.file.File;
import pl.homeworkjustclick.file.FileRepository;
import pl.homeworkjustclick.group.GroupRepository;
import pl.homeworkjustclick.user.UserRepository;

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
        "classpath:db/init_evaluation.sql"
})
public class SolutionControllerTest extends BaseTestEntity {
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
    void shouldGetAllSolutions() throws Exception {
        mockMvc.perform(get("/api/solutions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(12))
                .andReturn();
    }

    @Test
    void shouldGetAllSolutionsExtended() throws Exception {
        mockMvc.perform(get("/api/extended/solutions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(12))
                .andReturn();
    }

    @Test
    void shouldGetSolutionById() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        mockMvc.perform(get("/api/solution/{id}", solution.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(solution.getId()))
                .andReturn();
    }

    @Test
    void shouldNotGetSolutionById() throws Exception {
        mockMvc.perform(get("/api/solution/{id}", 9999))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetSolutionExtendedById() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/solution/{id}", solution.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(solution.getId()))
                .andReturn();
    }

    @Test
    void shouldNotGetSolutionExtendedById() throws Exception {
        mockMvc.perform(get("/api/extended/solution/{id}", 9999))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldNotGetAnyUncheckedSolutionsByTeacher() throws Exception {
        var teacher = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        mockMvc.perform(get("/api/solutions/uncheckedByTeacher/{id}", teacher.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllUncheckedSolutionsByTeacher() throws Exception {
        var teacher = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/solutions/uncheckedByTeacher/{id}", teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyUncheckedSolutionsExtendedByTeacher() throws Exception {
        var teacher = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        mockMvc.perform(get("/api/extended/solutions/uncheckedByTeacher/{id}", teacher.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllUncheckedSolutionsExtendedByTeacher() throws Exception {
        var teacher = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/extended/solutions/uncheckedByTeacher/{id}", teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyUncheckedSolutionsByStudentAndGroup() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/solutions/uncheckedByStudentAndGroup/{studentId}/{groupId}", student.getId(), group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllUncheckedSolutionsByStudentAndGroup() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/solutions/uncheckedByStudentAndGroup/{studentId}/{groupId}", student.getId(), group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyUncheckedSolutionsExtendedByStudentAndGroup() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/extended/solutions/uncheckedByStudentAndGroup/{studentId}/{groupId}", student.getId(), group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllUncheckedSolutionsExtendedByStudentAndGroup() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/extended/solutions/uncheckedByStudentAndGroup/{studentId}/{groupId}", student.getId(), group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyUncheckedSolutionsByStudent() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        mockMvc.perform(get("/api/solutions/uncheckedByStudent/{studentId}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllUncheckedSolutionsByStudent() throws Exception {
        evaluationRepository.deleteAll();
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        mockMvc.perform(get("/api/solutions/uncheckedByStudent/{studentId}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyUncheckedSolutionsExtendedByStudent() throws Exception {
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        mockMvc.perform(get("/api/extended/solutions/uncheckedByStudent/{studentId}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllUncheckedSolutionsExtendedByStudent() throws Exception {
        evaluationRepository.deleteAll();
        var student = userRepository.findByEmail("anna_malinowska@gmail.com").get();
        mockMvc.perform(get("/api/extended/solutions/uncheckedByStudent/{studentId}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyUncheckedSolutionsByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/solutions/uncheckedByGroup/{groupId}", group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllUncheckedSolutionsByGroup() throws Exception {
        evaluationRepository.deleteAll();
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/solutions/uncheckedByGroup/{groupId}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyUncheckedSolutionsByGroupExtended() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/solutions/uncheckedByGroup/{groupId}", group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllUncheckedSolutionsByGroupExtended() throws Exception {
        evaluationRepository.deleteAll();
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/solutions/uncheckedByGroup/{groupId}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyUncheckedSolutionsByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(get("/api/solutions/uncheckedByAssignment/{id}", assignment.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllUncheckedSolutionsByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/solutions/uncheckedByAssignment/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyUncheckedSolutionsExtendedByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/solutions/uncheckedByAssignment/{id}", assignment.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllUncheckedSolutionsExtendedByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/extended/solutions/uncheckedByAssignment/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldGetUncheckedSolutionByUserAssignmentAndGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(user.getId()).get(0);
        var assignment = assignmentRepository.getAssignmentsByGroupId(group.getId()).get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/solution/getUncheckedSolutionByUserAssignmentGroup/{user}/{assignment}/{group}", user.getId(), assignment.getId(), group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.groupId").value(group.getId()))
                .andExpect(jsonPath("$.assignmentId").value(assignment.getId()))
                .andReturn();
    }

    @Test
    void shouldNotGetUncheckedSolutionByUserAssignmentAndGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByTeacherId(user.getId()).get(0);
        var assignment = assignmentRepository.getAssignmentsByGroupId(group.getId()).get(0);
        mockMvc.perform(get("/api/solution/getUncheckedSolutionByUserAssignmentGroup/{user}/{assignment}/{group}", user.getId(), assignment.getId(), group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetCheckedSolutionByUserAssignmentAndGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(user.getId()).get(0);
        var assignment = assignmentRepository.getAssignmentsByGroupId(group.getId()).get(0);
        mockMvc.perform(get("/api/solution/getCheckedSolutionByUserAssignmentGroup/{user}/{assignment}/{group}", user.getId(), assignment.getId(), group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.groupId").value(group.getId()))
                .andExpect(jsonPath("$.assignmentId").value(assignment.getId()))
                .andReturn();
    }

    @Test
    void shouldNotGetCheckedSolutionByUserAssignmentAndGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(user.getId()).get(0);
        var assignment = assignmentRepository.getAssignmentsByGroupId(group.getId()).get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/solution/getCheckedSolutionByUserAssignmentGroup/{user}/{assignment}/{group}", user.getId(), assignment.getId(), group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllLateSolutionsByStudent() throws Exception {
        var student = userRepository.findByEmail("zofia_danielska@gmail.com").get();
        mockMvc.perform(get("/api/solutions/lateByStudent/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyLateSolutionsByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        mockMvc.perform(get("/api/solutions/lateByStudent/{id}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllLateSolutionsExtendedByStudent() throws Exception {
        var student = userRepository.findByEmail("zofia_danielska@gmail.com").get();
        mockMvc.perform(get("/api/extended/solutions/lateByStudent/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyLateSolutionsExtendedByStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        mockMvc.perform(get("/api/extended/solutions/lateByStudent/{id}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllLateSolutionsByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("zofia_danielska@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/solutions/lateByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyLateSolutionsByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/solutions/lateByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllLateSolutionsExtendedByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("zofia_danielska@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/extended/solutions/lateByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyLateSolutionsExtendedByGroupAndStudent() throws Exception {
        var student = userRepository.findByEmail("jan_kowalski@gmail.com").get();
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/extended/solutions/lateByGroupAndStudent/{groupId}/{studentId}", group.getId(), student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldNotGetAnyLateSolutionsByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/solutions/lateByGroup/{groupId}", group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllLateSolutionsByGroup() throws Exception {
        var group = groupRepository.findAll().get(1);
        mockMvc.perform(get("/api/solutions/lateByGroup/{groupId}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyLateSolutionsExtendedByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/solutions/lateByGroup/{groupId}", group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllLateSolutionsExtendedByGroup() throws Exception {
        var group = groupRepository.findAll().get(1);
        mockMvc.perform(get("/api/extended/solutions/lateByGroup/{groupId}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andReturn();
    }

    @Test
    void shouldGetAllLateSolutionsByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(2);
        mockMvc.perform(get("/api/solutions/lateByAssignment/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyLateSolutionsByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(get("/api/solutions/lateByAssignment/{id}", assignment.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetAllLateSolutionsExtendedByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(2);
        mockMvc.perform(get("/api/extended/solutions/lateByAssignment/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetAnyLateSolutionsExtendedByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/solutions/lateByAssignment/{id}", assignment.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldFindAllSolutionsCheckedByTeacher() throws Exception {
        var teacher = userRepository.findAll().get(0);
        mockMvc.perform(get("/api/solutions/checkedByTeacher/{id}", teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotFindAnySolutionsCheckedByTeacher() throws Exception {
        var teacher = userRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/solutions/checkedByTeacher/{id}", teacher.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldFindAllSolutionsExtendedCheckedByTeacher() throws Exception {
        var teacher = userRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/solutions/checkedByTeacher/{id}", teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotFindAnySolutionsExtendedCheckedByTeacher() throws Exception {
        var teacher = userRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/extended/solutions/checkedByTeacher/{id}", teacher.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldFindAllSolutionsCheckedByStudent() throws Exception {
        var student = userRepository.findAll().get(0);
        mockMvc.perform(get("/api/solutions/checkedByStudent/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotFindAnySolutionsCheckedByStudent() throws Exception {
        var student = userRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/solutions/checkedByStudent/{id}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldFindAllSolutionsExtendedCheckedByStudent() throws Exception {
        var student = userRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/solutions/checkedByStudent/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotFindAnySolutionsExtendedCheckedByStudent() throws Exception {
        var student = userRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/extended/solutions/checkedByStudent/{id}", student.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldFindAllSolutionsCheckedByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/solutions/checkedByGroup/{id}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotFindAnySolutionsCheckedByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/solutions/checkedByGroup/{id}", group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldFindAllSolutionsExtendedCheckedByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/solutions/checkedByGroup/{id}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotFindAnySolutionsExtendedCheckedByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/extended/solutions/checkedByGroup/{id}", group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldFindAllSolutionsCheckedByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(get("/api/solutions/checkedByAssignment/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotFindAnySolutionsCheckedByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/solutions/checkedByAssignment/{id}", assignment.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldFindAllSolutionsExtendedCheckedByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/solutions/checkedByAssignment/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotFindAnySolutionsExtendedCheckedByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/extended/solutions/checkedByAssignment/{id}", assignment.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldFindAllSolutionsCheckedByStudentAndGroup() throws Exception {
        var student = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/solutions/checkedByStudentAndGroup/{studentId}/{groupId}", student.getId(), group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotFindAnySolutionsCheckedByStudentAndGroup() throws Exception {
        var student = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/solutions/checkedByStudentAndGroup/{studentId}/{groupId}", student.getId(), group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldFindAllSolutionsExtendedCheckedByStudentAndGroup() throws Exception {
        var student = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        mockMvc.perform(get("/api/extended/solutions/checkedByStudentAndGroup/{studentId}/{groupId}", student.getId(), group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotFindAnySolutionsExtendedCheckedByStudentAndGroup() throws Exception {
        var student = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(student.getId()).get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/extended/solutions/checkedByStudentAndGroup/{studentId}/{groupId}", student.getId(), group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetCalendarListByTeacher() throws Exception {
        var teacher = userRepository.findAll().get(0);
        mockMvc.perform(get("/api/solutions/calendarListByTeacher/{id}", teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetCalendarListByTeacher() throws Exception {
        var teacher = userRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        mockMvc.perform(get("/api/solutions/calendarListByTeacher/{id}", teacher.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andReturn();
    }

    @Test
    void shouldGetSolutionsByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/solutions/byGroup/{id}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetSolutionsByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        mockMvc.perform(get("/api/solutions/byGroup/{id}", group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetSolutionsExtendedByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/solutions/byGroup/{id}", group.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andReturn();
    }

    @Test
    void shouldNotGetSolutionsExtendedByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        mockMvc.perform(get("/api/extended/solutions/byGroup/{id}", group.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetSolutionsByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(get("/api/solutions/byAssignment/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetSolutionsByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        mockMvc.perform(get("/api/solutions/byAssignment/{id}", assignment.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetSolutionsExtendedByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(get("/api/extended/solutions/byAssignment/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetSolutionsExtendedByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        mockMvc.perform(get("/api/extended/solutions/byAssignment/{id}", assignment.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldCheckForFileToSolutionAndReturnTrue() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        fileRepository.save(File.builder().solution(solution).build());
        mockMvc.perform(get("/api/solution/checkForFile/{id}", solution.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("true"))
                .andReturn();
    }

    @Test
    void shouldCheckForFileToSolutionAndReturnFalse() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        mockMvc.perform(get("/api/solution/checkForFile/{id}", solution.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldCheckForFileToNotExistingSolutionAndReturnFalse() throws Exception {
        mockMvc.perform(get("/api/solution/checkForFile/{id}", 9999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldCheckForEvaluationToSolutionAndReturnTrue() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        mockMvc.perform(get("/api/solution/checkForEvaluation/{id}", solution.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("true"))
                .andReturn();
    }

    @Test
    void shouldCheckForEvaluationToSolutionAndReturnFalse() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(get("/api/solution/checkForEvaluation/{id}", solution.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldCheckForEvaluationToNotExistingSolutionAndReturnFalse() throws Exception {
        mockMvc.perform(get("/api/solution/checkForEvaluation/{id}", 9999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldAddSolutionWithUserAndAssignment() throws Exception {
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        var user = userRepository.findAll().get(0);
        var assignment = assignmentRepository.getAllAssignmentsByStudent(user.getId()).get(0);
        var solution = Solution.builder().comment("test").build();
        var body = objectMapper.writeValueAsString(solution);
        mockMvc.perform(post("/api/solution/withUserAndAssignment/{userId}/{assignmentId}", user.getId(), assignment.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("test"))
                .andReturn();
    }

    @Test
    void shouldNotAddSolutionWithTeacherAndAssignment() throws Exception {
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        var user = userRepository.findAll().get(0);
        var assignment = assignmentRepository.getAllAssignmentsByStudent(user.getId()).get(0);
        var solution = Solution.builder().comment("test").build();
        var body = objectMapper.writeValueAsString(solution);
        mockMvc.perform(post("/api/solution/withUserAndAssignment/{userId}/{assignmentId}", assignment.getUser().getId(), assignment.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void shouldNotAddSolutionWithNotExistingUserAndAssignment() throws Exception {
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        var user = userRepository.findAll().get(0);
        var assignment = assignmentRepository.getAllAssignmentsByStudent(user.getId()).get(0);
        var solution = Solution.builder().comment("test").build();
        var body = objectMapper.writeValueAsString(solution);
        mockMvc.perform(post("/api/solution/withUserAndAssignment/{userId}/{assignmentId}", 9999, assignment.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldNotAddSolutionWithUserAndNotExistingAssignment() throws Exception {
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        var user = userRepository.findAll().get(0);
        var solution = Solution.builder().comment("test").build();
        var body = objectMapper.writeValueAsString(solution);
        mockMvc.perform(post("/api/solution/withUserAndAssignment/{userId}/{assignmentId}", user.getId(), 9999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldNotAddSolutionWithNotExistingUserAndNotExistingAssignment() throws Exception {
        evaluationRepository.deleteAll();
        solutionRepository.deleteAll();
        var solution = Solution.builder().comment("test").build();
        var body = objectMapper.writeValueAsString(solution);
        mockMvc.perform(post("/api/solution/withUserAndAssignment/{userId}/{assignmentId}", 9999, 9999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldNotAddDuplicatedSolution() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        var body = objectMapper.writeValueAsString(Solution.builder().comment("test").build());
        mockMvc.perform(post("/api/solution/withUserAndAssignment/{userId}/{assignmentId}", solution.getUser().getId(), solution.getAssignment().getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void shouldChangeUserInSolution() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        mockMvc.perform(put("/api/solution/user/{userId}/{solutionId}", solution.getUser().getId(), solution.getId()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldNotChangeNotExistingUserInSolution() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        mockMvc.perform(put("/api/solution/user/{userId}/{solutionId}", 9999, solution.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldChangeAssignmentInSolution() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        mockMvc.perform(put("/api/solution/assignment/{userId}/{solutionId}", solution.getAssignment().getId(), solution.getId()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldNotChangeNotExistingAssignmentInSolution() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        mockMvc.perform(put("/api/solution/assignment/{userId}/{solutionId}", 9999, solution.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldDeleteSolutionWhenNotEvaluated() throws Exception {
        var size = solutionRepository.findAll().size();
        var solution = solutionRepository.findAll().get(0);
        evaluationRepository.deleteAll();
        mockMvc.perform(delete("/api/solution/{id}", solution.getId())
                        .headers(HttpHeaders.writableHttpHeaders(createHttpHeaders())))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(size - 1, solutionRepository.findAll().size());
    }

    @Test
    void shouldNotDeleteSolutionWhenEvaluated() throws Exception {
        var size = solutionRepository.findAll().size();
        var solution = solutionRepository.findAll().get(0);
        mockMvc.perform(delete("/api/solution/{id}", solution.getId())
                        .headers(HttpHeaders.writableHttpHeaders(createHttpHeaders())))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals(size, solutionRepository.findAll().size());
    }

    @Test
    void shouldNotDeleteNotExistingSolution() throws Exception {
        var size = solutionRepository.findAll().size();
        mockMvc.perform(delete("/api/solution/{id}", 9999)
                        .headers(HttpHeaders.writableHttpHeaders(createHttpHeaders())))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(size, solutionRepository.findAll().size());
    }
}
