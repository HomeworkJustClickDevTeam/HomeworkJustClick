package pl.homeworkjustclick.solution;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.assignment.Assignment;
import pl.homeworkjustclick.assignment.AssignmentRepository;
import pl.homeworkjustclick.assignment.AssignmentResponseDto;
import pl.homeworkjustclick.evaluation.EvaluationUtilsService;
import pl.homeworkjustclick.group.Group;
import pl.homeworkjustclick.group.GroupRepository;
import pl.homeworkjustclick.group.GroupResponseDto;
import pl.homeworkjustclick.infrastructure.enums.CalendarStatus;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;
import pl.homeworkjustclick.infrastructure.exception.InvalidArgumentException;
import pl.homeworkjustclick.user.User;
import pl.homeworkjustclick.user.UserRepository;
import pl.homeworkjustclick.user.UserResponseDto;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class SolutionService {
    private final EntityManager entityManager;
    private final SolutionRepository solutionRepository;
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final GroupRepository groupRepository;
    private final EvaluationUtilsService evaluationUtilsService;

    public Solution findById(Integer id) {
        return solutionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solution with id = " + id + " not found"));
    }

    public Solution findByEvaluationId(Integer evaluationId) {
        return solutionRepository.getSolutionByEvaluationId(evaluationId)
                .orElseThrow(() -> new EntityNotFoundException("Solution with evaluationId = " + evaluationId + "not found"));
    }

    public Group getGroupBySolutionId(Integer solutionId) {
        return findById(solutionId).getGroup();
    }

    public List<SolutionResponseDto> getAll() {
        List<Solution> solutionList = solutionRepository.findAll();
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutionList.forEach(solution -> responseList.add(buildSolutionResponse(solution)));
        return responseList;
    }

    public SolutionResponseDto getById(int id) {
        Optional<Solution> solutionOptional = solutionRepository.findById(id);
        return solutionOptional.map(this::buildSolutionResponse).orElse(null);
    }

    public List<SolutionResponseExtendedDto> getAllExtended() {
        List<Solution> solutionList = solutionRepository.findAll();
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutionList.forEach(solution -> responseList.add(buildSolutionResponseExtended(solution)));
        return responseList;
    }

    public SolutionResponseExtendedDto getByIdExtended(int id) {
        Optional<Solution> solutionOptional = solutionRepository.findById(id);
        return solutionOptional.map(this::buildSolutionResponseExtended).orElse(null);
    }

    @Transactional
    public SolutionResponseDto add(Solution solution) {
        entityManager.persist(solution);
        return SolutionResponseDto.builder()
                .id(solution.getId())
                .userId(solution.getUser().getId())
                .assignmentId(solution.getAssignment().getId())
                .creationDateTime(solution.getCreationDatetime())
                .lastModifiedDatetime(solution.getLastModifiedDatetime())
                .comment(solution.getComment())
                .build();
    }

    @Transactional
    public SolutionResponseDto addWithUserAndAssignment(Solution solution, int userId, int assignmentId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Assignment> assignment = assignmentRepository.findById(assignmentId);
        if (user.isPresent() && assignment.isPresent()) {
            List<User> userList = userRepository.getStudentsByGroupId(assignment.get().getGroup().getId());
            AtomicBoolean ok = new AtomicBoolean(false);
            userList.forEach(user1 -> {
                if (user1.getId() == userId) {
                    ok.set(true);
                }
            });
            if (solutionRepository.getSolutionByUserAndAssignment(userId, assignmentId).isPresent()) {
                ok.set(false);
            }
            if (ok.get()) {
                solution.setAssignment(assignment.get());
                solution.setUser(user.get());
                solution.setGroup(assignment.get().getGroup());
                entityManager.persist(solution);
                return SolutionResponseDto.builder()
                        .id(solution.getId())
                        .userId(userId)
                        .assignmentId(assignmentId)
                        .groupId(assignment.get().getGroup().getId())
                        .creationDateTime(solution.getCreationDatetime())
                        .lastModifiedDatetime(solution.getLastModifiedDatetime())
                        .comment(solution.getComment())
                        .build();
            } else {
                return SolutionResponseDto.builder().forbidden(true).build();
            }
        } else {
            return SolutionResponseDto.builder().build();
        }
    }

    @Transactional
    public void delete(int id) {
        var solution = findById(id);
        if (evaluationUtilsService.existsBySolutionId(id)) {
            throw new InvalidArgumentException("Evaluation to solution with id = " + id + " already exists");
        }
        solutionRepository.delete(solution);
    }

    @Transactional
    public Boolean changeUserById(int id, int userId) {
        if(solutionRepository.findById(id).isPresent() && userRepository.findById(userId).isPresent()){
            Solution solution = solutionRepository.findById(id).get();
            User user = userRepository.findById(userId).get();
            solution.setUser(user);
            solutionRepository.save(solution);
            return true;
        }
        else {
            return false;
        }
    }

    @Transactional
    public Boolean changeAssignmentById(int id, int assignmentId) {
        if(solutionRepository.findById(id).isPresent() && assignmentRepository.findById(assignmentId).isPresent()){
            Solution solution = solutionRepository.findById(id).get();
            Assignment assignment = assignmentRepository.findById(assignmentId).get();
            solution.setAssignment(assignment);
            solutionRepository.save(solution);
            return true;
        }
        else {
            return false;
        }
    }

    public List<SolutionResponseDto> getSolutionsByGroupId(int id) {
        List<Solution> solutions = solutionRepository.getSolutionsByGroupId(id);
        List<SolutionResponseDto> solutionResponsDtos = new ArrayList<>();
        for(Solution solution : solutions) {
            solutionResponsDtos.add(SolutionResponseDto.builder()
                    .id(solution.getId())
                    .userId(solution.getUser().getId())
                    .assignmentId(solution.getAssignment().getId())
                    .groupId(solution.getGroup().getId())
                    .creationDateTime(solution.getCreationDatetime())
                    .lastModifiedDatetime(solution.getLastModifiedDatetime())
                    .comment(solution.getComment())
                    .build());
        }
        return solutionResponsDtos;
    }

    public List<SolutionResponseDto> getSolutionsByAssignmentId(int id) {
        List<Solution> solutions = solutionRepository.findAllByAssignmentId(id);
        List<SolutionResponseDto> solutionResponsDtos = new ArrayList<>();
        for(Solution solution : solutions) {
            solutionResponsDtos.add(SolutionResponseDto.builder()
                    .id(solution.getId())
                    .userId(solution.getUser().getId())
                    .assignmentId(solution.getAssignment().getId())
                    .groupId(solution.getGroup().getId())
                    .creationDateTime(solution.getCreationDatetime())
                    .lastModifiedDatetime(solution.getLastModifiedDatetime())
                    .comment(solution.getComment())
                    .build());
        }
        return solutionResponsDtos;
    }

    public List<SolutionResponseDto> getLateSolutionsByGroup(int groupId) {
        List<Solution> solutions = solutionRepository.getSolutionsByGroupId(groupId);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if (solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> responseList.add(buildSolutionResponse(solution)));
        return responseList;
    }

    public List<SolutionResponseDto> getLateSolutionsByUserAndGroup(int userId, int groupId) {
        List<Solution> solutions = solutionRepository.getSolutionsByUserAndGroup(userId, groupId);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if (solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> responseList.add(buildSolutionResponse(solution)));
        return responseList;
    }

    public List<SolutionResponseDto> getLateSolutionsByAssignment(int assignmentId) {
        List<Solution> solutions = solutionRepository.findAllByAssignmentId(assignmentId);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if (solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> responseList.add(buildSolutionResponse(solution)));
        return responseList;
    }

    public List<SolutionResponseDto> getLateSolutionsByStudent(int userId) {
        List<Solution> solutions = solutionRepository.getSolutionsByUser(userId);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if (solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> responseList.add(buildSolutionResponse(solution)));
        return responseList;
    }

    public List<SolutionResponseDto> getUncheckedSolutionsByGroup(int groupId) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByGroup(groupId);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponse(solution)));
        return responseList;
    }

    public List<SolutionResponseDto> getCheckedSolutionsByGroup(int groupId) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByGroup(groupId);

        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponse(solution)));
        return responseList;
    }

    public List<SolutionResponseDto> getUncheckedSolutionsByStudent(int studentId) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByStudent(studentId);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponse(solution)));
        return responseList;
    }

    public List<SolutionResponseDto> getCheckedSolutionsByStudent(int studentId) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByStudent(studentId);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponse(solution)));
        return responseList;
    }

    public List<SolutionResponseDto> getUncheckedSolutionsByStudentAndGroup(int studentId, int groupId) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByStudentAndGroup(studentId, groupId);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponse(solution)));
        return responseList;
    }

    public List<SolutionResponseDto> getCheckedSolutionsByStudentAndGroup(int studentId, int groupId) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByStudentAndGroup(studentId, groupId);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponse(solution)));
        return responseList;
    }

    public List<SolutionResponseDto> getUncheckedSolutionsByAssignment(int assignmentId) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByAssignment(assignmentId);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponse(solution)));
        return responseList;
    }

    public List<SolutionResponseDto> getCheckedSolutionsByAssignment(int assignmentId) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByAssignment(assignmentId);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponse(solution)));
        return responseList;
    }

    public List<SolutionResponseDto> getUncheckedSolutionsByTeacher(int teacherId) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByTeacher(teacherId);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponse(solution)));
        return responseList;
    }

    public List<SolutionResponseDto> getCheckedSolutionsByTeacher(int teacherId) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByTeacher(teacherId);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponse(solution)));
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getSolutionsByGroupIdExtended(int id) {
        List<Solution> solutions = solutionRepository.getSolutionsByGroupId(id);
        List<SolutionResponseExtendedDto> solutionResponses = new ArrayList<>();
        for (Solution solution : solutions) {
            solutionResponses.add(buildSolutionResponseExtended(solution));
        }
        return solutionResponses;
    }

    public List<SolutionResponseExtendedDto> getSolutionsByAssignmentIdExtended(int id) {
        List<Solution> solutions = solutionRepository.findAllByAssignmentId(id);
        List<SolutionResponseExtendedDto> solutionResponses = new ArrayList<>();
        for (Solution solution : solutions) {
            solutionResponses.add(buildSolutionResponseExtended(solution));
        }
        return solutionResponses;
    }

    public List<SolutionResponseExtendedDto> getLateSolutionsByGroupExtended(int groupId) {
        List<Solution> solutions = solutionRepository.getSolutionsByGroupId(groupId);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if (solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> responseList.add(buildSolutionResponseExtended(solution)));
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getLateSolutionsByUserAndGroupExtended(int userId, int groupId) {
        List<Solution> solutions = solutionRepository.getSolutionsByUserAndGroup(userId, groupId);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if (solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> responseList.add(buildSolutionResponseExtended(solution)));
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getLateSolutionsByAssignmentExtended(int assignmentId) {
        List<Solution> solutions = solutionRepository.findAllByAssignmentId(assignmentId);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if (solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> responseList.add(buildSolutionResponseExtended(solution)));
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getLateSolutionsByStudentExtended(int userId) {
        List<Solution> solutions = solutionRepository.getSolutionsByUser(userId);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if (solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> responseList.add(buildSolutionResponseExtended(solution)));
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getUncheckedSolutionsByGroupExtended(int groupId) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByGroup(groupId);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponseExtended(solution)));
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getCheckedSolutionsByGroupExtended(int groupId) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByGroup(groupId);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponseExtended(solution)));
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getUncheckedSolutionsByStudentExtended(int studentId) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByStudent(studentId);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponseExtended(solution)));
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getCheckedSolutionsByStudentExtended(int studentId) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByStudent(studentId);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponseExtended(solution)));
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getUncheckedSolutionsByStudentAndGroupExtended(int studentId, int groupId) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByStudentAndGroup(studentId, groupId);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponseExtended(solution)));
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getCheckedSolutionsByStudentAndGroupExtended(int studentId, int groupId) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByStudentAndGroup(studentId, groupId);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponseExtended(solution)));
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getUncheckedSolutionsByAssignmentExtended(int assignmentId) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByAssignment(assignmentId);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponseExtended(solution)));
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getCheckedSolutionsByAssignmentExtended(int assignmentId) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByAssignment(assignmentId);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponseExtended(solution)));
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getUncheckedSolutionsByTeacherExtended(int teacherId) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByTeacher(teacherId);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponseExtended(solution)));
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getCheckedSolutionsByTeacherExtended(int teacherId) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByTeacher(teacherId);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> responseList.add(buildSolutionResponseExtended(solution)));
        return responseList;
    }

    private SolutionResponseDto buildSolutionResponse(Solution solution) {
        return SolutionResponseDto.builder()
                .id(solution.getId())
                .userId(solution.getUser().getId())
                .groupId(solution.getGroup().getId())
                .assignmentId(solution.getAssignment().getId())
                .creationDateTime(solution.getCreationDatetime())
                .lastModifiedDatetime(solution.getLastModifiedDatetime())
                .comment(solution.getComment())
                .build();
    }

    private SolutionResponseExtendedDto buildSolutionResponseExtended(Solution solution) {
        User user = solution.getUser();
        Group group = solution.getGroup();
        Assignment assignment = solution.getAssignment();
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .index(user.getIndex())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .color(user.getColor())
                .username(user.getUsername())
                .verified(user.isVerified())
                .build();
        GroupResponseDto groupResponseDto = GroupResponseDto.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .color(group.getColor())
                .isArchived(group.isArchived())
                .build();
        AssignmentResponseDto assignmentResponseDto = AssignmentResponseDto.builder()
                .id(assignment.getId())
                .userId((assignment.getUser() == null) ? null : assignment.getUser().getId())
                .groupId((assignment.getGroup() == null) ? null : assignment.getGroup().getId())
                .taskDescription(assignment.getTaskDescription())
                .creationDatetime(assignment.getCreationDatetime())
                .lastModifiedDatetime(assignment.getLastModifiedDatetime())
                .completionDatetime(assignment.getCompletionDatetime())
                .title(assignment.getTitle())
                .visible(assignment.getVisible())
                .maxPoints(assignment.getMaxPoints())
                .autoPenalty(assignment.getAutoPenalty())
                .advancedEvaluation(assignment.getAdvancedEvaluation())
                .build();

        return SolutionResponseExtendedDto.builder()
                .id(solution.getId())
                .user(userResponseDto)
                .group(groupResponseDto)
                .assignment(assignmentResponseDto)
                .creationDateTime(solution.getCreationDatetime())
                .lastModifiedDatetime(solution.getLastModifiedDatetime())
                .comment(solution.getComment())
                .build();
    }

    private SolutionResponseCalendarDto buildSolutionResponseCalendar(Solution solution, CalendarStatus calendarStatus) {
        return SolutionResponseCalendarDto.builder()
                .id(solution.getId())
                .userId(solution.getUser().getId())
                .groupId(solution.getGroup().getId())
                .assignmentId(solution.getAssignment().getId())
                .creationDateTime(solution.getCreationDatetime())
                .lastModifiedDatetime(solution.getLastModifiedDatetime())
                .comment(solution.getComment())
                .status(calendarStatus)
                .build();
    }

    public boolean checkForEvaluationToSolution(int solutionId) {
        return solutionRepository.checkForEvaluationToSolution(solutionId) != 0;
    }

    public boolean checkForFileToSolution(int solutionId) {
        Optional<Solution> solutionOptional = solutionRepository.findById(solutionId);
        if (solutionOptional.isPresent()) {
            Solution solution = solutionOptional.get();
            return !solution.getFiles().isEmpty();
        } else return false;
    }

    public SolutionResponseDto getCheckedSolutionByUserAssignmentGroup(int userId, int groupId, int assignmentId) {
        if (assignmentRepository.findById(assignmentId).isPresent() && groupRepository.findById(groupId).isPresent() && userRepository.findById(userId).isPresent()) {
            Optional<Solution> solution = solutionRepository.getCheckedSolutionByUserAssignmentGroup(assignmentId, userId, groupId);
            if (solution.isPresent()) {
                return buildSolutionResponse(solution.get());
            } else {
                return SolutionResponseDto.builder().build();
            }
        } else {
            return SolutionResponseDto.builder().forbidden(true).build();
        }

    }

    public SolutionResponseDto getUncheckedSolutionByUserAssignmentGroup(int userId, int groupId, int assignmentId) {
        if (assignmentRepository.findById(assignmentId).isPresent() && groupRepository.findById(groupId).isPresent() && userRepository.findById(userId).isPresent()) {
            Optional<Solution> solution = solutionRepository.getUncheckedSolutionByUserAssignmentGroup(assignmentId, userId, groupId);
            if (solution.isPresent()) {
                return buildSolutionResponse(solution.get());
            } else {
                return SolutionResponseDto.builder().build();
            }
        } else {
            return SolutionResponseDto.builder().forbidden(true).build();
        }

    }

    public List<SolutionResponseCalendarDto> getSolutionsByTeacherCalender(int teacherId) {
        List<Solution> checkedSolutionsList = solutionRepository.getCheckedSolutionsByTeacher(teacherId);
        List<Solution> uncheckedSolutionsList = solutionRepository.getUncheckedSolutionsByTeacher(teacherId);
        List<Solution> expiredUncheckedSolutionsList = new ArrayList<>();
        List<Solution> nonExpiredUncheckedSolutionsList = new ArrayList<>();
        for (Solution solution : uncheckedSolutionsList) {
            if (solution.getCreationDatetime().plusDays(14).isBefore(OffsetDateTime.now())) {
                expiredUncheckedSolutionsList.add(solution);
            } else {
                nonExpiredUncheckedSolutionsList.add(solution);
            }
        }
        List<SolutionResponseCalendarDto> response = new ArrayList<>();
        for (Solution solution : checkedSolutionsList) {
            response.add(buildSolutionResponseCalendar(solution, CalendarStatus.DONE));
        }
        for (Solution solution : expiredUncheckedSolutionsList) {
            response.add(buildSolutionResponseCalendar(solution, CalendarStatus.LATE));
        }
        for (Solution solution : nonExpiredUncheckedSolutionsList) {
            response.add(buildSolutionResponseCalendar(solution, CalendarStatus.TODO));
        }
        response.sort(Comparator.comparing(SolutionResponseCalendarDto::getCreationDateTime));
        return response;
    }

    public Boolean checkIfSolutionWasLate(Solution solution) {
        return solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime());
    }

    public List<Solution> getSolutionsModelsByAssignmentId(Integer assignmentId) {
        return solutionRepository.findAllByAssignmentId(assignmentId);
    }
}
