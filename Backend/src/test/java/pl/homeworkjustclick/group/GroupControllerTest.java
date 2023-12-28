package pl.homeworkjustclick.group;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;
import pl.homeworkjustclick.groupstudent.GroupStudentRepository;
import pl.homeworkjustclick.user.UserRepository;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {
        "classpath:db/cleanup_all.sql",
        "classpath:db/init_user.sql",
        "classpath:db/init_group.sql",
        "classpath:db/init_group_student.sql",
        "classpath:db/init_group_teacher.sql"
})
@WithMockUser
class GroupControllerTest extends BaseTestEntity {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupStudentRepository groupStudentRepository;

    @Test
    void shouldGetAllGroups() throws Exception {
        mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldGetAllGroupsByUser() throws Exception {
        var user = userRepository.findAll().get(0);
        mockMvc.perform(get("/api/groups/byUser/{id}", user.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value(3))
                .andReturn();
    }

    @Test
    void shouldNotGetAllGroupsByNotExistingUser() throws Exception {
        mockMvc.perform(get("/api/groups/ByUser/{id}", 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldGetAllGroupsByTeacher() throws Exception {
        var user = userRepository.findAll().get(0);
        mockMvc.perform(get("/api/groups/byTeacher/{id}", user.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();
    }

    @Test
    void shouldNotGetAllGroupsByNotExistingTeacher() throws Exception {
        mockMvc.perform(get("/api/groups/byTeacher/{id}", 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldGetAllGroupsByStudent() throws Exception {
        var user = userRepository.findAll().get(0);
        mockMvc.perform(get("/api/groups/byStudent/{id}", user.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetAllGroupsByNotExistingStudent() throws Exception {
        mockMvc.perform(get("/api/groups/byStudent/{id}", 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldGetGroupById() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/group/{id}", group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(group.getId()))
                .andReturn();
    }

    @Test
    void shouldNotGetGroupByNotExistingId() throws Exception {
        mockMvc.perform(get("/api/group/{id}", 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldUserCheckWithRoleForTeacher() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByTeacherId(user.getId()).get(0);
        mockMvc.perform(get("/api/group/userCheckWithRole/{userId}/{groupId}", user.getId(), group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("Teacher"))
                .andReturn();
    }

    @Test
    void shouldUserCheckWithRoleForStudent() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(user.getId()).get(0);
        mockMvc.perform(get("/api/group/userCheckWithRole/{userId}/{groupId}", user.getId(), group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("Student"))
                .andReturn();
    }

    @Test
    void shouldNotUserCheckWithRoleForNotExistingUser() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(user.getId()).get(0);
        mockMvc.perform(get("/api/group/userCheckWithRole/{userId}/{groupId}", 9999, group.getId()))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$").value("User not in group"))
                .andReturn();
    }

    @Test
    void shouldNotUserCheckWithRoleForNotExistingGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        mockMvc.perform(get("/api/group/userCheckWithRole/{userId}/{groupId}", user.getId(), 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldNotUserCheckWithRoleForNotExistingUserAndGroup() throws Exception {
        mockMvc.perform(get("/api/group/userCheckWithRole/{userId}/{groupId}", 9999, 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldReturnTrueIfUserIsInGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(user.getId()).get(0);
        mockMvc.perform(get("/api/group/userCheck/{userId}/{groupId}", user.getId(), group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("true"))
                .andReturn();
    }

    @Test
    void shouldReturnFalseIfUserIsNotInGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.save(Group.builder().build());
        mockMvc.perform(get("/api/group/userCheck/{userId}/{groupId}", user.getId(), group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldReturnFalseIfNotExistingUserIsInGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(user.getId()).get(0);
        mockMvc.perform(get("/api/group/userCheck/{userId}/{groupId}", 9999, group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldReturnFalseIfUserIsInNotExistingGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        mockMvc.perform(get("/api/group/userCheck/{userId}/{groupId}", user.getId(), 9999))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldReturnFalseIfNotExistingUserIsInNotExistingGroup() throws Exception {
        mockMvc.perform(get("/api/group/userCheck/{userId}/{groupId}", 9999, 9999))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldReturnTrueIfUserIsTeacherInGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByTeacherId(user.getId()).get(0);
        mockMvc.perform(get("/api/group/teacherCheck/{userId}/{groupId}", user.getId(), group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("true"))
                .andReturn();
    }

    @Test
    void shouldReturnFalseIfUserIsNotTeacherInGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(user.getId()).get(0);
        mockMvc.perform(get("/api/group/teacherCheck/{userId}/{groupId}", user.getId(), group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldReturnFalseIfNotExistingUserIsTeacherInGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByTeacherId(user.getId()).get(0);
        mockMvc.perform(get("/api/group/teacherCheck/{userId}/{groupId}", 9999, group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldReturnFalseIfUserIsTeacherInNotExistingGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        mockMvc.perform(get("/api/group/teacherCheck/{userId}/{groupId}", user.getId(), 9999))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldReturnFalseIfNotExistingUserIsTeacherInNotExistingGroup() throws Exception {
        mockMvc.perform(get("/api/group/teacherCheck/{userId}/{groupId}", 9999, 9999))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldReturnTrueIfUserIsStudentInGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(user.getId()).get(0);
        mockMvc.perform(get("/api/group/studentCheck/{userId}/{groupId}", user.getId(), group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("true"))
                .andReturn();
    }

    @Test
    void shouldReturnFalseIfUserIsNotStudentInGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByTeacherId(user.getId()).get(0);
        mockMvc.perform(get("/api/group/studentCheck/{userId}/{groupId}", user.getId(), group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldReturnFalseIfNotExistingUserIsStudentInGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(user.getId()).get(0);
        mockMvc.perform(get("/api/group/studentCheck/{userId}/{groupId}", 9999, group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldReturnFalseIfUserIsStudentInNotExistingGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        mockMvc.perform(get("/api/group/studentCheck/{userId}/{groupId}", user.getId(), 9999))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldReturnFalseIfNotExistingUserIsStudentInNotExistingGroup() throws Exception {
        mockMvc.perform(get("/api/group/studentCheck/{userId}/{groupId}", 9999, 9999))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }

    @Test
    void shouldCreateGroupWithTeacher() throws Exception {
        var groupsSize = groupRepository.findAll().size();
        var group = Group.builder()
                .name("test")
                .description("test")
                .build();
        var body = objectMapper.writeValueAsString(group);
        var userId = userRepository.findAll().get(0).getId();
        mockMvc.perform(post("/api/group/withTeacher/{userId}", userId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        assertEquals(groupsSize + 1, groupRepository.findAll().size());
    }


    @Test
    void shouldNotCreateGroupWithNotExistingTeacher() throws Exception {
        var groupsSize = groupRepository.findAll().size();
        var group = Group.builder()
                .name("test")
                .description("test")
                .build();
        var body = objectMapper.writeValueAsString(group);
        mockMvc.perform(post("/api/group/withTeacher/{userId}", 9999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
        assertEquals(groupsSize, groupRepository.findAll().size());
    }

    @Test
    void shouldAddTeacherToGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.save(Group.builder().build());
        mockMvc.perform(post("/api/group/addTeacher/{userId}/{groupId}", user.getId(), group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    void shouldNotAddNotExistingTeacherToGroup() throws Exception {
        var group = groupRepository.save(Group.builder().build());
        mockMvc.perform(post("/api/group/addTeacher/{userId}/{groupId}", 9999, group.getId()))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldNotAddTeacherToNotExistingGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        mockMvc.perform(post("/api/group/addTeacher/{userId}/{groupId}", user.getId(), 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldAddStudentToGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.save(Group.builder().build());
        mockMvc.perform(post("/api/group/addStudent/{userId}/{groupId}", user.getId(), group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    void shouldNotAddNotExistingStudentToGroup() throws Exception {
        var group = groupRepository.save(Group.builder().build());
        mockMvc.perform(post("/api/group/addStudent/{userId}/{groupId}", 9999, group.getId()))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldNotAddStudentToNotExistingGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        mockMvc.perform(post("/api/group/addStudent/{userId}/{groupId}", user.getId(), 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldDeleteGroup() throws Exception {
        var groupsSize = groupRepository.findAll().size();
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(delete("/api/group/{groupId}", group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        assertTrue(groupRepository.findById(group.getId()).isEmpty());
        assertEquals(groupsSize - 1, groupRepository.findAll().size());
    }

    @Test
    void shouldNotDeleteNotExistingGroup() throws Exception {
        var groupsSize = groupRepository.findAll().size();
        mockMvc.perform(delete("/api/group/{groupId}", 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
        assertEquals(groupsSize, groupRepository.findAll().size());
    }

    @Test
    void shouldDeleteStudentFromGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(user.getId()).get(0);
        mockMvc.perform(delete("/api/group/deleteStudent/{userId}/{groupId}", user.getId(), group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        assertTrue(groupRepository.getGroupsByStudentId(user.getId()).stream().filter(g -> g.getId() == group.getId()).findFirst().isEmpty());
    }

    @Test
    void shouldNotDeleteStudentFromGroupIfStudentIsNotInGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.save(Group.builder().build());
        mockMvc.perform(delete("/api/group/deleteStudent/{userId}/{groupId}", user.getId(), group.getId()))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }


    @Test
    void shouldNotDeleteStudentFromGroupIfUserIsTeacherInGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByTeacherId(user.getId()).get(0);
        mockMvc.perform(delete("/api/group/deleteStudent/{userId}/{groupId}", user.getId(), group.getId()))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldNotDeleteNotExistingStudentFromGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(user.getId()).get(0);
        mockMvc.perform(delete("/api/group/deleteStudent/{userId}/{groupId}", 9999, group.getId()))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldNotDeleteStudentFromNotExistingGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        mockMvc.perform(delete("/api/group/deleteStudent/{userId}/{groupId}", user.getId(), 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldNotDeleteNotExistingStudentFromNotExistingGroup() throws Exception {
        mockMvc.perform(delete("/api/group/deleteStudent/{userId}/{groupId}", 9999, 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldAddAndDeleteTeacherFromGroup() throws Exception {
        var user1 = userRepository.findAll().get(0);
        var user2 = userRepository.findAll().get(1);
        var group = groupRepository.getGroupsByTeacherId(user1.getId()).get(0);
        groupStudentRepository.deleteAll();
        mockMvc.perform(post("/api/group/addTeacher/{userId}/{groupId}", user2.getId(), group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        mockMvc.perform(delete("/api/group/deleteTeacher/{userId}/{groupId}", user1.getId(), group.getId()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldNotDeleteTeacherFromGroupWhenIdIsFromStudent() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByStudentId(user.getId()).get(0);
        mockMvc.perform(delete("/api/group/deleteTeacher/{userId}/{groupId}", user.getId(), group.getId()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void shouldNotDeleteOnlyTeacherFromGroup() throws Exception {
        var user = userRepository.findAll().get(0);
        var group = groupRepository.getGroupsByTeacherId(user.getId()).get(0);
        mockMvc.perform(delete("/api/group/deleteTeacher/{userId}/{groupId}", user.getId(), group.getId()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void shouldArchiveGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(put("/api/group/archive/{groupId}", group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        var updatedGroup = groupRepository.findById(group.getId()).get();
        assertTrue(updatedGroup.isArchived());
    }

    @Test
    void shouldNotArchiveNotExistingGroup() throws Exception {
        mockMvc.perform(put("/api/group/archive/{groupId}", 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldUnarchiveGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(put("/api/group/archive/{groupId}", group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        mockMvc.perform(put("/api/group/unarchive/{groupId}", group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        var updatedGroup = groupRepository.findById(group.getId()).get();
        assertFalse(updatedGroup.isArchived());
    }

    @Test
    void shouldNotUnarchiveNotExistingGroup() throws Exception {
        mockMvc.perform(put("/api/group/unarchive/{groupId}", 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldChangeNameOfGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(put("/api/group/name/{groupId}", group.getId())
                        .content("test123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        var updatedGroup = groupRepository.findById(group.getId()).get();
        assertEquals("test123", updatedGroup.getName());
    }

    @Test
    void shouldNotChangeNameOfNotExistingGroup() throws Exception {
        mockMvc.perform(put("/api/group/name/{groupId}", 9999)
                        .content("test123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldChangeDescriptionOfGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(put("/api/group/description/{groupId}", group.getId())
                        .content("test123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        var updatedGroup = groupRepository.findById(group.getId()).get();
        assertEquals("test123", updatedGroup.getDescription());
    }

    @Test
    void shouldNotChangeDescriptionOfNotExistingGroup() throws Exception {
        mockMvc.perform(put("/api/group/description/{groupId}", 9999)
                        .content("test123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldChangeColorOfGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(put("/api/group/color/{groupId}", group.getId())
                        .content("1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        var updatedGroup = groupRepository.findById(group.getId()).get();
        assertEquals(1, updatedGroup.getColor());
    }

    @Test
    void shouldNotChangeColorOfNotExistingGroup() throws Exception {
        mockMvc.perform(put("/api/group/name/{groupId}", 9999)
                        .content("1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldNotChangeGroupColorWithZero() throws Exception {
        mockMvc.perform(put("/api/group/name/{groupId}", 9999)
                        .content("0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldNotChangeGroupColorWithNegativeValue() throws Exception {
        mockMvc.perform(put("/api/group/name/{groupId}", 9999)
                        .content("-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }


    @Test
    void shouldNotChangeGroupColorWithValueBiggerThan19() throws Exception {
        mockMvc.perform(put("/api/group/name/{groupId}", 9999)
                        .content("20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }
}
