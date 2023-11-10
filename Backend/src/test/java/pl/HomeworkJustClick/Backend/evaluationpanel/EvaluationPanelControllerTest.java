package pl.HomeworkJustClick.Backend.evaluationpanel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.HomeworkJustClick.Backend.BaseTestEntity;
import pl.HomeworkJustClick.Backend.evaluationbutton.EvaluationButtonDto;
import pl.HomeworkJustClick.Backend.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
        "classpath:db/init_evaluation_panel.sql"
})
public class EvaluationPanelControllerTest extends BaseTestEntity {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EvaluationPanelRepository evaluationPanelRepository;

    private static Stream<Arguments> prepareValidData() {
        return Stream.of(
                Arguments.of("test 1", 3, 1, List.of(0.0, 1.0, 2.0, 3.0)),
                Arguments.of("test 1", 3, 1, List.of(0.5, 1.5, 2.5, 3.5, 4.5))
        );
    }

    private static Stream<Arguments> prepareInvalidData() {
        return Stream.of(
                Arguments.of("", 3, 1, List.of(0.0, 1.0, 2.0, 3.0)),
                Arguments.of(null, 3, 1, List.of(0.0, 1.0, 2.0, 3.0)),
                Arguments.of("test 1", 0, 1, List.of(0.5, 1.5, 2.5, 3.5, 4.5)),
                Arguments.of("test 1", 6, 1, List.of(0.5, 1.5, 2.5, 3.5, 4.5)),
                Arguments.of("test 1", null, 1, List.of(0.5, 1.5, 2.5, 3.5, 4.5)),
                Arguments.of("test 1", 3, 999, List.of(0.5, 1.5, 2.5, 3.5, 4.5)),
                Arguments.of("test 1", 3, null, List.of(0.5, 1.5, 2.5, 3.5, 4.5)),
                Arguments.of("test 1", 3, 1, List.of(-0.5, -1.5, -2.5, -3.5, -4.5)),
                Arguments.of("test 1", 3, 1, List.of())
        );
    }

    @Test
    void shouldGetAllEvaluationPanelsByUserId() throws Exception {
        var userId = userRepository.findByEmail("jan_kowalski@gmail.com").get().getId();
        mockMvc.perform(get("/api/evaluation_panel/byUserId/" + userId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(1))
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("prepareValidData")
    void shouldCreateEvaluationPanel(String name, Integer width, Integer userId, List<Double> buttons) throws Exception {
        var evaluationPanelDto = createEvaluationPanelDto(name, width, userId, buttons);
        var body = objectMapper.writeValueAsString(evaluationPanelDto);
        mockMvc.perform(post("/api/evaluation_panel")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.width").value(width))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buttons.length()").value(buttons.size()));
    }

    @ParameterizedTest
    @MethodSource("prepareInvalidData")
    void shouldNotCreateEvaluationPanel(String name, Integer width, Integer userId, List<Double> buttons) throws Exception {
        var evaluationPanelDto = createEvaluationPanelDto(name, width, userId, buttons);
        var body = objectMapper.writeValueAsString(evaluationPanelDto);
        mockMvc.perform(post("/api/evaluation_panel")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError());
    }

    @ParameterizedTest
    @MethodSource("prepareValidData")
    void shouldUpdateEvaluationPanel(String name, Integer width, Integer userId, List<Double> buttons) throws Exception {
        var evaluationPanelId = evaluationPanelRepository.findAll().get(0).getId();
        var evaluationPanelDto = createEvaluationPanelDto(name, width, userId, buttons);
        var body = objectMapper.writeValueAsString(evaluationPanelDto);
        mockMvc.perform(put("/api/evaluation_panel/" + evaluationPanelId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.width").value(width))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buttons.length()").value(buttons.size()));
    }

    @ParameterizedTest
    @MethodSource("prepareInvalidData")
    void shouldNotUpdateEvaluationPanel(String name, Integer width, Integer userId, List<Double> buttons) throws Exception {
        var evaluationPanelId = evaluationPanelRepository.findAll().get(0).getId();
        var evaluationPanelDto = createEvaluationPanelDto(name, width, userId, buttons);
        var body = objectMapper.writeValueAsString(evaluationPanelDto);
        mockMvc.perform(put("/api/evaluation_panel/" + evaluationPanelId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldNotUpdateNotExistingEvaluationPanel() throws Exception {
        var evaluationPanelDto = createEvaluationPanelDto("test", 3, userRepository.findAll().get(0).getId(), List.of(1.0, 2.0, 3.0));
        var body = objectMapper.writeValueAsString(evaluationPanelDto);
        mockMvc.perform(put("/api/evaluation_panel/" + 999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteEvaluationPanel() throws Exception {
        var evaluationPanelId = evaluationPanelRepository.findAll().get(0).getId();
        mockMvc.perform(delete("/api/evaluation_panel/" + evaluationPanelId))
                .andExpect(status().isOk())
                .andReturn();
        assertFalse(evaluationPanelRepository.existsById(evaluationPanelId));
    }

    @Test
    void shouldNotDeleteNotExistingEvaluationPanel() throws Exception {
        mockMvc.perform(delete("/api/evaluation_panel/" + 999))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private EvaluationPanelDto createEvaluationPanelDto(String name, Integer width, Integer userId, List<Double> buttons) {
        if (userId != null && userId == 1) {
            userId = userRepository.findAll().get(0).getId();
        }
        var buttonsList = new ArrayList<EvaluationButtonDto>();
        buttons.forEach(button -> {
            buttonsList.add(EvaluationButtonDto.builder().points(button).build());
        });
        var evaluationPanelDto = new EvaluationPanelDto();
        evaluationPanelDto.setName(name);
        evaluationPanelDto.setWidth(width);
        evaluationPanelDto.setUserId(userId);
        evaluationPanelDto.setButtons(buttonsList);
        return evaluationPanelDto;
    }
}
