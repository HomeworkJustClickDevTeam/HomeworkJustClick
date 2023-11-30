package pl.HomeworkJustClick.Backend.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> getCommentsByAssignmentIdAndVisible(Integer assignmentId, Boolean visible, Pageable pageable);

    Page<Comment> getCommentsByUserIdAndVisible(Integer userId, Boolean visible, Pageable pageable);

    Page<Comment> getCommentsByUserIdAndAssignmentIdAndVisible(Integer userId, Integer assignmentId, Boolean visible, Pageable pageable);
}
