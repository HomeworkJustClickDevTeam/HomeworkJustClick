package pl.homeworkjustclick.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    @Query(value = "select * from _assignment where group_id = :groupId", nativeQuery = true)
    List<Assignment> getAssignmentsByGroupId(@Param("groupId") int groupId);

    @Query(value = "select a.* from _assignment a join _group_student gs on a.group_id = gs.group_id where a.group_id = :groupId and gs.user_id = :userId", nativeQuery = true)
    List<Assignment> getAllAssignmentsByGroupIdAndUserId(@Param("groupId") int groupId, @Param("userId") int userId);

    @Query(value = "select a.* from _assignment a join _solution s on a.id = s.assignment_id where s.user_id=:userId and s.group_id=:groupId", nativeQuery = true)
    List<Assignment> getDoneAssignmentsByGroupIdAndUserId(@Param("groupId") int groupId, @Param("userId") int userId);

    @Query(value = "select a.* from _assignment a join _solution s on a.id = s.assignment_id where s.user_id=:userId", nativeQuery = true)
    List<Assignment> getDoneAssignmentsByStudent(@Param("userId") int userId);

    @Query(value = "select a.* from _assignment a join _group_student gs on a.group_id = gs.group_id where gs.user_id = :userId", nativeQuery = true)
    List<Assignment> getAllAssignmentsByStudent(@Param("userId") int userId);

    @Query(value = "select COUNT(*) from _assignment a join _solution s on a.id = s.assignment_id where a.id = :assignmentId", nativeQuery = true)
    int checkForSolutionToAssignment(@Param("assignmentId") int assignmentId);
}
