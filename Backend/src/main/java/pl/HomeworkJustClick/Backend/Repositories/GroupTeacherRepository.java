package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.HomeworkJustClick.Backend.Entities.GroupTeacher;

import java.util.List;

public interface GroupTeacherRepository extends JpaRepository<GroupTeacher,Integer> {

    @Query(value="select COUNT(1) from _group_teacher where user_id = :teacher_id and group_id = :group_id", nativeQuery = true)
    int getGroupTeacherByTeacherAndGroup(int teacher_id, int group_id);
}
