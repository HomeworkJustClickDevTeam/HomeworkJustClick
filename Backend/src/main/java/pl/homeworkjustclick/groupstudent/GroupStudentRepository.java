package pl.homeworkjustclick.groupstudent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupStudentRepository extends JpaRepository<GroupStudent, Integer> {

    @Query(value = "select COUNT(1) from _group_student where user_id = :studentId and group_id = :groupId", nativeQuery = true)
    int checkForStudentInGroup(@Param("studentId") int studentId, @Param("groupId") int groupId);

    @Query(value = "select * from _group_student where user_id = :studentId and group_id = :groupId", nativeQuery = true)
    GroupStudent getGroupStudentObjectByStudentAndGroup(@Param("studentId") int studentId, @Param("groupId") int groupId);
}
