package pl.HomeworkJustClick.Backend.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.HomeworkJustClick.Backend.assignment.AssignmentMapper;
import pl.HomeworkJustClick.Backend.assignment.AssignmentService;
import pl.HomeworkJustClick.Backend.evaluation.EvaluationResponseDto;
import pl.HomeworkJustClick.Backend.evaluation.EvaluationService;
import pl.HomeworkJustClick.Backend.group.GroupMapper;
import pl.HomeworkJustClick.Backend.group.GroupService;
import pl.HomeworkJustClick.Backend.infrastructure.exception.EntityNotFoundException;
import pl.HomeworkJustClick.Backend.solution.SolutionService;
import pl.HomeworkJustClick.Backend.user.UserMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final AssignmentService assignmentService;
    private final EvaluationService evaluationService;
    private final SolutionService solutionService;
    private final GroupService groupService;
    private final AssignmentMapper assignmentMapper;
    private final UserMapper userMapper;
    private final GroupMapper groupMapper;

    @Transactional(readOnly = true)
    public AssignmentReportResponseDto createAssignmentReport(AssignmentReportDto assignmentReportDto) {
        var assignment = assignmentService.findById(assignmentReportDto.getAssignmentId());
        var evaluations = evaluationService.getAllEvaluationsByAssignment(assignment.getId());
        var maxPoints = assignment.getMax_points();
        var maxResult = calculateMaxResult(evaluations);
        var maxResultPercent = roundDouble(maxResult * 100 / maxPoints);
        var minResult = calculateMinResult(evaluations);
        var minResultPercent = roundDouble(minResult * 100 / maxPoints);
        var avgResult = calculateAvgResult(evaluations);
        var avgResultPercent = roundDouble(avgResult * 100 / maxPoints);
        var late = solutionService.getLateSolutionsByAssignment(assignment.getId()).size();
        var hist = assignmentReportDto.getHist();
        var studentsHist = calculateHistogram(evaluations, hist);
        return AssignmentReportResponseDto.builder()
                .assignment(assignmentMapper.map(assignment))
                .maxResult(maxResult)
                .maxResultPercent(maxResultPercent)
                .minResult(minResult)
                .minResultPercent(minResultPercent)
                .avgResult(avgResult)
                .avgResultPercent(avgResultPercent)
                .late(late)
                .hist(hist)
                .studentsHist(studentsHist)
                .students(createStudentsList(evaluations, maxPoints))
                .build();
    }

    @Transactional(readOnly = true)
    public GroupReportResponseDto createGroupReport(GroupReportDto groupReportDto) {
        var group = groupService.getById(groupReportDto.getGroupId()).orElseThrow(() -> new EntityNotFoundException(""));
        var assignments = assignmentService.getAssignmentsByGroupId(groupReportDto.getGroupId());
        var assignmentReportList = new ArrayList<AssignmentReportResponseDto>();
        assignments.forEach(assignment -> {
            var assignmentReport = createAssignmentReport(AssignmentReportDto.builder()
                    .assignmentId(assignment.getId())
                    .maxResult(groupReportDto.getMaxResult())
                    .minResult(groupReportDto.getMinResult())
                    .avgResult(groupReportDto.getAvgResult())
                    .hist(groupReportDto.getHist())
                    .late(groupReportDto.getLate())
                    .build());
            assignmentReportList.add(assignmentReport);
        });
        return GroupReportResponseDto.builder()
                .group(groupMapper.map(group))
                .assignments(assignmentReportList)
                .build();
    }

    private Double calculateMaxResult(List<EvaluationResponseDto> evaluations) {
        return evaluations.stream().max(Comparator.comparing(v -> v.getResult())).get().getResult();
    }

    private Double calculateMinResult(List<EvaluationResponseDto> evaluations) {
        return evaluations.stream().min(Comparator.comparing(v -> v.getResult())).get().getResult();
    }

    private Double calculateAvgResult(List<EvaluationResponseDto> evaluations) {
        return evaluations.stream().mapToDouble(EvaluationResponseDto::getResult).average().getAsDouble();
    }

    private List<Integer> calculateHistogram(List<EvaluationResponseDto> evaluations, List<Integer> histFromDto) {
        var hist = new ArrayList<>(histFromDto);
        Collections.sort(hist);
        var histSize = hist.size();
        if (hist.get(0) != 0) {
            hist.add(0);
        }
        if (hist.get(histSize - 1) == 100) {
            hist.remove(histSize - 1);
        }
        histSize = hist.size();
        Collections.sort(hist);
        var histResponse = new ArrayList<Integer>();
        for (int i = 0; i < histSize; i++) {
            histResponse.add(0);
        }
        for (EvaluationResponseDto evaluation : evaluations) {
            for (int j = 0; j < histSize - 1; j++) {
                if (evaluation.getResult() > hist.get(j) && evaluation.getResult() <= hist.get(j + 1)) {
                    var current = histResponse.get(j);
                    histResponse.set(j, current + 1);
                }
            }
        }
        return histResponse;
    }

    private List<AssignmentReportStudentResponseDto> createStudentsList(List<EvaluationResponseDto> evaluations, Integer maxPoints) {
        var assignmentReportStudentResponseDtoList = new ArrayList<AssignmentReportStudentResponseDto>();
        evaluations.forEach(evaluation -> {
            var solutionOptional = solutionService.getSolutionByEvaluationId(evaluation.getId());
            var solution = solutionOptional.get();
            var user = solution.getUser();
            var result = evaluation.getResult();
            var resultPercent = roundDouble(result * 100 / maxPoints);
            assignmentReportStudentResponseDtoList.add(
                    AssignmentReportStudentResponseDto.builder()
                            .student(userMapper.map2SimpleResponseDto(user))
                            .result(result)
                            .resultPercent(resultPercent)
                            .late(solutionService.checkIfSolutionWasLate(solution))
                            .build()
            );
        });
        return assignmentReportStudentResponseDtoList;
    }

    private Double roundDouble(Double d) {
        return new BigDecimal(d.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
