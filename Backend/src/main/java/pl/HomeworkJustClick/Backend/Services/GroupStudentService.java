package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.GroupStudent;

import java.util.List;

public interface GroupStudentService {

    public List<GroupStudent> getAll();

    public GroupStudent getById(int id);

    public Boolean add(GroupStudent groupStudent);

    public Boolean delete(int id);

    public Boolean changeDescriptionById (int id, String description);
}
