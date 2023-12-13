package pl.homeworkjustclick.evaluation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {

    @Query(value = "select e.* from _evaluation e join _solution s on e.solution_id = s.id where s.assignment_id = :assignment_id", nativeQuery = true)
    List<Evaluation> getEvaluationsByAssignment(@Param("assignment_id") int assignment_id);

    @Query(value = "select COUNT(e.*) from _evaluation e join _solution s on e.solution_id = s.id where s.assignment_id = :assignment_id", nativeQuery = true)
    int countEvaluationsByAssignment(@Param("assignment_id") int assignment_id);

    @Query(value = "select e.* from _evaluation e join _solution s on e.solution_id = s.id where s.user_id = :student_id", nativeQuery = true)
    List<Evaluation> getAllEvaluationsByStudent(@Param("student_id") int student_id);

    @Query(value = "select e.* from _evaluation e join _solution s on e.solution_id = s.id where s.user_id = :student_id and e.group_id = :group_id", nativeQuery = true)
    List<Evaluation> getAllEvaluationsByStudentInGroup(@Param("student_id") int student_id, @Param("group_id") int group_id);

    @Query(value = "select e.* from _evaluation e join _solution s on e.solution_id = s.id where s.id = :solution_id", nativeQuery = true)
    Optional<Evaluation> getEvaluationBySolution(@Param("solution_id") int solution_id);

    @Query(value = "select count(e.*) from _evaluation e join _solution s on e.solution_id = s.id where s.id = :solution_id", nativeQuery = true)
    int checkForEvaluationToSolution(@Param("solution_id") int solution_id);

    Boolean existsBySolutionId(Integer solutionId);

    Boolean existsBySolutionIdAndId(Integer solutionId, Integer id);

    @Query(value = "select e.* from _evaluation e join _evaluation_report er on e.id = er.evaluation_id where e.user_id = :userId", nativeQuery = true)
    List<Evaluation> findReportedEvaluationsByUserId(@Param("userId") Integer userId);

    @Query(value = "select e.* from _evaluation e join _evaluation_report er on e.id = er.evaluation_id where e.group_id = :groupId", nativeQuery = true)
    List<Evaluation> findReportedEvaluationsByGroupId(@Param("groupId") Integer groupId);

    @Query(value = "select e.* from _evaluation e join _evaluation_report er on e.id = er.evaluation_id where e.user_id = :userId and e.group_id = :groupId", nativeQuery = true)
    List<Evaluation> findReportedEvaluationsByUserIdAndGroupId(@Param("userId") Integer userId, @Param("groupId") Integer groupId);
}