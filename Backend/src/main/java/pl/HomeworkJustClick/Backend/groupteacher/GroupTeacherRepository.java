package pl.HomeworkJustClick.Backend.groupteacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupTeacherRepository extends JpaRepository<GroupTeacher,Integer> {

    @Query(value="select COUNT(1) from _group_teacher where user_id = :teacher_id and group_id = :group_id", nativeQuery = true)
    int checkForTeacherInGroup(@Param("teacher_id") int teacher_id, @Param("group_id") int group_id);

    @Query(value="select * from _group_teacher where user_id = :teacher_id and group_id = :group_id", nativeQuery = true)
    GroupTeacher getGroupTeacherObjectByTeacherAndGroup(@Param("teacher_id") int teacher_id, @Param("group_id") int group_id);

    @Query(value = "select COUNT(1) from _group_teacher where group_id = :group_id", nativeQuery = true)
    int countTeachersInGroup(@Param("group_id") int group_id);
}
