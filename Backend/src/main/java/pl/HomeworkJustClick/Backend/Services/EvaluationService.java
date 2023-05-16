package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.Evaluation;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Responses.EvaluationResponse;

import java.util.List;
import java.util.Optional;

public interface EvaluationService {
    List<Evaluation> getAll();

    Optional<Evaluation> getById(int id);

    EvaluationResponse add(Evaluation evaluation);

    public EvaluationResponse addWithUserAndSolution(Evaluation evaluation, int user_id, int solution_id);

    Boolean delete(int id);

    Boolean changeResultById(int id, Double result);

    Boolean changeUserById(int id, int userId);

    Boolean changeSolutionById(int id, int solutionId);

    Boolean changeGradeById(int id, Double grade);

}
