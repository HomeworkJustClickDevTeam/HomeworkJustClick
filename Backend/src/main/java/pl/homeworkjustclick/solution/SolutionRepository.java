package pl.homeworkjustclick.solution;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Integer> {

    @Query(value = "select * from _solution where group_id = :groupId", nativeQuery = true)
    List<Solution> getSolutionsByGroupId(@Param("groupId") int groupId);

    @EntityGraph(attributePaths = "user")
    List<Solution> findAllByAssignmentId(Integer assignmentId);

    @Query(value = "select COUNT(*) from _solution where assignment_id = :assignment_id", nativeQuery = true)
    int countSolutionsByAssignmentId(@Param("assignment_id") int assignment_id);

    @Query(value = "select * from _solution where group_id = :groupId and user_id = :userId", nativeQuery = true)
    List<Solution> getSolutionsByUserAndGroup(@Param("userId") int userId, @Param("groupId") int groupId);

    @Query(value = "select * from _solution where user_id = :userId", nativeQuery = true)
    List<Solution> getSolutionsByUser(@Param("userId") int userId);

    @Query(value = "select * from _solution where user_id = :userId and assignment_id = :assignmentId", nativeQuery = true)
    Optional<Solution> getSolutionByUserAndAssignment(@Param("userId") int userId, @Param("assignmentId") int assignmentId);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is null and s.group_id = :groupId", nativeQuery = true)
    List<Solution> getUncheckedSolutionsByGroup(@Param("groupId") int groupId);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is null and s.user_id = :studentId", nativeQuery = true)
    List<Solution> getUncheckedSolutionsByStudent(@Param("studentId") int studentId);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is null and s.user_id = :studentId and s.group_id = :groupId", nativeQuery = true)
    List<Solution> getUncheckedSolutionsByStudentAndGroup(@Param("studentId") int studentId, @Param("groupId") int groupId);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is null and s.assignment_id = :assignmentId", nativeQuery = true)
    List<Solution> getUncheckedSolutionsByAssignment(@Param("assignmentId") int assignmentId);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id join _group_teacher gt on s.group_id = gt.group_id where e.id is null and gt.user_id = :teacherId", nativeQuery = true)
    List<Solution> getUncheckedSolutionsByTeacher(@Param("teacherId") int teacherId);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is not null and s.group_id = :groupId", nativeQuery = true)
    List<Solution> getCheckedSolutionsByGroup(@Param("groupId") int groupId);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is not null and s.user_id = :studentId", nativeQuery = true)
    List<Solution> getCheckedSolutionsByStudent(@Param("studentId") int studentId);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is not null and s.user_id = :studentId and s.group_id = :groupId", nativeQuery = true)
    List<Solution> getCheckedSolutionsByStudentAndGroup(@Param("studentId") int studentId, @Param("groupId") int groupId);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is not null and s.assignment_id = :assignmentId", nativeQuery = true)
    List<Solution> getCheckedSolutionsByAssignment(@Param("assignmentId") int assignmentId);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id join _group_teacher gt on s.group_id = gt.group_id where e.id is not null and gt.user_id = :teacher_id", nativeQuery = true)
    List<Solution> getCheckedSolutionsByTeacher(@Param("teacher_id") int teacher_id);

    @Query(value = "select COUNT(*) from _solution s join _evaluation e on s.id = e.solution_id where s.id = :solutionId", nativeQuery = true)
    int checkForEvaluationToSolution(@Param("solutionId") int solutionId);

    @Query(value = "SELECT s.* FROM _solution s LEFT JOIN _evaluation e on s.id = e.solution_id WHERE s.assignment_id = :assignmentId AND s.user_id = :userId AND s.group_id = :groupId AND e.id IS NOT NULL", nativeQuery = true)
    Optional<Solution> getCheckedSolutionByUserAssignmentGroup(@Param("assignmentId") int assignmentId, @Param("userId") int userId, @Param("groupId") int groupId);

    @Query(value = "SELECT s.* FROM _solution s LEFT JOIN _evaluation e on s.id = e.solution_id WHERE s.assignment_id = :assignmentId AND s.user_id = :userId AND s.group_id = :groupId AND e.id IS NULL", nativeQuery = true)
    Optional<Solution> getUncheckedSolutionByUserAssignmentGroup(@Param("assignmentId") int assignmentId, @Param("userId") int userId, @Param("groupId") int groupId);

    @Query(value = "select s.* from _solution s join _evaluation e on s.id = e.solution_id where e.id = :evaluationId", nativeQuery = true)
    Optional<Solution> getSolutionByEvaluationId(@Param("evaluationId") Integer evaluationId);
}
