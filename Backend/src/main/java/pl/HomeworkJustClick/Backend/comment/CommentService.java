package pl.HomeworkJustClick.Backend.comment;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;
import pl.HomeworkJustClick.Backend.user.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final EntityManager entityManager;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getById(int id) {
        return commentRepository.findById(id);
    }

    public List<Comment> getCommentsByUser (int user_id) {
        return commentRepository.getCommentsByUser(user_id);
    }

    public List<Comment> getCommentsByEvaluation (int evaluation_id) {
        return commentRepository.getCommentsByEvaluation(evaluation_id);
    }

    @Transactional
    public CommentResponseDto addWithUser(Comment comment, int user_id) {
        Optional<User> user = userRepository.findById(user_id);
        if(user.isPresent()) {
            comment.setUser(user.get());
            entityManager.persist(comment);
            return CommentResponseDto.builder()
                    .id(comment.getId())
                    .title(comment.getTitle())
                    .description(comment.getDescription())
                    .user_id(comment.getUser().getId())
                    .build();
        } else {
            return CommentResponseDto.builder().build();
        }
    }

    @Transactional
    public Boolean delete (int id) {
        if(commentRepository.existsById(id)){
            commentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Boolean changeTitleById (int id, String title) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            comment.setTitle(title);
            commentRepository.save(comment);
            return true;
        } else {
            return false;
        }
    }

    public Boolean changeDescriptionById (int id, String description) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            comment.setTitle(description);
            commentRepository.save(comment);
            return true;
        } else {
            return false;
        }
    }
}
