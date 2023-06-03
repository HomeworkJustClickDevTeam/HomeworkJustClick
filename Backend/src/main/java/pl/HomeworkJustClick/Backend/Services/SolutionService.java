package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.Solution;
import pl.HomeworkJustClick.Backend.Responses.SolutionResponse;

import java.util.List;
import java.util.Optional;

public interface SolutionService {

    List<Solution> getAll();

    Optional<Solution> getById(int id);

    SolutionResponse add(Solution solution);

    public SolutionResponse addWithUserAndAssignment(Solution solution, int user_id, int assignment_id);

    Boolean delete(int id);

    Boolean changeUserById(int id, int userId);

    Boolean changeAssignmentById(int id, int assignmentId);

    public List<SolutionResponse> getSolutionsByGroupId(int id);

    public List<SolutionResponse> getSolutionsByAssignmentId(int id);

    public List<Solution> getLateSolutionsByGroup(int group_id);

    public List<Solution> getLateSolutionsByUserAndGroup(int user_id, int group_id);

    public List<Solution> getLateSolutionsByAssignment(int assignment_id);

    public List<Solution> getLateSolutionsByStudent(int user_id);

}
