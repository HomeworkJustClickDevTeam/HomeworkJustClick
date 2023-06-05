package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.Solution;
import pl.HomeworkJustClick.Backend.Responses.SolutionResponse;

import java.util.List;
import java.util.Optional;

public interface SolutionService {

    List<SolutionResponse> getAll();

    SolutionResponse getById(int id);

    SolutionResponse add(Solution solution);

    SolutionResponse addWithUserAndAssignment(Solution solution, int user_id, int assignment_id);

    Boolean delete(int id);

    Boolean changeUserById(int id, int userId);

    Boolean changeAssignmentById(int id, int assignmentId);

    List<SolutionResponse> getSolutionsByGroupId(int id);

    List<SolutionResponse> getSolutionsByAssignmentId(int id);

    List<SolutionResponse> getLateSolutionsByGroup(int group_id);

    List<SolutionResponse> getLateSolutionsByUserAndGroup(int user_id, int group_id);

    List<SolutionResponse> getLateSolutionsByAssignment(int assignment_id);

    List<SolutionResponse> getLateSolutionsByStudent(int user_id);

    List<SolutionResponse> getUncheckedSolutionsByGroup(int group_id);

    List<SolutionResponse> getCheckedSolutionsByGroup(int group_id);

    List<SolutionResponse> getUncheckedSolutionsByStudent(int student_id);

    List<SolutionResponse> getCheckedSolutionsByStudent(int student_id);

    List<SolutionResponse> getUncheckedSolutionsByStudentAndGroup(int student_id, int group_id);

    List<SolutionResponse> getCheckedSolutionsByStudentAndGroup(int student_id, int group_id);

    List<SolutionResponse> getUncheckedSolutionsByAssignment(int assignment_id);

    List<SolutionResponse> getCheckedSolutionsByAssignment(int assignment_id);

    List<SolutionResponse> getUncheckedSolutionsByTeacher(int teacher_id);

    List<SolutionResponse> getCheckedSolutionsByTeacher(int teacher_id);
}
