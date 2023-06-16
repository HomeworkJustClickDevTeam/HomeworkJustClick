package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.Evaluation;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Responses.EvaluationResponse;
import pl.HomeworkJustClick.Backend.Responses.EvaluationResponseExtended;

import java.util.List;
import java.util.Optional;

public interface EvaluationService {
    List<EvaluationResponse> getAll();

    EvaluationResponse getById(int id);

    EvaluationResponse add(Evaluation evaluation);

    public EvaluationResponse addWithUserAndSolution(Evaluation evaluation, int user_id, int solution_id);

    List<EvaluationResponseExtended> getAllExtended();

    EvaluationResponseExtended getByIdExtended(int id);

    EvaluationResponseExtended addExtended(Evaluation evaluation);

    public EvaluationResponseExtended addWithUserAndSolutionExtended(Evaluation evaluation, int user_id, int solution_id);

    Boolean delete(int id);

    Boolean changeResultById(int id, Double result);

    Boolean changeUserById(int id, int userId);

    Boolean changeSolutionById(int id, int solutionId);

    Boolean changeGradeById(int id, Double grade);

    public List<EvaluationResponse> getAllEvaluationsByStudent(int student_id);

    public List<EvaluationResponse> getAllEvaluationsByStudentInGroup(int student_id, int group_id);

    public List<EvaluationResponse> getAllEvaluationsByAssignment(int assignment_id);

    public EvaluationResponse getEvaluationBySolution(int solution_id);

    public List<EvaluationResponseExtended> getAllEvaluationsByStudentExtended(int student_id);

    public List<EvaluationResponseExtended> getAllEvaluationsByStudentInGroupExtended(int student_id, int group_id);

    public List<EvaluationResponseExtended> getAllEvaluationsByAssignmentExtended(int assignment_id);

    public EvaluationResponseExtended getEvaluationBySolutionExtended(int solution_id);

    public Boolean checkForEvaluationToSolution(int solution_id);

}
