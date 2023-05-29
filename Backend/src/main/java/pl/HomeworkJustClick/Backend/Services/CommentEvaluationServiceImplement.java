package pl.HomeworkJustClick.Backend.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Comment;
import pl.HomeworkJustClick.Backend.Entities.CommentEvaluation;
import pl.HomeworkJustClick.Backend.Entities.Evaluation;
import pl.HomeworkJustClick.Backend.Repositories.CommentEvaluationRepository;
import pl.HomeworkJustClick.Backend.Repositories.CommentRepository;
import pl.HomeworkJustClick.Backend.Repositories.EvaluationRepository;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;
import pl.HomeworkJustClick.Backend.Responses.CommentResponse;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentEvaluationServiceImplement implements CommentEvaluationService {

    private final CommentRepository commentRepository;

    private final CommentEvaluationRepository commentEvaluationRepository;

    private final UserRepository userRepository;

    private final EvaluationRepository evaluationRepository;

    @Override
    public List<CommentEvaluation> getAll() {
        return commentEvaluationRepository.findAll();
    }

    @Override
    public Optional<CommentEvaluation> getById(int id) {
        return commentEvaluationRepository.findById(id);
    }

    @Override
    public Boolean add(CommentEvaluation commentEvaluation) {
        commentEvaluationRepository.save(commentEvaluation);
        return true;
    }

    @Override
    public Boolean addWithCommentAndEvaluation(int comment_id, int evaluation_id){
        Optional<Comment> commentOptional = commentRepository.findById(comment_id);
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(evaluation_id);
        if(commentOptional.isPresent() && evaluationOptional.isPresent()) {
            CommentEvaluation commentEvaluation = new CommentEvaluation(evaluationOptional.get(), commentOptional.get(), "");
            commentEvaluationRepository.save(commentEvaluation);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean delete(int id) {
        if(commentEvaluationRepository.existsById(id)) {
            commentEvaluationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean deleteFromEvaluation(int comment_id, int evaluation_id) {
        Optional<CommentEvaluation> commentEvaluationOptional = commentEvaluationRepository.getCommentEvaluationByCommentAndEvaluation(comment_id, evaluation_id);
        if(commentEvaluationOptional.isPresent()) {
            commentEvaluationRepository.deleteById(commentEvaluationOptional.get().getId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean changeDescriptionById(int id, String description) {
        Optional<CommentEvaluation> commentEvaluationOptional = commentEvaluationRepository.findById(id);
        if (commentEvaluationOptional.isPresent()) {
            CommentEvaluation commentEvaluation = commentEvaluationOptional.get();
            commentEvaluation.setDescription(description);
            commentEvaluationRepository.save(commentEvaluation);
            return true;
        } else {
            return false;
        }
    }
}
