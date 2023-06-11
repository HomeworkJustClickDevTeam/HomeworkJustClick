package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.GroupStudent;

import java.util.List;
import java.util.Optional;

public interface GroupStudentService {

    public List<GroupStudent> getAll();

    public Optional<GroupStudent> getById(int id);

    public Boolean add(GroupStudent groupStudent);

    public Boolean delete(int id);

    public Boolean changeDescriptionById (int id, String description);

    public Boolean addStudentToGroup (int group_id, int student_id);

    public Boolean deleteStudentFromGroup (int group_id, int student_id);

    public Boolean checkForStudentInGroup(int student_id, int group_id);
}
