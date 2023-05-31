package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.Assignment;
import pl.HomeworkJustClick.Backend.Responses.AssignmentResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface AssignmentService {

    public List<Assignment> getAll();
    public Optional<Assignment> getById(int id);
    public AssignmentResponse add(Assignment assignment);

    public AssignmentResponse addWithUserAndGroup(Assignment assignment, int user_id, int group_id);

    public Boolean delete(int id);

    public Boolean changeTitleById(int id, String title);

    public Boolean changeTaskDescriptionById(int id, String taskDescription);

    public Boolean changeCompletionDatetime(int id, OffsetDateTime completionDatetime);

    public Boolean changeVisibility(int id, Boolean visible);

    public Boolean changeUser(int id, int userId);

    public Boolean changeGroup(int id, int groupId);

    public List<AssignmentResponse> getAssignmentsByGroupId(int id);

    public List<AssignmentResponse> getUncheckedAssignmentsByGroup(int group_id);

    public List<Assignment> getAllAssignmentsByGroupIdAndUserId(int group_id, int user_id);

    public List<Assignment> getUndoneAssignmentsByGroupIdAndUserId(int group_id, int user_id);

    public List<Assignment> getDoneAssignmentsByGroupIdAndUserId(int group_id, int user_id);

    public List<Assignment> getExpiredUndoneAssignmentsByGroupIdAndUserId(int group_id, int user_id);

    public List<Assignment> getExpiredUndoneAssignmentsByStudent(int student_id);

    public List<Assignment> getNonExpiredUndoneAssignmentsByGroupIdAndUserId(int group_id, int user_id);

    public List<Assignment> getNonExpiredUndoneAssignmentsByStudent(int student_id);

    public List<Assignment> getAllAssignmentsByStudent(int user_id);

    public List<Assignment> getUndoneAssignmentsByStudent(int student_id);

    public List<Assignment> getDoneAssignmentsByStudent(int student_id);

}
