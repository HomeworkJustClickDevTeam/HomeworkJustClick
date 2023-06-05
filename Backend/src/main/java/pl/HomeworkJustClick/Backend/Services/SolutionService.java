package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.Solution;
import pl.HomeworkJustClick.Backend.Responses.SolutionResponse;

import java.util.List;
import java.util.Optional;

public interface SolutionService {

    List<SolutionResponse> getAll();

    SolutionResponse getById(int id);

    SolutionResponse add(Solution solution);

    public SolutionResponse addWithUserAndAssignment(Solution solution, int user_id, int assignment_id);

    Boolean delete(int id);

    Boolean changeUserById(int id, int userId);

    Boolean changeAssignmentById(int id, int assignmentId);

    public List<SolutionResponse> getSolutionsByGroupId(int id);

    public List<SolutionResponse> getSolutionsByAssignmentId(int id);

    public List<SolutionResponse> getLateSolutionsByGroup(int group_id);

    public List<SolutionResponse> getLateSolutionsByUserAndGroup(int user_id, int group_id);

    public List<SolutionResponse> getLateSolutionsByAssignment(int assignment_id);

    public List<SolutionResponse> getLateSolutionsByStudent(int user_id);

}
