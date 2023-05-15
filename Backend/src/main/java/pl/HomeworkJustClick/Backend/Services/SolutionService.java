package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.Solution;
import pl.HomeworkJustClick.Backend.Responses.SolutionResponse;

import java.util.List;
import java.util.Optional;

public interface SolutionService {
    List<Solution> getAll();
    Optional<Solution> getById(int id);
    SolutionResponse add(Solution solution);
    Boolean delete(int id);
    Boolean changeUserById(int id, int userId);
    Boolean changeAssignmentById(int id, int assignmentId);

}
