package pl.homeworkjustclick.evaluationreport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EvaluationReportRepository extends JpaRepository<EvaluationReport, Integer> {
    boolean existsByEvaluationId(Integer evaluationId);
    boolean existsByEvaluationIdAndId(Integer evaluationId, Integer id);

    EvaluationReport findByEvaluationId(Integer evaluationId);

    @Query(value = "select er.* from _evaluation_report er join _evaluation e on e.id = er.evaluation_id where e.user_id = :userId", nativeQuery = true)
    Page<EvaluationReport> findAllByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query(value = "select er.* from _evaluation_report er join _evaluation e on e.id = er.evaluation_id where e.group_id = :groupId", nativeQuery = true)
    Page<EvaluationReport> findAllByGroupId(@Param("groupId") Integer groupId, Pageable pageable);

    @Query(value = "select er.* from _evaluation_report er join _evaluation e on e.id = er.evaluation_id where e.user_id = :userId and e.group_id = :groupId", nativeQuery = true)
    Page<EvaluationReport> findAllByUserIdAndGroupId(@Param("userId") Integer userId, @Param("groupId") Integer groupId, Pageable pageable);
}
