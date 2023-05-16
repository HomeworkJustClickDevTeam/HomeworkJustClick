package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.CommentEvaluation;

import java.util.List;
import java.util.Optional;

public interface CommentEvaluationService {

    public List<CommentEvaluation> getAll();

    public Optional<CommentEvaluation> getById(int id);

    public Boolean add(CommentEvaluation commentEvaluation);

    public Boolean addWithCommentAndEvaluation(int comment_id, int evaluation_id);

    public Boolean delete(int id);

    public Boolean changeDescriptionById(int id, String description);
}
