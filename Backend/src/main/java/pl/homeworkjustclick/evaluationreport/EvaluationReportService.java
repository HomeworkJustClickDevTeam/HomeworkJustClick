package pl.homeworkjustclick.evaluationreport;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.evaluation.EvaluationUtilsService;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;
import pl.homeworkjustclick.infrastructure.exception.InvalidArgumentException;
import pl.homeworkjustclick.notification.NotificationCreateService;
import pl.homeworkjustclick.solution.SolutionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationReportService {
    private final EvaluationReportRepository repository;
    private final EvaluationReportMapper mapper;
    private final EvaluationUtilsService evaluationUtilsService;
    private final NotificationCreateService notificationCreateService;
    private final SolutionService solutionService;

    public EvaluationReport findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EvaluationReport with id = " + id + " not found"));
    }

    public EvaluationReportResponseDto getEvaluationReportByEvaluationId(Integer evaluationId) {
        return mapper.map(findById(evaluationId), solutionService.findByEvaluationId(evaluationId));
    }

    public List<EvaluationReportResponseDto> getEvaluationReportsByTeacherId(Integer userId) {
        return repository.findAllByTeacherId(userId)
                .stream()
                .map(evaluationReport -> mapper.map(evaluationReport, solutionService.findByEvaluationId(evaluationReport.getEvaluation().getId())))
                .toList();
    }

    public List<EvaluationReportResponseDto> getEvaluationReportsByStudentId(Integer userId) {
        return repository.findAllByStudentId(userId)
                .stream()
                .map(evaluationReport -> mapper.map(evaluationReport, solutionService.findByEvaluationId(evaluationReport.getEvaluation().getId())))
                .toList();
    }

    public List<EvaluationReportResponseDto> getEvaluationReportsByGroupId(Integer groupId) {
        return repository.findAllByGroupId(groupId)
                .stream()
                .map(evaluationReport -> mapper.map(evaluationReport, solutionService.findByEvaluationId(evaluationReport.getEvaluation().getId())))
                .toList();
    }

    public List<EvaluationReportResponseDto> getEvaluationReportsByTeacherIdAndGroupId(Integer userId, Integer groupId) {
        return repository.findAllByTeacherIdAndGroupId(userId, groupId)
                .stream()
                .map(evaluationReport -> mapper.map(evaluationReport, solutionService.findByEvaluationId(evaluationReport.getEvaluation().getId())))
                .toList();
    }

    public List<EvaluationReportResponseDto> getEvaluationReportsByStudentIdAndGroupId(Integer userId, Integer groupId) {
        return repository.findAllByStudentIdAndGroupId(userId, groupId)
                .stream()
                .map(evaluationReport -> mapper.map(evaluationReport, solutionService.findByEvaluationId(evaluationReport.getEvaluation().getId())))
                .toList();
    }

    @Transactional
    public EvaluationReportResponseDto createEvaluationReport(EvaluationReportDto evaluationReportDto) {
        if (repository.existsByEvaluationId(evaluationReportDto.getEvaluationId())) {
            throw new InvalidArgumentException("EvaluationReport with id = " + evaluationReportDto.getEvaluationId() + " already exists");
        }
        var evaluationReport = mapper.map(evaluationReportDto);
        setRelationFields(evaluationReport, evaluationReportDto);
        var response = mapper.map(repository.save(evaluationReport), solutionService.findByEvaluationId(evaluationReport.getEvaluation().getId()));
        notificationCreateService.createEvaluationReportNotification(evaluationReport.getEvaluation().getUser(), evaluationReport.getEvaluation().getSolution().getAssignment(), evaluationReport.getEvaluation().getSolution().getUser());
        return response;
    }

    @Transactional
    public EvaluationReportResponseDto updateEvaluationReport(Integer id, EvaluationReportDto evaluationReportDto) {
        var evaluationReport = findById(id);
        if (repository.existsByEvaluationId(evaluationReportDto.getEvaluationId()) && !repository.existsByEvaluationIdAndId(evaluationReportDto.getEvaluationId(), id)) {
            throw new InvalidArgumentException("EvaluationReport with id = " + evaluationReportDto.getEvaluationId() + " already exists");
        }
        mapper.map(evaluationReport, evaluationReportDto);
        setRelationFields(evaluationReport, evaluationReportDto);
        return mapper.map(repository.save(evaluationReport), solutionService.findByEvaluationId(evaluationReport.getEvaluation().getId()));
    }

    public void deleteEvaluationReport(Integer id) {
        var evaluationReport = findById(id);
        repository.delete(evaluationReport);
    }

    private void setRelationFields(EvaluationReport evaluationReport, EvaluationReportDto evaluationReportDto) {
        evaluationReport.setEvaluation(evaluationUtilsService.findById(evaluationReportDto.getEvaluationId()));
    }
}
