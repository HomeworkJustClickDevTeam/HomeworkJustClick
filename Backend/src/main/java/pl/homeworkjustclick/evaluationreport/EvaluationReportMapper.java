package pl.homeworkjustclick.evaluationreport;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.homeworkjustclick.evaluation.EvaluationMapper;
import pl.homeworkjustclick.solution.Solution;
import pl.homeworkjustclick.solution.SolutionMapper;

@Component
@RequiredArgsConstructor
public class EvaluationReportMapper {
    private final EvaluationMapper evaluationMapper;
    private final SolutionMapper solutionMapper;

    EvaluationReport map(EvaluationReportDto evaluationReportDto) {
        return EvaluationReport.builder()
                .comment(evaluationReportDto.getComment())
                .build();
    }

    EvaluationReportResponseDto map(EvaluationReport evaluationReport, Solution solution) {
        return EvaluationReportResponseDto.builder()
                .id(evaluationReport.getId())
                .comment(evaluationReport.getComment())
                .evaluation(evaluationMapper.map(evaluationReport.getEvaluation()))
                .solution(solutionMapper.mapExtended(solution))
                .build();
    }

    void map(EvaluationReport target, EvaluationReportDto source) {
        target.setComment(source.getComment());
    }
}
