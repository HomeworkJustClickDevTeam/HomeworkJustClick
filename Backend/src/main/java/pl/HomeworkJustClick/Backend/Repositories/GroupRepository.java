package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.User;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Query(value = "select g.id, g.name, g.description from _group g join _group_teacher gt on g.id = gt.group_id where gt.user_id = :teacher_id", nativeQuery = true)
    List<Group> getGroupTeachersByTeacher(int teacher_id);
}
