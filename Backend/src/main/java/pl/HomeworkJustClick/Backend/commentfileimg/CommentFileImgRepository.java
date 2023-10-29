package pl.HomeworkJustClick.Backend.commentfileimg;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentFileImgRepository extends JpaRepository<CommentFileImg, Integer> {
    Page<CommentFileImg> getCommentFileImgsByCommentId(Integer commentId, Pageable pageable);

    Page<CommentFileImg> getCommentFileImgsByFileId(Integer fileId, Pageable pageable);
}
