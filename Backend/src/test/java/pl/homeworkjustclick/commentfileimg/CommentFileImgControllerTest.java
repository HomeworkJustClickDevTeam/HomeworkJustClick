package pl.homeworkjustclick.commentfileimg;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;
import pl.homeworkjustclick.assignment.AssignmentRepository;
import pl.homeworkjustclick.comment.CommentRepository;
import pl.homeworkjustclick.file.FileRepository;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

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
class CommentFileImgControllerTest extends BaseTestEntity {
    @Autowired
    CommentFileImgRepository commentFileImgRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    private static Stream<Arguments> prepareValidData() {
        return Stream.of(
                Arguments.of(100, 100, 100, 100, 100, 100, "#ffffff", 1, 1),
                Arguments.of(100, 100, 100, 100, 100, 100, null, 1, 1)
        );
    }

    @Test
    void shouldGetAllCommentFileImgs() throws Exception {
        mockMvc.perform(get("/api/comment_file_img").param("page", "0").param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(6))
                .andReturn();
    }

    @Test
    void shouldGetCommentFileImgById() throws Exception {
        var commentFileImg = commentFileImgRepository.findAll().get(0);
        mockMvc.perform(get("/api/comment_file_img/" + commentFileImg.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentFileImg.getId()))
                .andExpect(jsonPath("$.leftTopX").value(commentFileImg.getLeftTopX()))
                .andExpect(jsonPath("$.leftTopY").value(commentFileImg.getLeftTopY()))
                .andExpect(jsonPath("$.width").value(commentFileImg.getWidth()))
                .andExpect(jsonPath("$.height").value(commentFileImg.getHeight()))
                .andExpect(jsonPath("$.imgWidth").value(commentFileImg.getImgWidth()))
                .andExpect(jsonPath("$.imgHeight").value(commentFileImg.getImgHeight()))
                .andExpect(jsonPath("$.color").value(commentFileImg.getColor()))
                .andExpect(jsonPath("$.comment.id").value(commentFileImg.getComment().getId()))
                .andExpect(jsonPath("$.file.id").value(commentFileImg.getFile().getId()))
                .andReturn();
    }

    @Test
    void shouldGetCommentFileImgsByCommentId() throws Exception {
        var comment = commentRepository.findAll().get(0);
        var commentFileImgsSize = commentFileImgRepository.getCommentFileImgsByCommentId(comment.getId(), Pageable.ofSize(20)).getTotalElements();
        mockMvc.perform(get("/api/comment_file_img/byCommentId/" + comment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(commentFileImgsSize))
                .andReturn();
    }

    @Test
    void shouldGetCommentFileImgsByFileId() throws Exception {
        var file = fileRepository.findAll().get(1);
        var commentFileImgsSize = commentFileImgRepository.getCommentFileImgsByFileId(file.getId(), Pageable.ofSize(20)).getTotalElements();
        mockMvc.perform(get("/api/comment_file_img/byFileId/" + file.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(commentFileImgsSize))
                .andReturn();
    }

    @Test
    void shouldGetInvisibleCommentWhenGettingCommentFileImgByFile() throws Exception {
        var assignment = assignmentRepository.findAll().get(1);
        var comment = commentRepository.getCommentsByAssignmentIdAndVisible(assignment.getId(), false, Pageable.ofSize(20)).getContent().get(0);
        var commentFileImg = commentFileImgRepository.getCommentFileImgsByCommentId(comment.getId(), Pageable.ofSize(20)).getContent().get(0);
        var file = commentFileImg.getFile();
        mockMvc.perform(get("/api/comment_file_img/byFileId/" + file.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("prepareValidData")
    void shouldCreateCommentFileImg(Integer leftTopX, Integer leftTopY, Integer width, Integer height, Integer imgWidth, Integer imgHeight, String color, Integer commentId, Integer fileId) throws Exception {
        var commentFileImgDto = createCommentImgDto(leftTopX, leftTopY, width, height, imgWidth, imgHeight, color, commentId, fileId);
        var body = objectMapper.writeValueAsString(commentFileImgDto);
        mockMvc.perform(post("/api/comment_file_img")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.leftTopX").value(commentFileImgDto.getLeftTopX()))
                .andExpect(jsonPath("$.leftTopY").value(commentFileImgDto.getLeftTopY()))
                .andExpect(jsonPath("$.width").value(commentFileImgDto.getWidth()))
                .andExpect(jsonPath("$.height").value(commentFileImgDto.getHeight()))
                .andExpect(jsonPath("$.imgWidth").value(commentFileImgDto.getImgWidth()))
                .andExpect(jsonPath("$.imgHeight").value(commentFileImgDto.getImgHeight()))
                .andExpect(jsonPath("$.comment.id").value(commentFileImgDto.getCommentId()))
                .andExpect(jsonPath("$.file.id").value(commentFileImgDto.getFileId()))
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("prepareValidData")
    void shouldUpdateCommentFileImg(Integer leftTopX, Integer leftTopY, Integer width, Integer height, Integer imgWidth, Integer imgHeight, String color, Integer commentId, Integer fileId) throws Exception {
        var commentFileImg = commentFileImgRepository.findAll().get(0);
        var commentFileImgDto = createCommentImgDto(leftTopX, leftTopY, width, height, imgWidth, imgHeight, color, commentId, fileId);
        var body = objectMapper.writeValueAsString(commentFileImgDto);
        mockMvc.perform(put("/api/comment_file_img/" + commentFileImg.getId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.leftTopX").value(commentFileImgDto.getLeftTopX()))
                .andExpect(jsonPath("$.leftTopY").value(commentFileImgDto.getLeftTopY()))
                .andExpect(jsonPath("$.width").value(commentFileImgDto.getWidth()))
                .andExpect(jsonPath("$.height").value(commentFileImgDto.getHeight()))
                .andExpect(jsonPath("$.imgWidth").value(commentFileImgDto.getImgWidth()))
                .andExpect(jsonPath("$.imgHeight").value(commentFileImgDto.getImgHeight()))
                .andExpect(jsonPath("$.comment.id").value(commentFileImgDto.getCommentId()))
                .andExpect(jsonPath("$.file.id").value(commentFileImgDto.getFileId()))
                .andReturn();
        var updatedCommentFileImg = commentFileImgRepository.findById(commentFileImg.getId()).get();
        assertEquals(updatedCommentFileImg.getLeftTopX(), commentFileImgDto.getLeftTopX());
        assertEquals(updatedCommentFileImg.getLeftTopY(), commentFileImgDto.getLeftTopY());
        assertEquals(updatedCommentFileImg.getWidth(), commentFileImgDto.getWidth());
        assertEquals(updatedCommentFileImg.getHeight(), commentFileImgDto.getHeight());
        assertEquals(updatedCommentFileImg.getImgWidth(), commentFileImgDto.getImgWidth());
        assertEquals(updatedCommentFileImg.getImgHeight(), commentFileImgDto.getImgHeight());
        assertEquals(updatedCommentFileImg.getComment().getId(), commentFileImgDto.getCommentId());
        assertEquals(updatedCommentFileImg.getFile().getId(), commentFileImgDto.getFileId());
    }

    @Test
    void shouldNotUpdateNotExistingCommentFileImg() throws Exception {
        var commentFileImgDto = createCommentImgDto(1, 1, 1, 1, 1, 1, "#ffffff", 1, 1);
        var body = objectMapper.writeValueAsString(commentFileImgDto);
        mockMvc.perform(put("/api/comment_file_img/" + 999)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldUpdateAllCommentFileImgsColorsByCommentId() throws Exception {
        var newColor = "a".repeat(10);
        var commentFileImg = commentFileImgRepository.findAll().get(0);
        var commentId = commentFileImg.getComment().getId();
        var colorDto = CommentFileImgUpdateColorDto.builder().color(newColor).build();
        var body = objectMapper.writeValueAsString(colorDto);
        mockMvc.perform(put("/api/comment_file_img/colorByCommentId/" + commentId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
        commentFileImgRepository.getCommentFileImgsByCommentId(commentId).forEach(c -> assertEquals(newColor, c.getColor()));
    }

    @Test
    void shouldNotUpdateAllCommentFileImgsColorsByCommentIdWithInvalidColor() throws Exception {
        var newColor = "a".repeat(11);
        var commentFileImg = commentFileImgRepository.findAll().get(0);
        var commentId = commentFileImg.getComment().getId();
        var colorDto = CommentFileImgUpdateColorDto.builder().color(newColor).build();
        var body = objectMapper.writeValueAsString(colorDto);
        mockMvc.perform(put("/api/comment_file_img/colorByCommentId/" + commentId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError())
                .andReturn();
        commentFileImgRepository.getCommentFileImgsByCommentId(commentId).forEach(c -> assertNotEquals(newColor, c.getColor()));
    }

    @Test
    void shouldDeleteCommentFileImg() throws Exception {
        var commentFileImg = commentFileImgRepository.findAll().get(0);
        mockMvc.perform(delete("/api/comment_file_img/" + commentFileImg.getId()))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(commentFileImgRepository.findById(commentFileImg.getId()).isEmpty());
    }

    @Test
    void shouldNotDeleteCommentFileImg() throws Exception {
        mockMvc.perform(delete("/api/comment_file_img/" + 999))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void shouldDeleteCommentFileImgByCommentIdAndFileId() throws Exception {
        var commentFileImgs = commentFileImgRepository.findAll();
        var firstCommentFileImg = commentFileImgs.get(0);
        var comment = firstCommentFileImg.getComment();
        var file = firstCommentFileImg.getFile();
        var secondCommentFileImg = commentFileImgs.get(1);
        secondCommentFileImg.setComment(comment);
        secondCommentFileImg.setFile(file);
        commentFileImgRepository.save(secondCommentFileImg);
        mockMvc.perform(delete("/api/comment_file_img/byCommentFile/" + comment.getId() + "/" + file.getId()))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(commentFileImgs.size() - 2, commentFileImgRepository.findAll().size());
    }

    @Test
    void shouldNotDeleteCommentFileImgByNotExistingCommentIdAndFileId() throws Exception {
        var commentFileImgs = commentFileImgRepository.findAll();
        mockMvc.perform(delete("/api/comment_file_img/byCommentFile/999/999"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(commentFileImgs.size(), commentFileImgRepository.findAll().size());
    }

    private CommentFileImgDto createCommentImgDto(Integer leftTopX, Integer leftTopY, Integer width, Integer height, Integer imgWidth, Integer imgHeight, String color, Integer commentId, Integer fileId) {
        if (commentId == 1) {
            commentId = commentRepository.findAll().get(0).getId();
        }
        if (fileId == 1) {
            fileId = fileRepository.findAll().get(0).getId();
        }
        return CommentFileImgDto.builder()
                .leftTopX(leftTopX)
                .leftTopY(leftTopY)
                .width(width)
                .height(height)
                .imgWidth(imgWidth)
                .imgHeight(imgHeight)
                .color(color)
                .commentId(commentId)
                .fileId(fileId)
                .build();
    }
}
