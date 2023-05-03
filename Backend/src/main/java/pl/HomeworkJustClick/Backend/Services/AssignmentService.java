package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.Assignment;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Responses.AssignmentResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

public interface AssignmentService {

    public List<Assignment> getAll();
    public Assignment getById(int id);
    public Boolean add(Assignment assignment);
    public Boolean delete(int id);
    public Boolean changeTitleById(int id, String title);
    public Boolean changeTaskDescriptionById(int id, String taskDescription);
    public Boolean changeCompletionDatetime(int id, OffsetDateTime completionDatetime);






}
