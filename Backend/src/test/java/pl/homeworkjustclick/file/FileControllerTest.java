package pl.homeworkjustclick.file;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;
import pl.homeworkjustclick.assignment.AssignmentRepository;
import pl.homeworkjustclick.solution.SolutionRepository;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        "classpath:db/init_file.sql"
})
public class FileControllerTest extends BaseTestEntity {
    @Autowired
    FileRepository fileRepository;
    @Autowired
    AssignmentRepository assignmentRepository;
    @Autowired
    SolutionRepository solutionRepository;

    @Test
    void shouldGetAllFiles() throws Exception {
        mockMvc.perform(get("/api/files"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(12))
                .andReturn();
    }

    @Test
    void shouldGetFileById() throws Exception {
        var file = fileRepository.findAll().get(0);
        mockMvc.perform(get("/api/file/{id}", file.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(file.getId()))
                .andExpect(jsonPath("$.name").value(file.getName()))
                .andExpect(jsonPath("$.format").value(file.getFormat()))
                .andExpect(jsonPath("$.mongoId").value(file.getMongoId()))
                .andReturn();
    }

    @Test
    void shouldNotGetNotExistingFileById() throws Exception {
        mockMvc.perform(get("/api/file/{id}", 9999))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetFilesBySolution() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        mockMvc.perform(get("/api/files/bySolution/{id}", solution.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();
    }

    @Test
    void shouldNotGetFilesBySolution() throws Exception {
        var solution = solutionRepository.findAll().get(0);
        fileRepository.deleteAll();
        mockMvc.perform(get("/api/files/bySolution/{id}", solution.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldGetFilesByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        fileRepository.save(File.builder().assignment(assignment).build());
        mockMvc.perform(get("/api/files/byAssignment/{id}", assignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();
    }

    @Test
    void shouldNotGetFilesByAssignment() throws Exception {
        var assignment = assignmentRepository.findAll().get(0);
        mockMvc.perform(get("/api/files/byAssignment/{id}", assignment.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void shouldCreateFileWithSolution() throws Exception {
        var size = fileRepository.findAll().size();
        var solution = solutionRepository.findAll().get(0);
        var file = File.builder().name("name").format("jpg").mongoId("123abc").build();
        var body = objectMapper.writeValueAsString(file);
        mockMvc.perform(post("/api/file/withSolution/{id}", solution.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(file.getName()))
                .andExpect(jsonPath("$.format").value(file.getFormat()))
                .andExpect(jsonPath("$.mongoId").value(file.getMongoId()))
                .andReturn();
        assertEquals(size + 1, fileRepository.findAll().size());
    }

    @Test
    void shouldNotCreateFileWithNotExistingSolution() throws Exception {
        var size = fileRepository.findAll().size();
        var file = File.builder().name("name").format("jpg").mongoId("123abc").build();
        var body = objectMapper.writeValueAsString(file);
        mockMvc.perform(post("/api/file/withSolution/{id}", 9999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(size, fileRepository.findAll().size());
    }

    @Test
    void shouldCreateFileWithAssignment() throws Exception {
        var size = fileRepository.findAll().size();
        var assignment = assignmentRepository.findAll().get(0);
        var file = File.builder().name("name").format("jpg").mongoId("123abc").build();
        var body = objectMapper.writeValueAsString(file);
        mockMvc.perform(post("/api/file/withAssignment/{id}", assignment.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(file.getName()))
                .andExpect(jsonPath("$.format").value(file.getFormat()))
                .andExpect(jsonPath("$.mongoId").value(file.getMongoId()))
                .andReturn();
        assertEquals(size + 1, fileRepository.findAll().size());
    }

    @Test
    void shouldNotCreateFileWithNotExistingAssignment() throws Exception {
        var size = fileRepository.findAll().size();
        var file = File.builder().name("name").format("jpg").mongoId("123abc").build();
        var body = objectMapper.writeValueAsString(file);
        mockMvc.perform(post("/api/file/withAssignment/{id}", 9999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(size, fileRepository.findAll().size());
    }

    @Test
    void shouldDeleteFile() throws Exception {
        var size = fileRepository.findAll().size();
        var file = fileRepository.findAll().get(0);
        mockMvc.perform(delete("/api/file/{id}", file.getId()))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(size - 1, fileRepository.findAll().size());
        assertFalse(fileRepository.existsById(file.getId()));
    }

    @Test
    void shouldNotDeleteNotExistingFile() throws Exception {
        var size = fileRepository.findAll().size();
        mockMvc.perform(delete("/api/file/{id}", 9999))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(size, fileRepository.findAll().size());
    }
}
