package pl.homeworkjustclick.commentfiletext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentFileTextRepository extends JpaRepository<CommentFileText, Integer> {
    Page<CommentFileText> findCommentFileTextsByCommentId(Integer commentId, Pageable pageable);

    List<CommentFileText> findCommentFileTextsByCommentId(Integer commentId);

    Page<CommentFileText> findCommentFileTextsByFileId(Integer fileId, Pageable pageable);

    List<CommentFileText> findCommentFileTextsByCommentIdAndFileId(Integer commentId, Integer fileId);
}
