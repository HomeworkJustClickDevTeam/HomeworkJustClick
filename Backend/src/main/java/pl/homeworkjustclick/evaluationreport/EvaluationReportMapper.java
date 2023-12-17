package pl.homeworkjustclick.evaluationreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.homeworkjustclick.assignment.Assignment;
import pl.homeworkjustclick.assignment.AssignmentMapper;
import pl.homeworkjustclick.evaluation.EvaluationMapper;

@Component
@RequiredArgsConstructor
public class EvaluationReportMapper {
    private final EvaluationMapper evaluationMapper;
    private final AssignmentMapper assignmentMapper;

    EvaluationReport map(EvaluationReportDto evaluationReportDto) {
        return EvaluationReport.builder()
                .comment(evaluationReportDto.getComment())
                .build();
    }

    EvaluationReportResponseDto map(EvaluationReport evaluationReport, Assignment assignment) {
        return EvaluationReportResponseDto.builder()
                .id(evaluationReport.getId())
                .comment(evaluationReport.getComment())
                .evaluation(evaluationMapper.map(evaluationReport.getEvaluation()))
                .assignment(assignmentMapper.map(assignment))
                .build();
    }

    void map(EvaluationReport target, EvaluationReportDto source) {
        target.setComment(source.getComment());
    }
}
