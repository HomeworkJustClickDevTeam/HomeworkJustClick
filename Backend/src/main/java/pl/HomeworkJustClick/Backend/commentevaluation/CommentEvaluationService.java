package pl.HomeworkJustClick.Backend.commentevaluation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.comment.Comment;
import pl.HomeworkJustClick.Backend.comment.CommentRepository;
import pl.HomeworkJustClick.Backend.evaluation.Evaluation;
import pl.HomeworkJustClick.Backend.evaluation.EvaluationRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentEvaluationService {

    private final CommentRepository commentRepository;

    private final CommentEvaluationRepository commentEvaluationRepository;

    private final EvaluationRepository evaluationRepository;

    public List<CommentEvaluation> getAll() {
        return commentEvaluationRepository.findAll();
    }

    public Optional<CommentEvaluation> getById(int id) {
        return commentEvaluationRepository.findById(id);
    }

    @Transactional
    public Boolean add(CommentEvaluation commentEvaluation) {
        commentEvaluationRepository.save(commentEvaluation);
        return true;
    }

    @Transactional
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

    @Transactional
    public Boolean delete(int id) {
        if(commentEvaluationRepository.existsById(id)) {
            commentEvaluationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Boolean deleteFromEvaluation(int comment_id, int evaluation_id) {
        Optional<CommentEvaluation> commentEvaluationOptional = commentEvaluationRepository.getCommentEvaluationByCommentAndEvaluation(comment_id, evaluation_id);
        if(commentEvaluationOptional.isPresent()) {
            commentEvaluationRepository.deleteById(commentEvaluationOptional.get().getId());
            return true;
        } else {
            return false;
        }
    }
}
