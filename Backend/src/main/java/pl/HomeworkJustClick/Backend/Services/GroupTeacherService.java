package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.GroupTeacher;

import java.util.List;

public interface GroupTeacherService {

    public List<GroupTeacher> getAll();

    public GroupTeacher getById(int id);

    public Boolean add(GroupTeacher groupTeacher);

    public Boolean delete(int id);

    public Boolean changeDescriptionById (int id, String description);

    public Boolean addTeacherToGroup (int group_id, int teacher_id);
}
