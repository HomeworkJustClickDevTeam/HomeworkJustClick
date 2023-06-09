package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.Assignment;
import pl.HomeworkJustClick.Backend.Responses.AssignmentResponse;
import pl.HomeworkJustClick.Backend.Responses.AssignmentResponseExtended;

import java.time.OffsetDateTime;
import java.util.List;

public interface AssignmentService {

    public List<AssignmentResponse> getAll();

    public List<AssignmentResponseExtended> getAllExtended();

    public AssignmentResponse getById(int id);

    public AssignmentResponseExtended getByIdExtended(int id);
    public AssignmentResponse add(Assignment assignment);

    public AssignmentResponse addWithUserAndGroup(Assignment assignment, int user_id, int group_id);

    public Boolean delete(int id);

    public Boolean update(int id, Assignment updatedAssignment);

    public Boolean changeTitleById(int id, String title);

    public Boolean changeTaskDescriptionById(int id, String taskDescription);

    public Boolean changeCompletionDatetime(int id, OffsetDateTime completionDatetime);

    public Boolean changeVisibility(int id, Boolean visible);

    public Boolean changeUser(int id, int userId);

    public Boolean changeGroup(int id, int groupId);

    public Boolean changeMaxPoints(int id, int points);

    public List<AssignmentResponse> getAssignmentsByGroupId(int id);

    public List<AssignmentResponse> getUncheckedAssignmentsByGroup(int group_id);

    public List<AssignmentResponse> getAllAssignmentsByGroupIdAndUserId(int group_id, int user_id);

    public List<AssignmentResponse> getUndoneAssignmentsByGroupIdAndUserId(int group_id, int user_id);

    public List<AssignmentResponse> getDoneAssignmentsByGroupIdAndUserId(int group_id, int user_id);

    public List<AssignmentResponse> getExpiredUndoneAssignmentsByGroupIdAndUserId(int group_id, int user_id);

    public List<AssignmentResponse> getExpiredUndoneAssignmentsByStudent(int student_id);

    public List<AssignmentResponse> getNonExpiredUndoneAssignmentsByGroupIdAndUserId(int group_id, int user_id);

    public List<AssignmentResponse> getNonExpiredUndoneAssignmentsByStudent(int student_id);

    public List<AssignmentResponse> getAllAssignmentsByStudent(int user_id);

    public List<AssignmentResponse> getUndoneAssignmentsByStudent(int student_id);

    public List<AssignmentResponse> getDoneAssignmentsByStudent(int student_id);

    public List<AssignmentResponseExtended> getAssignmentsByGroupIdExtended(int id);

    public List<AssignmentResponseExtended> getUncheckedAssignmentsByGroupExtended(int group_id);

    public List<AssignmentResponseExtended> getAllAssignmentsByGroupIdAndUserIdExtended(int group_id, int user_id);

    public List<AssignmentResponseExtended> getUndoneAssignmentsByGroupIdAndUserIdExtended(int group_id, int user_id);

    public List<AssignmentResponseExtended> getDoneAssignmentsByGroupIdAndUserIdExtended(int group_id, int user_id);

    public List<AssignmentResponseExtended> getExpiredUndoneAssignmentsByGroupIdAndUserIdExtended(int group_id, int user_id);

    public List<AssignmentResponseExtended> getExpiredUndoneAssignmentsByStudentExtended(int student_id);

    public List<AssignmentResponseExtended> getNonExpiredUndoneAssignmentsByGroupIdAndUserIdExtended(int group_id, int user_id);

    public List<AssignmentResponseExtended> getNonExpiredUndoneAssignmentsByStudentExtended(int student_id);

    public List<AssignmentResponseExtended> getAllAssignmentsByStudentExtended(int user_id);

    public List<AssignmentResponseExtended> getUndoneAssignmentsByStudentExtended(int student_id);

    public List<AssignmentResponseExtended> getDoneAssignmentsByStudentExtended(int student_id);

}
