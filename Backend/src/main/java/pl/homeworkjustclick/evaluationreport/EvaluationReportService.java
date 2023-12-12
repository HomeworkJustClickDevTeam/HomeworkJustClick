package pl.homeworkjustclick.evaluationreport;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.assignment.AssignmentService;
import pl.homeworkjustclick.evaluation.EvaluationUtilsService;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;
import pl.homeworkjustclick.infrastructure.exception.InvalidArgumentException;
import pl.homeworkjustclick.notification.NotificationCreateService;

@Service
@RequiredArgsConstructor
public class EvaluationReportService {
    private final EvaluationReportRepository repository;
    private final EvaluationReportMapper mapper;
    private final EvaluationUtilsService evaluationUtilsService;
    private final NotificationCreateService notificationCreateService;
    private final AssignmentService assignmentService;

    public EvaluationReport findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EvaluationReport with id = " + id + " not fount"));
    }

    public EvaluationReportResponseDto getEvaluationReportByEvaluationId(Integer evaluationId) {
        return mapper.map(repository.findByEvaluationId(evaluationId), assignmentService.findByEvaluationId(evaluationId));
    }

    public Slice<EvaluationReportResponseDto> getEvaluationReportsByTeacherId(Integer userId, Pageable pageable) {
        return repository.findAllByTeacherId(userId, pageable)
                .map(evaluationReport -> mapper.map(evaluationReport, assignmentService.findByEvaluationId(evaluationReport.getEvaluation().getId())));
    }

    public Slice<EvaluationReportResponseDto> getEvaluationReportsByStudentId(Integer userId, Pageable pageable) {
        return repository.findAllByStudentId(userId, pageable)
                .map(evaluationReport -> mapper.map(evaluationReport, assignmentService.findByEvaluationId(evaluationReport.getEvaluation().getId())));
    }

    public Slice<EvaluationReportResponseDto> getEvaluationReportsByGroupId(Integer groupId, Pageable pageable) {
        return repository.findAllByGroupId(groupId, pageable)
                .map(evaluationReport -> mapper.map(evaluationReport, assignmentService.findByEvaluationId(evaluationReport.getEvaluation().getId())));
    }

    public Slice<EvaluationReportResponseDto> getEvaluationReportsByTeacherIdAndGroupId(Integer userId, Integer groupId, Pageable pageable) {
        return repository.findAllByTeacherIdAndGroupId(userId, groupId, pageable)
                .map(evaluationReport -> mapper.map(evaluationReport, assignmentService.findByEvaluationId(evaluationReport.getEvaluation().getId())));
    }

    public Slice<EvaluationReportResponseDto> getEvaluationReportsByStudentIdAndGroupId(Integer userId, Integer groupId, Pageable pageable) {
        return repository.findAllByStudentIdAndGroupId(userId, groupId, pageable)
                .map(evaluationReport -> mapper.map(evaluationReport, assignmentService.findByEvaluationId(evaluationReport.getEvaluation().getId())));
    }

    @Transactional
    public EvaluationReportResponseDto createEvaluationReport(EvaluationReportDto evaluationReportDto) {
        if (repository.existsByEvaluationId(evaluationReportDto.getEvaluationId())) {
            throw new InvalidArgumentException("EvaluationReport with id = " + evaluationReportDto.getEvaluationId() + " already exists");
        }
        var evaluationReport = mapper.map(evaluationReportDto);
        setRelationFields(evaluationReport, evaluationReportDto);
        var response = mapper.map(repository.save(evaluationReport), assignmentService.findByEvaluationId(evaluationReport.getEvaluation().getId()));
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
        return mapper.map(repository.save(evaluationReport), assignmentService.findByEvaluationId(evaluationReport.getEvaluation().getId()));
    }

    public void deleteEvaluationReport(Integer id) {
        var evaluationReport = findById(id);
        repository.delete(evaluationReport);
    }

    private void setRelationFields(EvaluationReport evaluationReport, EvaluationReportDto evaluationReportDto) {
        evaluationReport.setEvaluation(evaluationUtilsService.findById(evaluationReportDto.getEvaluationId()));
    }
}
