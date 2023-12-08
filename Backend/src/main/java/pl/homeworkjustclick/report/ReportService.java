package pl.homeworkjustclick.report;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.homeworkjustclick.assignment.AssignmentMapper;
import pl.homeworkjustclick.assignment.AssignmentService;
import pl.homeworkjustclick.evaluation.Evaluation;
import pl.homeworkjustclick.evaluation.EvaluationService;
import pl.homeworkjustclick.group.GroupMapper;
import pl.homeworkjustclick.group.GroupService;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;
import pl.homeworkjustclick.solution.SolutionService;
import pl.homeworkjustclick.user.UserMapper;
import pl.homeworkjustclick.user.UserService;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Transactional
    public ResponseEntity<UrlResource> createGroupCsvReport(GroupReportDto groupReportDto) throws IOException {
        var groupReport = createGroupReport(groupReportDto);
        var dataLines = new ArrayList<String[]>();
        var headerLength = 2;
        var header = new ArrayList<>(List.of("nazwa zadania", "liczba punktów do zdobycia"));
        var end = new ArrayList<>(List.of("suma", "0"));
        if (groupReportDto.getAvgResult()) {
            header.add("średni wynik");
            header.add("średni wynik %");
            end.add("0");
            end.add("0");
            headerLength += 2;
        }
        if (groupReportDto.getMaxResult()) {
            header.add("najwyższy wynik");
            header.add("najwyższy wynik %");
            end.add("0");
            end.add("0");
            headerLength += 2;
        }
        if (groupReportDto.getMinResult()) {
            header.add("najniższy wynik");
            header.add("najniższy wynik %");
            end.add("0");
            end.add("0");
            headerLength += 2;
        }
        if (groupReportDto.getLate()) {
            header.add("liczba spóżnionych");
            end.add("0");
            headerLength += 1;
        }
        var hist = groupReport.getAssignments().get(0).getHist();
        if (hist.get(0) != 0) {
            hist.add(0);
        }
        if (hist.get(hist.size() - 1) != 100) {
            hist.add(100);
        }
        Collections.sort(hist);
        for (int i = 0; i < hist.size() - 1; i++) {
            header.add(hist.get(i) + "-" + hist.get(i + 1));
            end.add("0");
        }
        var students = userService.getStudentsByGroup(groupReportDto.getGroupId());
        students.forEach(student -> {
            header.add(student.getFirstname() + " " + student.getLastname() + " " + student.getIndex());
            end.add("0");
            header.add(student.getFirstname() + " " + student.getLastname() + " " + student.getIndex() + " %");
            end.add("0");
        });
        dataLines.add(header.toArray(new String[0]));
        var endLine = end.toArray(new String[0]);
        int finalHeaderLength = headerLength;
        groupReport.getAssignments().forEach(assignment -> {
            var line = new String[header.size()];
            line[0] = assignment.getAssignment().getTitle();
            line[1] = String.valueOf(assignment.getAssignment().getMax_points());
            endLine[1] = String.valueOf(Double.parseDouble(endLine[1]) + assignment.getAssignment().getMax_points());
            var k = 2;
            if (groupReportDto.getAvgResult()) {
                line[k] = String.valueOf(assignment.getAvgResult());
                endLine[k] = String.valueOf(Double.parseDouble(endLine[k]) + assignment.getAvgResult());
                line[k + 1] = String.valueOf(assignment.getAvgResultPercent());
                endLine[k + 1] = String.valueOf(Double.parseDouble(endLine[k + 1]) + assignment.getAvgResultPercent());
                k += 2;
            }
            if (groupReportDto.getMaxResult()) {
                line[k] = String.valueOf(assignment.getMaxResult());
                endLine[k] = String.valueOf(Double.parseDouble(endLine[k]) + assignment.getMaxResult());
                line[k + 1] = String.valueOf(assignment.getMaxResultPercent());
                endLine[k + 1] = String.valueOf(Double.parseDouble(endLine[k + 1]) + assignment.getMaxResultPercent());
                k += 2;
            }
            if (groupReportDto.getMinResult()) {
                line[k] = String.valueOf(assignment.getMinResult());
                endLine[k] = String.valueOf(Double.parseDouble(endLine[k + 1]) + assignment.getMinResult());
                line[k + 1] = String.valueOf(assignment.getMinResultPercent());
                endLine[k + 1] = String.valueOf(Double.parseDouble(endLine[k + 1]) + assignment.getMinResultPercent());
                k += 2;
            }
            if (groupReportDto.getLate()) {
                line[k] = String.valueOf(assignment.getLate());
                endLine[k] = String.valueOf(Double.parseDouble(endLine[k]) + assignment.getLate());
            }
            for (int i = finalHeaderLength; i < finalHeaderLength + hist.size() - 1; i++) {
                line[i] = String.valueOf(assignment.getStudentsHist().get(i - finalHeaderLength));
                endLine[i] = String.valueOf(Double.parseDouble(endLine[i]) + assignment.getStudentsHist().get(i - finalHeaderLength));
            }
            var j = 0;
            for (int i = finalHeaderLength + hist.size() - 1; i < header.size(); i += 2) {
                line[i] = String.valueOf(assignment.getStudents().get(j).getResult());
                endLine[i] = String.valueOf(Double.parseDouble(endLine[i]) + assignment.getStudents().get(j).getResult());
                line[i + 1] = String.valueOf(assignment.getStudents().get(j).getResultPercent());
                endLine[i + 1] = String.valueOf(Double.parseDouble(endLine[i + 1]) + assignment.getStudents().get(j).getResultPercent());
                j += 1;
            }
            dataLines.add(line);
        });
        if (finalHeaderLength >= 5) {
            endLine[3] = String.valueOf(roundDouble(Double.parseDouble(endLine[3]) / (dataLines.size() - 1)));
        }
        if (finalHeaderLength >= 7) {
            endLine[5] = String.valueOf(roundDouble(Double.parseDouble(endLine[5]) / (dataLines.size() - 1)));
        }
        if (finalHeaderLength >= 9) {
            endLine[7] = String.valueOf(roundDouble(Double.parseDouble(endLine[7]) / (dataLines.size() - 1)));
        }
        for (int i = finalHeaderLength + hist.size(); i < header.size(); i += 2) {
            endLine[i] = String.valueOf(roundDouble(Double.parseDouble(endLine[i]) / (dataLines.size() - 1)));
        }
        dataLines.add(endLine);
        var fileName = "raport.csv";
        File csvOutputFile = new File(fileName);
        csvOutputFile.createNewFile();
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
        return generateResponse(fileName);
    }

    @Transactional
    public ResponseEntity<UrlResource> createAssignmentCsvReport(AssignmentReportDto assignmentReportDto) throws IOException {
        var assignmentReport = createAssignmentReport(assignmentReportDto);
        var dataLines = new ArrayList<String[]>();
        var headerLength = 2;
        var header = new ArrayList<>(List.of("nazwa zadania", "liczba punktów do zdobycia"));
        if (assignmentReportDto.getAvgResult()) {
            header.add("średni wynik");
            header.add("średni wynik %");
            headerLength += 2;
        }
        if (assignmentReportDto.getMaxResult()) {
            header.add("najwyższy wynik");
            header.add("najwyższy wynik %");
            headerLength += 2;
        }
        if (assignmentReportDto.getMinResult()) {
            header.add("najniższy wynik");
            header.add("najniższy wynik %");
            headerLength += 2;
        }
        if (assignmentReportDto.getLate()) {
            header.add("liczba spóżnionych");
            headerLength += 1;
        }
        var hist = assignmentReport.getHist();
        if (hist.get(0) != 0) {
            hist.add(0);
        }
        if (hist.get(hist.size() - 1) != 100) {
            hist.add(100);
        }
        Collections.sort(hist);
        for (int i = 0; i < hist.size() - 1; i++) {
            header.add(hist.get(i) + "-" + hist.get(i + 1));
        }
        var students = userService.getStudentsByGroup(assignmentReport.getAssignment().getGroupId());
        students.forEach(student -> {
            header.add(student.getFirstname() + " " + student.getLastname() + " " + student.getIndex());
            header.add(student.getFirstname() + " " + student.getLastname() + " " + student.getIndex() + " %");
        });
        dataLines.add(header.toArray(new String[0]));
        int finalHeaderLength = headerLength;
        var line = new String[header.size()];
        line[0] = assignmentReport.getAssignment().getTitle();
        line[1] = String.valueOf(assignmentReport.getAssignment().getMax_points());
        var k = 2;
        if (assignmentReportDto.getAvgResult()) {
            line[k] = String.valueOf(assignmentReport.getAvgResult());
            line[k + 1] = String.valueOf(assignmentReport.getAvgResultPercent());
            k += 2;
        }
        if (assignmentReportDto.getMaxResult()) {
            line[k] = String.valueOf(assignmentReport.getMaxResult());
            line[k + 1] = String.valueOf(assignmentReport.getMaxResultPercent());
            k += 2;
        }
        if (assignmentReportDto.getMinResult()) {
            line[k] = String.valueOf(assignmentReport.getMinResult());
            line[k + 1] = String.valueOf(assignmentReport.getMinResultPercent());
            k += 2;
        }
        if (assignmentReportDto.getLate()) {
            line[k] = String.valueOf(assignmentReport.getLate());
        }
        for (int i = finalHeaderLength; i < finalHeaderLength + hist.size() - 1; i++) {
            line[i] = String.valueOf(assignmentReport.getStudentsHist().get(i - finalHeaderLength));
        }
        var j = 0;
        for (int i = finalHeaderLength + hist.size() - 1; i < header.size(); i += 2) {
            line[i] = String.valueOf(assignmentReport.getStudents().get(j).getResult());
            line[i + 1] = String.valueOf(assignmentReport.getStudents().get(j).getResultPercent());
            j += 1;
        }
        dataLines.add(line);
        var fileName = "raport.csv";
        File csvOutputFile = new File(fileName);
        csvOutputFile.createNewFile();
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
        return generateResponse(fileName);
    }

    private ResponseEntity<UrlResource> generateResponse(String fileName) throws MalformedURLException {
        var path = Paths.get(fileName);
        var resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(resource);
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private String escapeSpecialCharacters(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data cannot be null");
        }
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
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
                if (studentResult.getResultPercent() != 0 && studentResult.getResultPercent() > hist.get(j) && studentResult.getResultPercent() <= hist.get(j + 1)) {
                    var current = histResponse.get(j);
                    histResponse.set(j, current + 1);
                }
            }
            if (studentResult.getResultPercent() == 0) {
                var current = histResponse.get(0);
                histResponse.set(0, current + 1);
            }
            if (studentResult.getResultPercent() > hist.get(histSize - 1)) {
                var current = histResponse.get(histSize - 1);
                histResponse.set(histSize - 1, current + 1);
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
        studentsFromGroup.forEach(student -> assignmentReportStudentResponseDtoList.add(
                AssignmentReportStudentResponseDto.builder()
                        .student(userMapper.map2SimpleResponseDto(student))
                        .result((double) 0)
                        .resultPercent((double) 0)
                        .description("Nie wysłano rozwiązania")
                        .build()
        ));
        return assignmentReportStudentResponseDtoList;
    }

    private Double roundDouble(Double d) {
        return new BigDecimal(d.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
