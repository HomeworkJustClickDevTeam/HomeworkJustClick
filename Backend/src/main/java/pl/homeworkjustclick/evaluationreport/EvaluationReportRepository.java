package pl.homeworkjustclick.evaluationreport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EvaluationReportRepository extends JpaRepository<EvaluationReport, Integer> {
    boolean existsByEvaluationId(Integer evaluationId);
    boolean existsByEvaluationIdAndId(Integer evaluationId, Integer id);

    Optional<EvaluationReport> findByEvaluationId(Integer evaluationId);

    @Query(value = "select er.* from _evaluation_report er join _evaluation e on e.id = er.evaluation_id where e.user_id = :userId", nativeQuery = true)
    List<EvaluationReport> findAllByTeacherId(@Param("userId") Integer userId);

    @Query(value = "select er.* from _evaluation_report er join _evaluation e on er.evaluation_id = e.id join _solution s on e.solution_id = s.id where s.user_id = :userId", nativeQuery = true)
    List<EvaluationReport> findAllByStudentId(@Param("userId") Integer userId);

    @Query(value = "select er.* from _evaluation_report er join _evaluation e on e.id = er.evaluation_id where e.group_id = :groupId", nativeQuery = true)
    List<EvaluationReport> findAllByGroupId(@Param("groupId") Integer groupId);

    @Query(value = "select er.* from _evaluation_report er join _evaluation e on e.id = er.evaluation_id where e.user_id = :userId and e.group_id = :groupId", nativeQuery = true)
    List<EvaluationReport> findAllByTeacherIdAndGroupId(@Param("userId") Integer userId, @Param("groupId") Integer groupId);

    @Query(value = "select er.* from _evaluation_report er join _evaluation e on er.evaluation_id = e.id join _solution s on e.solution_id = s.id where s.user_id = :userId and s.group_id = :groupId", nativeQuery = true)
    List<EvaluationReport> findAllByStudentIdAndGroupId(@Param("userId") Integer userId, @Param("groupId") Integer groupId);
}
