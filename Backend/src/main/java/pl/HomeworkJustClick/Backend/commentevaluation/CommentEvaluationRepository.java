package pl.HomeworkJustClick.Backend.commentevaluation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentEvaluationRepository extends JpaRepository<CommentEvaluation, Integer> {

    @Query(value = "select * from _comment_evaluation where comment_id = :comment_id and evaluation_id = :evaluation_id", nativeQuery = true)
    Optional<CommentEvaluation> getCommentEvaluationByCommentAndEvaluation(@Param("comment_id") int comment_id, @Param("evaluation_id") int evaluation_id);
}
