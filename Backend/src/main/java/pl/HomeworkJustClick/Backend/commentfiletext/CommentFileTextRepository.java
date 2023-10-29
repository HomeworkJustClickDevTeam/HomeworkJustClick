package pl.HomeworkJustClick.Backend.commentfiletext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentFileTextRepository extends JpaRepository<CommentFileText, Integer> {
    Page<CommentFileText> findCommentFileTextsByCommentId(Integer commentId, Pageable pageable);

    Page<CommentFileText> findCommentFileTextsByFileId(Integer fileId, Pageable pageable);
}
