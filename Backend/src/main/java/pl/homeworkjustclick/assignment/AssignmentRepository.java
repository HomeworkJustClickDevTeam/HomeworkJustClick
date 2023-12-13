package pl.homeworkjustclick.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    @Query(value = "select * from _assignment where group_id = :group_id", nativeQuery = true)
    List<Assignment> getAssignmentsByGroupId(@Param("group_id") int group_id);

    @Query(value = "select a.* from _assignment a join _group_student gs on a.group_id = gs.group_id where a.group_id = :group_id and gs.user_id = :user_id", nativeQuery = true)
    List<Assignment> getAllAssignmentsByGroupIdAndUserId(@Param("group_id") int group_id, @Param("user_id") int user_id);

    @Query(value = "select a.* from _assignment a join _solution s on a.id = s.assignment_id where s.user_id=:user_id and s.group_id=:group_id", nativeQuery = true)
    List<Assignment> getDoneAssignmentsByGroupIdAndUserId(@Param("group_id") int group_id, @Param("user_id") int user_id);

    @Query(value = "select a.* from _assignment a join _solution s on a.id = s.assignment_id where s.user_id=:user_id", nativeQuery = true)
    List<Assignment> getDoneAssignmentsByStudent(@Param("user_id") int user_id);

    @Query(value = "select a.* from _assignment a join _group_student gs on a.group_id = gs.group_id where gs.user_id = :user_id", nativeQuery = true)
    List<Assignment> getAllAssignmentsByStudent(@Param("user_id") int user_id);

    @Query(value = "select COUNT(*) from _assignment a join _solution s on a.id = s.assignment_id where a.id = :assignment_id", nativeQuery = true)
    int checkForSolutionToAssignment(@Param("assignment_id") int assignment_id);

    @Query(value = "select a.* from _assignment a join _solution s on a.id = s.assignment_id join public._evaluation e on s.id = e.solution_id where e.id = :evaluationId", nativeQuery = true)
    Optional<Assignment> findAssignmentByEvaluationId(@Param("evaluationId") Integer evaluationId);
}
