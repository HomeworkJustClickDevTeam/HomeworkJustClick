package pl.HomeworkJustClick.Backend.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import pl.HomeworkJustClick.Backend.BaseTestEntity;
import pl.HomeworkJustClick.Backend.infrastructure.auth.RegisterRequest;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest extends BaseTestEntity {

    @Autowired
    UserRepository userRepository;


    @Test
    public void shouldRegisterUser() throws Exception {
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
        assertNotNull(userRepository.findByEmail("test@test.pl"));
    }
}
