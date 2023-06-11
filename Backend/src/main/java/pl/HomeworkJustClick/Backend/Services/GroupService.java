package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.GroupTeacher;
import pl.HomeworkJustClick.Backend.Responses.GroupResponse;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    public List<Group> getAll();

    public Optional<Group> getById(int id);

    public GroupResponse add(Group group);

    public Boolean delete(int id);

    public Boolean changeNameById(int id, String name);

    public Boolean changeDescriptionById(int id, String description);

    public Boolean changeColorById(int id, int color);

    public int archiveGroup(int id);

    public int unarchiveGroup(int id);

    public Boolean addWithTeacher(Group group, GroupTeacher groupTeacher);

    public List<Group> getGroupsByTeacher(int teacher_id);

    public List<Group> getGroupsByStudent(int student_id);
}
