package pl.homeworkjustclick.file;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import pl.homeworkjustclick.infrastructure.BaseTestEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FileControllerTest extends BaseTestEntity {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    FileRepository fileRepository;

    @BeforeEach
    public void setUp() throws IOException {
        Mockito.when(postgresClientService.checkToken(any())).thenReturn(Boolean.TRUE);
        var file = new File("src/test/java/pl/HomeworkJustClick/HomeworkJustClick/file/lorem_lines.txt");
        var inputStream = new FileInputStream(file);
        var multipartFile = new MockMultipartFile("file", "lorem_lines.txt", "txt", inputStream);
        pl.homeworkjustclick.file.File _file = new pl.homeworkjustclick.file.File("lorem_lines.txt", "txt");
        _file.setFile(new Binary(BsonBinarySubType.BINARY, multipartFile.getBytes()));
        fileRepository.save(_file);
    }

    @Test
    void shouldGetFile() throws Exception {
        var id = fileRepository.findAll().get(0).getId();
        mockMvc.perform(get("/api/file/" + id)
                        .headers(HttpHeaders.writableHttpHeaders(createHttpHeaders("aaa"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void shouldAddFile() throws Exception {
        var file = new File("src/test/java/pl/HomeworkJustClick/HomeworkJustClick/file/lorem_lines.txt");
        var inputStream = new FileInputStream(file);
        var multipartFile = new MockMultipartFile("file", "lorem_lines.txt", "txt", inputStream);
        var res = mockMvc.perform(multipart("/api/file")
                        .file(multipartFile)
                        .headers(HttpHeaders.writableHttpHeaders(createHttpHeaders("aaa"))))
                .andExpect(status().isOk())
                .andReturn();
        var content = res.getResponse().getContentAsString();
        var fileResponseDto = objectMapper.readValue(content, FileResponseDto.class);
        assertTrue(fileRepository.findById(fileResponseDto.getId()).isPresent());
    }

    @Test
    void shouldDeleteFile() throws Exception {
        var id = fileRepository.findAll().get(0).getId();
        assertTrue(fileRepository.findById(id).isPresent());
        mockMvc.perform(delete("/api/file/" + id)
                        .headers(HttpHeaders.writableHttpHeaders(createHttpHeaders("aaa"))))
                .andExpect(status().isOk())
                .andReturn();
        assertFalse(fileRepository.findById(id).isPresent());
    }
}
