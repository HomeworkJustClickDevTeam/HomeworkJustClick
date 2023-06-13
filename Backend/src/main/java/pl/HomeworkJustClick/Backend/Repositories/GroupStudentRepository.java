package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.HomeworkJustClick.Backend.Entities.GroupStudent;

@Repository
public interface GroupStudentRepository extends JpaRepository<GroupStudent,Integer> {

    @Query(value="select COUNT(1) from _group_student where user_id = :student_id and group_id = :group_id", nativeQuery = true)
    int checkForStudentInGroup(@Param("student_id") int student_id, @Param("group_id") int group_id);

    @Query(value="select * from _group_student where user_id = :student_id and group_id = :group_id", nativeQuery = true)
    GroupStudent getGroupStudentObjectByStudentAndGroup(@Param("student_id") int student_id, @Param("group_id") int group_id);
}
