package pl.HomeworkJustClick.Backend.evaluationreport;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationReportRepository extends JpaRepository<EvaluationReport, Integer> {
    boolean existsByEvaluationId(Integer evaluationId);

    boolean existsByEvaluationIdAndId(Integer evaluationId, Integer id);
}
