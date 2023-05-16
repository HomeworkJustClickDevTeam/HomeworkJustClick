package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.HomeworkJustClick.Backend.Entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
