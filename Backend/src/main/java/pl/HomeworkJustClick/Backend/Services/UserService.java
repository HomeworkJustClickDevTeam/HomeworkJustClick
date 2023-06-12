package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<User> getAll();

    public Optional<User> getById(int id);

    Optional<User> getByEmail(String email);

    public Boolean add(User user);

    public Boolean update(int id, User updatedUser);

    public Boolean delete(int id);

    public Boolean changeIndexById(int id, int index);

    public List<User> getTeachersByGroup(int group_id);

    public List<User> getStudentsByGroup(int group_id);

    public Boolean changeColorById(int id, int color);
}
