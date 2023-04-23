package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.User;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Query(value = "select g.* from _group g join _group_teacher gt on g.id = gt.group_id where gt.user_id = :teacher_id", nativeQuery = true)
    List<Group> getGroupTeachersByTeacher(int teacher_id);

    @Query(value = "select g.* from _group g join _group_student gs on g.id = gs.group_id where gs.user_id = :student_id", nativeQuery = true)
    List<Group> getGroupStudentsByStudent(int student_id);
}
