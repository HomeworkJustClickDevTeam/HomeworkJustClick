package pl.HomeworkJustClick.Backend.commentfiletext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.HomeworkJustClick.Backend.BaseTestEntity;
import pl.HomeworkJustClick.Backend.comment.CommentRepository;
import pl.HomeworkJustClick.Backend.file.FileRepository;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
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
        "classpath:db/init_evaluation.sql",
        "classpath:db/init_comment.sql",
        "classpath:db/init_comment_evaluation.sql",
        "classpath:db/init_file.sql",
        "classpath:db/init_comment_file_img.sql",
        "classpath:db/init_comment_file_text.sql"
})
public class CommentFileTextControllerTest extends BaseTestEntity {
    @Autowired
    CommentFileTextRepository commentFileTextRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    FileRepository fileRepository;

    @Test
    void shouldGetAllCommentFileTexts() throws Exception {
        mockMvc.perform(get("/api/comment_file_text").param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(6))
                .andReturn();
    }

    @Test
    void shouldGetCommentFileTextById() throws Exception {
        var commentFileText = commentFileTextRepository.findAll().get(0);
        mockMvc.perform(get("/api/comment_file_text/" + commentFileText.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentFileText.getId()))
                .andExpect(jsonPath("$.highlightStart").value(commentFileText.getHighlightStart()))
                .andExpect(jsonPath("$.highlightEnd").value(commentFileText.getHighlightEnd()))
                .andExpect(jsonPath("$.color").value(commentFileText.getColor()))
                .andExpect(jsonPath("$.comment.id").value(commentFileText.getComment().getId()))
                .andExpect(jsonPath("$.file.id").value(commentFileText.getFile().getId()))
                .andReturn();
    }

    @Test
    void shouldGetCommentFileTextsByCommentId() throws Exception {
        var comment = commentRepository.findAll().get(0);
        var commentFileTextsSize = commentFileTextRepository.findCommentFileTextsByCommentId(comment.getId(), Pageable.ofSize(20)).getTotalElements();
        mockMvc.perform(get("/api/comment_file_text/byCommentId/" + comment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(commentFileTextsSize))
                .andReturn();
    }

    @Test
    void shouldGetCommentFileTextsByFileId() throws Exception {
        var file = fileRepository.findAll().get(0);
        var commentFileTextsSize = commentFileTextRepository.findCommentFileTextsByFileId(file.getId(), Pageable.ofSize(20)).getTotalElements();
        mockMvc.perform(get("/api/comment_file_text/byFileId/" + file.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(commentFileTextsSize))
                .andReturn();
    }

    @Test
    void shouldCreateCommentFileText() throws Exception {
        var commentFileTextDto = createCommentFileTextDto(100, 100, "#ffffff", commentRepository.findAll().get(0).getId(), fileRepository.findAll().get(0).getId());
        var body = objectMapper.writeValueAsString(commentFileTextDto);
        mockMvc.perform(post("/api/comment_file_text")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.highlightStart").value(commentFileTextDto.getHighlightStart()))
                .andExpect(jsonPath("$.highlightEnd").value(commentFileTextDto.getHighlightEnd()))
                .andExpect(jsonPath("$.color").value(commentFileTextDto.getColor()))
                .andExpect(jsonPath("$.comment.id").value(commentFileTextDto.getCommentId()))
                .andExpect(jsonPath("$.file.id").value(commentFileTextDto.getFileId()))
                .andReturn();
    }

    @Test
    void shouldNotCreateCommentFileText() throws Exception {
        var commentFileTextDto = createCommentFileTextDto(100, 100, "#ffffff", 999, 999);
        var body = objectMapper.writeValueAsString(commentFileTextDto);
        mockMvc.perform(post("/api/comment_file_text")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldUpdateCommentFileText() throws Exception {
        var commentFileText = commentFileTextRepository.findAll().get(0);
        var commentFileTextDto = createCommentFileTextDto(100, 100, "#ffffff", commentRepository.findAll().get(0).getId(), fileRepository.findAll().get(0).getId());
        var body = objectMapper.writeValueAsString(commentFileTextDto);
        mockMvc.perform(put("/api/comment_file_text/" + commentFileText.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.highlightStart").value(commentFileTextDto.getHighlightStart()))
                .andExpect(jsonPath("$.highlightEnd").value(commentFileTextDto.getHighlightEnd()))
                .andExpect(jsonPath("$.color").value(commentFileTextDto.getColor()))
                .andExpect(jsonPath("$.comment.id").value(commentFileTextDto.getCommentId()))
                .andExpect(jsonPath("$.file.id").value(commentFileTextDto.getFileId()))
                .andReturn();
        var updatedCommentFileText = commentFileTextRepository.findById(commentFileText.getId()).get();
        assertEquals(commentFileTextDto.getHighlightStart(), updatedCommentFileText.getHighlightStart());
        assertEquals(commentFileTextDto.getHighlightEnd(), updatedCommentFileText.getHighlightEnd());
        assertEquals(commentFileTextDto.getColor(), updatedCommentFileText.getColor());
        assertEquals(commentFileTextDto.getCommentId(), updatedCommentFileText.getComment().getId());
        assertEquals(commentFileTextDto.getFileId(), updatedCommentFileText.getFile().getId());
    }

    @Test
    void shouldNotUpdateCommentFileText() throws Exception {
        var commentFileText = commentFileTextRepository.findAll().get(0);
        var commentFileTextDto = createCommentFileTextDto(100, 100, "#ffffff", 999, 999);
        var body = objectMapper.writeValueAsString(commentFileTextDto);
        mockMvc.perform(put("/api/comment_file_text/" + commentFileText.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldUpdateAllCommentFileTextsColorsByCommentId() throws Exception {
        var newColor = "a".repeat(10);
        var commentFileText = commentFileTextRepository.findAll().get(0);
        var commentId = commentFileText.getComment().getId();
        var colorDto = CommentFileTextUpdateColorDto.builder().color(newColor).build();
        var body = objectMapper.writeValueAsString(colorDto);
        mockMvc.perform(put("/api/comment_file_text/colorByCommentId/" + commentId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
        commentFileTextRepository.findCommentFileTextsByCommentId(commentId).forEach(c -> assertEquals(newColor, c.getColor()));
    }

    @Test
    void shouldNotUpdateAllCommentFileImgsColorsByCommentIdWithInvalidColor() throws Exception {
        var newColor = "a".repeat(11);
        var commentFileText = commentFileTextRepository.findAll().get(0);
        var commentId = commentFileText.getComment().getId();
        var colorDto = CommentFileTextUpdateColorDto.builder().color(newColor).build();
        var body = objectMapper.writeValueAsString(colorDto);
        mockMvc.perform(put("/api/comment_file_text/colorByCommentId/" + commentId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
        commentFileTextRepository.findCommentFileTextsByCommentId(commentId).forEach(c -> assertNotEquals(newColor, c.getColor()));
    }

    @Test
    void shouldDeleteCommentFileText() throws Exception {
        var commentFileText = commentFileTextRepository.findAll().get(0);
        mockMvc.perform(delete("/api/comment_file_text/" + commentFileText.getId()))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(commentFileTextRepository.findById(commentFileText.getId()).isEmpty());
    }

    @Test
    void shouldMotDeleteCommentFileText() throws Exception {
        mockMvc.perform(delete("/api/comment_file_text/" + 999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldDeleteCommentFileTextsByCommentIdAndFileId() throws Exception {
        var commentFileTexts = commentFileTextRepository.findAll();
        var firstCommentFileText = commentFileTexts.get(0);
        var comment = firstCommentFileText.getComment();
        var file = firstCommentFileText.getFile();
        var secondCommentFileText = commentFileTexts.get(1);
        secondCommentFileText.setComment(comment);
        secondCommentFileText.setFile(file);
        commentFileTextRepository.save(secondCommentFileText);
        mockMvc.perform(delete("/api/comment_file_text/byCommentFile/" + comment.getId() + "/" + file.getId()))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(commentFileTexts.size() - 2, commentFileTextRepository.findAll().size());
    }

    @Test
    void shouldNotDeleteCommentFileTextsByNotExistingCommentIdAndFileId() throws Exception {
        var commentFileTexts = commentFileTextRepository.findAll();
        mockMvc.perform(delete("/api/comment_file_text/byCommentFile/999/999"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(commentFileTexts.size(), commentFileTextRepository.findAll().size());
    }

    private CommentFileTextDto createCommentFileTextDto(Integer highlightStart, Integer highlightEnd, String color, Integer commentId, Integer fileId) {
        return CommentFileTextDto.builder()
                .highlightStart(highlightStart)
                .highlightEnd(highlightEnd)
                .color(color)
                .commentId(commentId)
                .fileId(fileId)
                .build();
    }
}
