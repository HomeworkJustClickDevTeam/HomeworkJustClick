package pl.homeworkjustclick.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;
import pl.homeworkjustclick.group.GroupRepository;
import pl.homeworkjustclick.infrastructure.auth.api.AuthenticationRequest;
import pl.homeworkjustclick.infrastructure.auth.api.ChangePasswordRequest;
import pl.homeworkjustclick.infrastructure.auth.api.RegisterRequest;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
public class UserControllerTest extends BaseTestEntity {
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;

    @Test
    void shouldGetUserById() throws Exception {
        var userToFind = userRepository.findAll().get(0);
        mockMvc.perform(get("/api/user/{id}", userToFind.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(userToFind.getId()))
                .andExpect(jsonPath("$.email").value(userToFind.getEmail()))
                .andReturn();
    }

    @Test
    void shouldNotGetUserById() throws Exception {
        mockMvc.perform(get("/api/user/{id}", 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value(3))
                .andReturn();
    }

    @Test
    void shouldGetAllTeachersByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/user/getTeachersByGroup/{id}", group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();
    }

    @Test
    void shouldNotGetAllTeachersByNotExistingGroup() throws Exception {
        mockMvc.perform(get("/api/user/getTeachersByGroup/{id}", 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldGetAllStudentsByGroup() throws Exception {
        var group = groupRepository.findAll().get(0);
        mockMvc.perform(get("/api/user/getStudentsByGroup/{id}", group.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void shouldNotGetAllStudentsByNotExistingGroup() throws Exception {
        mockMvc.perform(get("/api/user/getStudentsByGroup/{id}", 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldChangeUserPassword() throws Exception {
        var registerRequest = RegisterRequest.builder()
                .email("test@test.pl")
                .firstname("test")
                .lastname("test")
                .password("123")
                .build();
        var request = objectMapper.writeValueAsString(registerRequest);
        mockMvc.perform(post("/api/auth/register")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        var changePasswordRequest = ChangePasswordRequest.builder()
                .email("test@test.pl")
                .password("123")
                .newPassword("321")
                .build();
        var body = objectMapper.writeValueAsString(changePasswordRequest);
        mockMvc.perform(post("/api/changePassword")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        var authRequest = AuthenticationRequest.builder()
                .email("test@test.pl")
                .password("321")
                .build();
        body = objectMapper.writeValueAsString(authRequest);
        mockMvc.perform(post("/api/auth/authenticate")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    void shouldNotChangeNotExistingUserPassword() throws Exception {
        var changePasswordRequest = ChangePasswordRequest.builder()
                .email("test@test.pl")
                .password("123")
                .newPassword("321")
                .build();
        var body = objectMapper.writeValueAsString(changePasswordRequest);
        mockMvc.perform(post("/api/changePassword")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldNotChangeWithBadOldPassword() throws Exception {
        var registerRequest = RegisterRequest.builder()
                .email("test@test.pl")
                .firstname("test")
                .lastname("test")
                .password("123")
                .build();
        var request = objectMapper.writeValueAsString(registerRequest);
        mockMvc.perform(post("/api/auth/register")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        var changePasswordRequest = ChangePasswordRequest.builder()
                .email("test@test.pl")
                .password("bad")
                .newPassword("321")
                .build();
        var body = objectMapper.writeValueAsString(changePasswordRequest);
        mockMvc.perform(post("/api/changePassword")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
        var authRequest = AuthenticationRequest.builder()
                .email("test@test.pl")
                .password("123")
                .build();
        body = objectMapper.writeValueAsString(authRequest);
        mockMvc.perform(post("/api/auth/authenticate")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    void shouldRefreshToken() throws Exception {
        mockMvc.perform(post("/api/refreshToken/{id}", userRepository.findAll().get(0).getId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    void shouldNotRefreshTokenByNotExistingUser() throws Exception {
        mockMvc.perform(post("/api/refreshToken/{id}", 9999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldUpdateUser() throws Exception {
        var user = userRepository.findAll().get(0);
        user.setFirstname("test");
        user.setLastname("test");
        var body = objectMapper.writeValueAsString(user);
        mockMvc.perform(put("/api/user/{id}", user.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        var updatedUser = userRepository.findByEmail(user.getEmail()).get();
        assertEquals(user.getFirstname(), updatedUser.getFirstname());
        assertEquals(user.getLastname(), updatedUser.getLastname());
    }

    @Test
    void shouldUpdateUserIndex() throws Exception {
        var user = userRepository.findAll().get(0);
        mockMvc.perform(put("/api/user/index/{id}", user.getId())
                        .content("654321")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        var updatedUser = userRepository.findByEmail(user.getEmail()).get();
        assertEquals(654321, updatedUser.getIndex());
    }

    @Test
    void shouldNotUpdateNotExistingUserIndex() throws Exception {
        mockMvc.perform(put("/api/user/index/{id}", 9999)
                        .content("654321")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldNotUpdateWithInvalidIndex() throws Exception {
        var user = userRepository.findAll().get(0);
        mockMvc.perform(put("/api/user/index/{id}", user.getId())
                        .content("7654321")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
        var updatedUser = userRepository.findByEmail(user.getEmail()).get();
        assertNotEquals(7654321, updatedUser.getIndex());
    }

    @Test
    void shouldUpdateUserColor() throws Exception {
        var user = userRepository.findAll().get(0);
        mockMvc.perform(put("/api/user/color/{id}", user.getId())
                        .content("5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        var updatedUser = userRepository.findByEmail(user.getEmail()).get();
        assertEquals(5, updatedUser.getColor());
    }

    @Test
    void shouldNotUpdateNotExistingUserColor() throws Exception {
        mockMvc.perform(put("/api/user/color/{id}", 9999)
                        .content("654321")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldNotUpdateWithInvalidColor() throws Exception {
        var user = userRepository.findAll().get(0);
        mockMvc.perform(put("/api/user/color/{id}", user.getId())
                        .content("25")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
        var updatedUser = userRepository.findByEmail(user.getEmail()).get();
        assertNotEquals(25, updatedUser.getColor());
    }
}
