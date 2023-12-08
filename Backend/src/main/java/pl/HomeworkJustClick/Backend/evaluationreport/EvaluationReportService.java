package pl.HomeworkJustClick.Backend.evaluationreport;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.evaluation.EvaluationUtilsService;
import pl.HomeworkJustClick.Backend.infrastructure.exception.EntityNotFoundException;
import pl.HomeworkJustClick.Backend.infrastructure.exception.InvalidArgumentException;
import pl.HomeworkJustClick.Backend.notification.NotificationCreateService;

@Service
@RequiredArgsConstructor
public class EvaluationReportService {
    private final EvaluationReportRepository repository;
    private final EvaluationReportMapper mapper;
    private final EvaluationUtilsService evaluationUtilsService;
    private final NotificationCreateService notificationCreateService;

    public EvaluationReport findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EvaluationReport with id = " + id + " not fount"));
    }

    public EvaluationReportResponseDto getEvaluationReportByEvaluationId(Integer evaluationId) {
        return mapper.map(repository.findByEvaluationId(evaluationId));
    }

    public Slice<EvaluationReportResponseDto> getEvaluationReportsByUserId(Integer userId, Pageable pageable) {
        return repository.findAllByUserId(userId, pageable)
                .map(mapper::map);
    }

    public Slice<EvaluationReportResponseDto> getEvaluationReportsByGroupId(Integer groupId, Pageable pageable) {
        return repository.findAllByGroupId(groupId, pageable)
                .map(mapper::map);
    }

    public Slice<EvaluationReportResponseDto> getEvaluationReportsByUserIdAndGroupId(Integer userId, Integer groupId, Pageable pageable) {
        return repository.findAllByUserIdAndGroupId(userId, groupId, pageable)
                .map(mapper::map);
    }

    @Transactional
    public EvaluationReportResponseDto createEvaluationReport(EvaluationReportDto evaluationReportDto) {
        if (repository.existsByEvaluationId(evaluationReportDto.getEvaluationId())) {
            throw new InvalidArgumentException("EvaluationReport with id = " + evaluationReportDto.getEvaluationId() + " already exists");
        }
        var evaluationReport = mapper.map(evaluationReportDto);
        setRelationFields(evaluationReport, evaluationReportDto);
        var response = mapper.map(repository.save(evaluationReport));
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
        return mapper.map(repository.save(evaluationReport));
    }

    public void deleteEvaluationReport(Integer id) {
        var evaluationReport = findById(id);
        repository.delete(evaluationReport);
    }

    private void setRelationFields(EvaluationReport evaluationReport, EvaluationReportDto evaluationReportDto) {
        evaluationReport.setEvaluation(evaluationUtilsService.findById(evaluationReportDto.getEvaluationId()));
    }
}
