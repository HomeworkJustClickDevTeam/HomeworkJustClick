package pl.homeworkjustclick.commentevaluation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentEvaluationRepository extends JpaRepository<CommentEvaluation, Integer> {
    Page<CommentEvaluation> getCommentEvaluationsByCommentId(Integer commentId, Pageable pageable);

    Page<CommentEvaluation> getCommentEvaluationsByEvaluationId(Integer evaluationId, Pageable pageable);
}