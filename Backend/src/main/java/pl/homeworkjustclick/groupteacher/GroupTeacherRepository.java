package pl.homeworkjustclick.groupteacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupTeacherRepository extends JpaRepository<GroupTeacher, Integer> {

    @Query(value = "select COUNT(1) from _group_teacher where user_id = :teacherId and group_id = :groupId", nativeQuery = true)
    int checkForTeacherInGroup(@Param("teacherId") int teacherId, @Param("groupId") int groupId);

    @Query(value = "select * from _group_teacher where user_id = :teacherId and group_id = :groupId", nativeQuery = true)
    GroupTeacher getGroupTeacherObjectByTeacherAndGroup(@Param("teacherId") int teacherId, @Param("groupId") int groupId);

    @Query(value = "select COUNT(1) from _group_teacher where group_id = :groupId", nativeQuery = true)
    int countTeachersInGroup(@Param("groupId") int groupId);
}
