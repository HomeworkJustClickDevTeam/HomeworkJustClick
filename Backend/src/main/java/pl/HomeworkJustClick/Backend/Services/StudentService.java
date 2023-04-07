package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.Student;

import java.util.List;

public interface StudentService {

    public List<Student> getAll();

    public Student getById(int id);

    public Boolean add(Student student);

    public Boolean update(int id, Student updatedStudent);

    public Boolean delete(int id);

    public Boolean changeIndexById(int id, int index);
}
