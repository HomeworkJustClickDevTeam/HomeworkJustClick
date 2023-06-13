package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.HomeworkJustClick.Backend.Entities.Comment;
import pl.HomeworkJustClick.Backend.Entities.CommentEvaluation;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "select * from _comment where user_id = :user_id", nativeQuery = true)
    public List<Comment> getCommentsByUser(@Param("user_id") int user_id);

    @Query(value = "select c.* from _comment c join _comment_evaluation ce on c.id = ce.comment_id where ce.evaluation_id = :evaluation_id", nativeQuery = true)
    public List<Comment> getCommentsByEvaluation(@Param("evaluation_id") int evaluation_id);

}
