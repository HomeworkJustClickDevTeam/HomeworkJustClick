package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.HomeworkJustClick.Backend.Entities.Group;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Query(value = "select g.* from _group g join _group_teacher gt on g.id = gt.group_id where gt.user_id = :teacher_id", nativeQuery = true)
    List<Group> getGroupsByTeacherId(int teacher_id);

    @Query(value = "select g.* from _group g join _group_student gs on g.id = gs.group_id where gs.user_id = :student_id", nativeQuery = true)
    List<Group> getGroupsByStudentId(int student_id);
}
