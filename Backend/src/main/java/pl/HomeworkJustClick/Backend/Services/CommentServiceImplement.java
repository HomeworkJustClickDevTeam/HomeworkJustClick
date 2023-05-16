package pl.HomeworkJustClick.Backend.Services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Comment;
import pl.HomeworkJustClick.Backend.Entities.CommentEvaluation;
import pl.HomeworkJustClick.Backend.Entities.Evaluation;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Repositories.CommentEvaluationRepository;
import pl.HomeworkJustClick.Backend.Repositories.CommentRepository;
import pl.HomeworkJustClick.Backend.Repositories.EvaluationRepository;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;
import pl.HomeworkJustClick.Backend.Responses.CommentResponse;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImplement implements CommentService {

    EntityManager entityManager;
    public CommentServiceImplement(EntityManager entityManager){this.entityManager = entityManager;}

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentEvaluationRepository commentEvaluationRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private EvaluationRepository evaluationRepository;

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public Optional<Comment> getById(int id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> getCommentsByUser (int user_id) {
        return commentRepository.getCommentsByUser(user_id);
    }

    @Override
    public List<Comment> getCommentsByEvaluation (int evaluation_id) {
        return commentRepository.getCommentsByEvaluation(evaluation_id);
    }

    @Transactional
    @Override
    public CommentResponse addWithUser(Comment comment, int user_id) {
        Optional<User> user = userRepository.findById(user_id);
        if(user.isPresent()) {
            comment.setUser(user.get());
            entityManager.persist(comment);
            return CommentResponse.builder()
                    .id(comment.getId())
                    .title(comment.getTitle())
                    .description(comment.getDescription())
                    .user_id(comment.getUser().getId())
                    .build();
        } else {
            return CommentResponse.builder().build();
        }
    }

    @Override
    public Boolean delete (int id) {
        if(commentRepository.existsById(id)){
            commentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
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

    @Override
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
