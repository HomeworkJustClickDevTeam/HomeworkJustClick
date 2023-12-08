package pl.homeworkjustclick.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        "classpath:db/init_file.sql",
        "classpath:db/init_comment_file_img.sql",
        "classpath:db/init_comment_file_text.sql"
})
public class CommentUtilsServiceTest extends BaseTestEntity {
    @Autowired
    CommentUtilsService commentUtilsService;
    @Autowired
    CommentRepository commentRepository;

    @Test
    void shouldUpdateComment() {
        // given
        var comment = commentRepository.findAll().get(0);
        var commentId = comment.getId();
        // when
        commentUtilsService.update(commentId);
        // then
        var updatedComment = commentRepository.findById(commentId).get();
        assertEquals(comment.getCounter() + 1, updatedComment.getCounter());
        assertTrue(comment.getLastUsedDate().isBefore(updatedComment.getLastUsedDate()));
    }
}
