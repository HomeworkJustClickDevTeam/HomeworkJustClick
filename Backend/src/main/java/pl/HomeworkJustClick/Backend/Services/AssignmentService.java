package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.Assignment;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Responses.AssignmentResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
}
