package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.HomeworkJustClick.Backend.Entities.GroupTeacher;
import pl.HomeworkJustClick.Backend.Entities.User;

import java.util.List;

@Repository
public interface GroupTeacherRepository extends JpaRepository<GroupTeacher,Integer> {

    @Query(value="select COUNT(1) from _group_teacher where user_id = :teacher_id and group_id = :group_id", nativeQuery = true)
    int getGroupTeacherByTeacherAndGroup(int teacher_id, int group_id);

    @Query(value="select * from _group_teacher where user_id = :teacher_id and group_id = :group_id", nativeQuery = true)
    GroupTeacher getGroupTeacherObjectByTeacherAndGroup(int teacher_id, int group_id);

    @Query(value = "select COUNT(1) from _group_teacher where group_id = :id", nativeQuery = true)
    int countTeachersInGroup(int id);
}
