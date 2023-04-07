package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.Teacher;

import java.util.List;

public interface TeacherService {

    public List<Teacher> getAll();

    public Teacher getById(int id);

    public Boolean add(Teacher teacher);

    public Boolean update(int id, Teacher updatedTeacher);

    public Boolean delete(int id);

    public Boolean changeTitleById(int id, String title);
}
