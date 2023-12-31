package pl.homeworkjustclick.evaluation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {

    @Query(value = "select e.* from _evaluation e join _solution s on e.solution_id = s.id where s.assignment_id = :assignmentId", nativeQuery = true)
    List<Evaluation> getEvaluationsByAssignment(@Param("assignmentId") int assignmentId);

    @Query(value = "select COUNT(e.*) from _evaluation e join _solution s on e.solution_id = s.id where s.assignment_id = :assignmentId", nativeQuery = true)
    int countEvaluationsByAssignment(@Param("assignmentId") int assignmentId);

    @Query(value = "select e.* from _evaluation e join _solution s on e.solution_id = s.id where s.user_id = :studentId", nativeQuery = true)
    List<Evaluation> getAllEvaluationsByStudent(@Param("studentId") int studentId);

    @Query(value = "select e.* from _evaluation e join _solution s on e.solution_id = s.id where s.user_id = :studentId and e.group_id = :groupId", nativeQuery = true)
    List<Evaluation> getAllEvaluationsByStudentInGroup(@Param("studentId") int studentId, @Param("groupId") int groupId);

    @Query(value = "select e.* from _evaluation e join _solution s on e.solution_id = s.id where s.id = :solutionId", nativeQuery = true)
    Optional<Evaluation> getEvaluationBySolution(@Param("solutionId") int solutionId);

    @Query(value = "select count(e.*) from _evaluation e join _solution s on e.solution_id = s.id where s.id = :solutionId", nativeQuery = true)
    int checkForEvaluationToSolution(@Param("solutionId") int solutionId);

    Boolean existsBySolutionId(Integer solutionId);

    Boolean existsBySolutionIdAndId(Integer solutionId, Integer id);

    @Query(value = "select e.* from _evaluation e join _evaluation_report er on e.id = er.evaluation_id where e.user_id = :userId", nativeQuery = true)
    List<Evaluation> findReportedEvaluationsByUserId(@Param("userId") Integer userId);

    @Query(value = "select e.* from _evaluation e join _evaluation_report er on e.id = er.evaluation_id where e.group_id = :groupId", nativeQuery = true)
    List<Evaluation> findReportedEvaluationsByGroupId(@Param("groupId") Integer groupId);

    @Query(value = "select e.* from _evaluation e join _evaluation_report er on e.id = er.evaluation_id where e.user_id = :userId and e.group_id = :groupId", nativeQuery = true)
    List<Evaluation> findReportedEvaluationsByUserIdAndGroupId(@Param("userId") Integer userId, @Param("groupId") Integer groupId);

    List<Evaluation> findAllByGroupId(Integer groupId);

    @Query(value = "select _evaluation.* from _evaluation join _solution on _evaluation.solution_id = _solution.id join _assignment on _solution.assignment_id = _assignment.id where _solution.user_id = :userId and _assignment.id = :assignmentId", nativeQuery = true)
    Optional<Evaluation> findEvaluationByStudentAndAssignment(@Param("userId") Integer userId, @Param("assignmentId") Integer assignmentId);
}