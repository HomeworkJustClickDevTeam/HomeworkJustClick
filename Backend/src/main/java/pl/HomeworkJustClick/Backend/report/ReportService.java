package pl.HomeworkJustClick.Backend.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.HomeworkJustClick.Backend.assignment.AssignmentMapper;
import pl.HomeworkJustClick.Backend.assignment.AssignmentService;
import pl.HomeworkJustClick.Backend.evaluation.Evaluation;
import pl.HomeworkJustClick.Backend.evaluation.EvaluationService;
import pl.HomeworkJustClick.Backend.group.GroupMapper;
import pl.HomeworkJustClick.Backend.group.GroupService;
import pl.HomeworkJustClick.Backend.infrastructure.exception.EntityNotFoundException;
import pl.HomeworkJustClick.Backend.solution.SolutionService;
import pl.HomeworkJustClick.Backend.user.UserMapper;
import pl.HomeworkJustClick.Backend.user.UserService;

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
    private final UserService userService;

    @Transactional(readOnly = true)
    public AssignmentReportResponseDto createAssignmentReport(AssignmentReportDto assignmentReportDto) {
        var assignmentId = assignmentReportDto.getAssignmentId();
        var assignment = assignmentService.findById(assignmentReportDto.getAssignmentId());
        var evaluations = evaluationService.getEvaluationsByAssignment(assignment.getId());
        var groupId = assignment.getGroup().getId();
        if (evaluations.isEmpty()) {
            return AssignmentReportResponseDto.builder()
                    .assignment(assignmentMapper.map(assignment))
                    .description("Brak ocenionych rozwiązań")
                    .build();
        }
        var maxPoints = assignment.getMax_points();
        var studentsResults = createStudentsList(evaluations, maxPoints, groupId, assignmentId);
        var maxResult = calculateMaxResult(studentsResults);
        var maxResultPercent = roundDouble(maxResult * 100 / maxPoints);
        var minResult = calculateMinResult(studentsResults);
        var minResultPercent = roundDouble(minResult * 100 / maxPoints);
        var avgResult = calculateAvgResult(studentsResults);
        var avgResultPercent = roundDouble(avgResult * 100 / maxPoints);
        var late = solutionService.getLateSolutionsByAssignment(assignment.getId()).size();
        var hist = assignmentReportDto.getHist();
        var studentsHist = calculateHistogram(studentsResults, hist);
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
                .students(studentsResults)
                .build();
    }

    @Transactional(readOnly = true)
    public GroupReportResponseDto createGroupReport(GroupReportDto groupReportDto) {
        var group = groupService.getById(groupReportDto.getGroupId()).orElseThrow(() -> new EntityNotFoundException("Group with id = " + groupReportDto.getGroupId() + " not found"));
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

    private Double calculateMaxResult(List<AssignmentReportStudentResponseDto> evaluations) {
        return evaluations.stream().max(Comparator.comparing(AssignmentReportStudentResponseDto::getResult)).orElseThrow().getResult();
    }

    private Double calculateMinResult(List<AssignmentReportStudentResponseDto> evaluations) {
        return evaluations.stream().min(Comparator.comparing(AssignmentReportStudentResponseDto::getResult)).orElseThrow().getResult();
    }

    private Double calculateAvgResult(List<AssignmentReportStudentResponseDto> evaluations) {
        return evaluations.stream().mapToDouble(AssignmentReportStudentResponseDto::getResult).average().getAsDouble();
    }

    private List<Integer> calculateHistogram(List<AssignmentReportStudentResponseDto> studentResults, List<Integer> histFromDto) {
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
        for (AssignmentReportStudentResponseDto studentResult : studentResults) {
            for (int j = 0; j < histSize - 1; j++) {
                if (studentResult.getResult() != 0 && studentResult.getResult() > hist.get(j) && studentResult.getResult() <= hist.get(j + 1)) {
                    var current = histResponse.get(j);
                    histResponse.set(j, current + 1);
                }
            }
            if (studentResult.getResult() == 0) {
                var current = histResponse.get(0);
                histResponse.set(0, current + 1);
            }
        }
        return histResponse;
    }

    private List<AssignmentReportStudentResponseDto> createStudentsList(List<Evaluation> evaluations, Integer maxPoints, Integer groupId, Integer assignmentId) {
        var assignmentReportStudentResponseDtoList = new ArrayList<AssignmentReportStudentResponseDto>();
        var studentsFromGroup = userService.getStudentsByGroup(groupId);
        var solutions = solutionService.getSolutionsModelsByAssignmentId(assignmentId);
        evaluations.forEach(evaluation -> {
            var solution = evaluation.getSolution();
            var user = solution.getUser();
            studentsFromGroup.removeIf(u -> u.getEmail().equals(user.getEmail()));
            solutions.removeIf(s -> s.getId() == solution.getId());
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
        solutions.forEach(s -> {
            studentsFromGroup.removeIf(u -> u.getEmail().equals(s.getUser().getEmail()));
            assignmentReportStudentResponseDtoList.add(
                    AssignmentReportStudentResponseDto.builder()
                            .student(userMapper.map2SimpleResponseDto(s.getUser()))
                            .result((double) 0)
                            .resultPercent((double) 0)
                            .description("Nie oceniono rozwiązania")
                            .build()
            );
        });
        studentsFromGroup.forEach(student -> {
            assignmentReportStudentResponseDtoList.add(
                    AssignmentReportStudentResponseDto.builder()
                            .student(userMapper.map2SimpleResponseDto(student))
                            .result((double) 0)
                            .resultPercent((double) 0)
                            .description("Nie wysłano rozwiązania")
                            .build()
            );
        });
        return assignmentReportStudentResponseDtoList;
    }

    private Double roundDouble(Double d) {
        return new BigDecimal(d.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
