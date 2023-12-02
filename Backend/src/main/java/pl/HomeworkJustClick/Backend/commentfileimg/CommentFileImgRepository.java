package pl.HomeworkJustClick.Backend.commentfileimg;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentFileImgRepository extends JpaRepository<CommentFileImg, Integer> {
    Page<CommentFileImg> getCommentFileImgsByCommentId(Integer commentId, Pageable pageable);

    List<CommentFileImg> getCommentFileImgsByCommentId(Integer commentId);

    Page<CommentFileImg> getCommentFileImgsByFileId(Integer fileId, Pageable pageable);

    List<CommentFileImg> getCommentFileImgsByCommentIdAndFileId(Integer commentId, Integer fileId);
}
