package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.GroupTeacher;

import java.util.List;

public interface GroupService {
    public List<Group> getAll();

    public Group getById(int id);

    public Boolean add(Group group);

    public Boolean delete(int id);

    public Boolean changeNameById(int id, String name);

    public Boolean addWithTeacher(Group group, GroupTeacher groupTeacher);

    public List<Group> getGroupsByTeacher(int teacher_id);

    public List<Group> getGroupsByStudent(int student_id);
}
