package pl.homeworkjustclick.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;
import pl.homeworkjustclick.infrastructure.auth.api.AuthenticationRequest;
import pl.homeworkjustclick.infrastructure.auth.api.AuthenticationResponseDto;
import pl.homeworkjustclick.infrastructure.auth.api.RegisterRequest;
import pl.homeworkjustclick.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {
        "classpath:db/cleanup_all.sql"
})
class AuthenticationControllerTest extends BaseTestEntity {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        var registerRequest = RegisterRequest.builder()
                .email("test@test.pl")
                .firstname("test")
                .lastname("test")
                .password("12345678")
                .build();
        var request = objectMapper.writeValueAsString(registerRequest);
        mockMvc.perform(post("/api/auth/register")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        assertNotNull(userRepository.findByEmail("test@test.pl"));
    }

    @Test
    void shouldNotRegisterUserWithDuplicatedEmail() throws Exception {
        var registerRequest = RegisterRequest.builder()
                .email("test@test.pl")
                .firstname("test")
                .lastname("test")
                .password("12345678")
                .build();
        var request = objectMapper.writeValueAsString(registerRequest);
        mockMvc.perform(post("/api/auth/register")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        assertNotNull(userRepository.findByEmail("test@test.pl"));
        mockMvc.perform(post("/api/auth/register")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldNotRegisterWithInvalidEmail() throws Exception {
        var registerRequest = RegisterRequest.builder()
                .email("test")
                .firstname("test")
                .lastname("test")
                .password("12345678")
                .build();
        var request = objectMapper.writeValueAsString(registerRequest);
        mockMvc.perform(post("/api/auth/register")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldRegisterAndAuthenticate() throws Exception {
        var registerRequest = RegisterRequest.builder()
                .email("test@test.pl")
                .firstname("test")
                .lastname("test")
                .password("12345678")
                .build();
        var request = objectMapper.writeValueAsString(registerRequest);
        mockMvc.perform(post("/api/auth/register")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        assertNotNull(userRepository.findByEmail("test@test.pl"));
        var authRequest = AuthenticationRequest.builder()
                .email("test@test.pl")
                .password("12345678")
                .build();
        request = objectMapper.writeValueAsString(authRequest);
        mockMvc.perform(post("/api/auth/authenticate")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();
    }

    @Test
    void shouldNotAuthenticateWithInvalidPassword() throws Exception {
        var registerRequest = RegisterRequest.builder()
                .email("test@test.pl")
                .firstname("test")
                .lastname("test")
                .password("12345678")
                .build();
        var request = objectMapper.writeValueAsString(registerRequest);
        mockMvc.perform(post("/api/auth/register")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        assertNotNull(userRepository.findByEmail("test@test.pl"));
        var authRequest = AuthenticationRequest.builder()
                .email("test@test.pl")
                .password("87654321")
                .build();
        request = objectMapper.writeValueAsString(authRequest);
        mockMvc.perform(post("/api/auth/authenticate")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldNotAuthenticateWithInvalidEmail() throws Exception {
        var registerRequest = RegisterRequest.builder()
                .email("test@test.pl")
                .firstname("test")
                .lastname("test")
                .password("12345678")
                .build();
        var request = objectMapper.writeValueAsString(registerRequest);
        mockMvc.perform(post("/api/auth/register")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        assertNotNull(userRepository.findByEmail("test@test.pl"));
        var authRequest = AuthenticationRequest.builder()
                .email("test1@test.pl")
                .password("87654321")
                .build();
        request = objectMapper.writeValueAsString(authRequest);
        mockMvc.perform(post("/api/auth/authenticate")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldCheckValidToken() throws Exception {
        var registerRequest = RegisterRequest.builder()
                .email("test@test.pl")
                .firstname("test")
                .lastname("test")
                .password("12345678")
                .build();
        var request = objectMapper.writeValueAsString(registerRequest);
        var response = mockMvc.perform(post("/api/auth/register")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        var authResponseDto = objectMapper.readValue(response.getResponse().getContentAsString(), AuthenticationResponseDto.class);
        var token = authResponseDto.getToken();
        var headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);
        mockMvc.perform(post("/api/auth/checkToken")
                        .headers(headers))
                .andExpect(status().is2xxSuccessful());
    }
}
