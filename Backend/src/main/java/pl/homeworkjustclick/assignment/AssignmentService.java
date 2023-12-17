package pl.homeworkjustclick.assignment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.evaluation.EvaluationRepository;
import pl.homeworkjustclick.group.Group;
import pl.homeworkjustclick.group.GroupRepository;
import pl.homeworkjustclick.group.GroupResponseDto;
import pl.homeworkjustclick.infrastructure.enums.CalendarStatus;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;
import pl.homeworkjustclick.notification.NotificationCreateService;
import pl.homeworkjustclick.solution.SolutionRepository;
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
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    private final SolutionRepository solutionRepository;

    private final EvaluationRepository evaluationRepository;
    private final NotificationCreateService notificationCreateService;

    public Assignment findById(Integer id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assignment with id = " + id + " not found"));
    }

    public List<AssignmentResponseDto> getAll() {
        List<Assignment> assignmentList = assignmentRepository.findAll();
        List<AssignmentResponseDto> responseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            responseList.add(buildAssignmentResponse(assignment));
        });
        return responseList;
    }

    public List<AssignmentResponseExtendedDto> getAllExtended() {
        List<Assignment> assignmentList = assignmentRepository.findAll();
        List<AssignmentResponseExtendedDto> responseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            responseList.add(buildAssignmentResponseExtended(assignment));
        });
        return responseList;
    }

    public AssignmentResponseDto getById(int id) {
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(id);
        return assignmentOptional.map(this::buildAssignmentResponse).orElse(null);
    }

    public AssignmentResponseExtendedDto getByIdExtended(int id) {
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(id);
        return assignmentOptional.map(this::buildAssignmentResponseExtended).orElse(null);
    }

    public Assignment findByEvaluationId(Integer evaluationId) {
        return assignmentRepository.findAssignmentByEvaluationId(evaluationId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment with evaluationId = " + evaluationId + " not found"));
    }

    @Transactional
    public AssignmentResponseDto add(Assignment assignment) {
        assignmentRepository.save(assignment);
        return AssignmentResponseDto.builder()
                .id(assignment.getId())
                .userId((assignment.getUser() == null) ? null : assignment.getUser().getId())
                .groupId((assignment.getGroup() == null) ? null : assignment.getGroup().getId())
                .taskDescription(assignment.getTaskDescription())
                .creationDatetime(assignment.getCreationDatetime())
                .lastModifiedDatetime(assignment.getLastModifiedDatetime())
                .completionDatetime(assignment.getCompletionDatetime())
                .title(assignment.getTitle())
                .visible(assignment.getVisible())
                .max_points(assignment.getMax_points())
                .build();
    }

    @Transactional
    public AssignmentResponseDto addWithUserAndGroup(Assignment assignment, int studentId, int groupId) {
        Optional<User> user = userRepository.findById(studentId);
        Optional<Group> group = groupRepository.findById(groupId);
        if (user.isPresent() && group.isPresent()) {
            List<User> userList = userRepository.getTeachersByGroupId(groupId);
            AtomicBoolean ok = new AtomicBoolean(false);
            userList.forEach(user1 -> {
                if (user1.getId() == studentId) {
                    ok.set(true);
                }
            });
            if (ok.get()) {
                assignment.setGroup(group.get());
                assignment.setUser(user.get());
                assignmentRepository.save(assignment);
                group.get().getGroupStudents().forEach(groupStudent -> {
                    var student = groupStudent.getUser();
                    notificationCreateService.createAssignmentNotification(student, assignment, group.get());
                });
                return AssignmentResponseDto.builder()
                        .id(assignment.getId())
                        .userId(studentId)
                        .groupId(groupId)
                        .taskDescription(assignment.getTaskDescription())
                        .creationDatetime(assignment.getCreationDatetime())
                        .lastModifiedDatetime(assignment.getLastModifiedDatetime())
                        .completionDatetime(assignment.getCompletionDatetime())
                        .title(assignment.getTitle())
                        .visible(assignment.getVisible())
                        .max_points(assignment.getMax_points())
                        .build();
            } else {
                return AssignmentResponseDto.builder().forbidden(true).build();
            }
        } else {
            return AssignmentResponseDto.builder().build();
        }
    }

    @Transactional
    public Boolean update(int id, Assignment updatedAssignment) {
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(id);
        if(assignmentOptional.isEmpty()) {
            return false;
        }
        Assignment assignment = assignmentOptional.get();
        if (!updatedAssignment.getTitle().isEmpty() && !updatedAssignment.getTitle().equals(assignment.getTitle())) {
            assignment.setTitle(updatedAssignment.getTitle());
        }
        if (!updatedAssignment.getVisible().equals(assignment.getVisible())) {
            assignment.setVisible(updatedAssignment.getVisible());
        }
        if (!updatedAssignment.getTaskDescription().isEmpty() && !updatedAssignment.getTaskDescription().equals(assignment.getTaskDescription())) {
            assignment.setTaskDescription(updatedAssignment.getTaskDescription());
        }
        if (!updatedAssignment.getCreationDatetime().equals(assignment.getCreationDatetime())) {
            assignment.setCreationDatetime(updatedAssignment.getCreationDatetime());
        }
        if (!updatedAssignment.getCompletionDatetime().equals(assignment.getCompletionDatetime())) {
            assignment.setCompletionDatetime(updatedAssignment.getCompletionDatetime());
        }
        if (!updatedAssignment.getLastModifiedDatetime().equals(assignment.getLastModifiedDatetime())) {
            assignment.setLastModifiedDatetime(updatedAssignment.getLastModifiedDatetime());
        }
        if (updatedAssignment.getMax_points() >= 0 && !updatedAssignment.getMax_points().equals(assignment.getMax_points())) {
            assignment.setMax_points(updatedAssignment.getMax_points());
        }
        if (updatedAssignment.getAuto_penalty() >= 0 && updatedAssignment.getAuto_penalty() <= 100 && !updatedAssignment.getAuto_penalty().equals(assignment.getAuto_penalty())) {
            assignment.setAuto_penalty(updatedAssignment.getAuto_penalty());
        }
        updatedAssignment.setUser(assignment.getUser());
        updatedAssignment.setGroup(assignment.getGroup());
        assignmentRepository.save(updatedAssignment);
        return true;
    }

    @Transactional
    public Boolean delete(int id) {
        if(assignmentRepository.existsById(id)) {
            assignmentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Boolean changeTitleById(int id, String title) {
        if(assignmentRepository.findById(id).isPresent()){
            Assignment assignment = assignmentRepository.findById(id).get();
            assignment.setTitle(title);
            assignmentRepository.save(assignment);
            return true;
        }else {
            return false;
        }
    }

    @Transactional
    public Boolean changeTaskDescriptionById(int id, String taskDescription) {
        if(assignmentRepository.findById(id).isPresent()){
            Assignment assignment = assignmentRepository.findById(id).get();
            assignment.setTaskDescription(taskDescription);
            assignmentRepository.save(assignment);
            return true;
        }else {
            return false;
        }
    }

    @Transactional
    public Boolean changeCompletionDatetime(int id, OffsetDateTime completionDatetime) {
        if(assignmentRepository.findById(id).isPresent()){
            Assignment assignment = assignmentRepository.findById(id).get();
            assignment.setCompletionDatetime(completionDatetime);
            assignmentRepository.save(assignment);
            return true;
        }else {
            return false;
        }
    }

    @Transactional
    public Boolean changeVisibility(int id, Boolean visible) {
        if(assignmentRepository.findById(id).isPresent()){
            Assignment assignment = assignmentRepository.findById(id).get();
            assignment.setVisible(visible);
            assignmentRepository.save(assignment);
            return true;
        }
        else {
            return false;
        }
    }

    @Transactional
    public Boolean changeUser(int id, int userId) {
        if(assignmentRepository.findById(id).isPresent() && userRepository.findById(userId).isPresent())
        {
            Assignment assignment = assignmentRepository.findById(id).get();
            User user = userRepository.findById(userId).get();
            assignment.setUser(user);
            assignmentRepository.save(assignment);
            return true;
        }
        else{
            return false;
        }
    }

    @Transactional
    public Boolean changeGroup(int id, int groupId) {
        if(assignmentRepository.findById(id).isPresent() && groupRepository.findById(groupId).isPresent())
        {
            Assignment assignment = assignmentRepository.findById(id).get();
            Group group = groupRepository.findById(groupId).get();
            assignment.setGroup(group);
            assignmentRepository.save(assignment);
            return true;
        }
        else{
            return false;
        }
    }

    @Transactional
    public Boolean changeMaxPoints(int id, int points) {
        if (assignmentRepository.findById(id).isPresent() && points >= 0) {
            Assignment assignment = assignmentRepository.findById(id).get();
            assignment.setMax_points(points);
            assignmentRepository.save(assignment);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Boolean changeAutoPenalty(int id, int auto_penalty) {
        if (assignmentRepository.findById(id).isPresent() && auto_penalty >= 0 && auto_penalty <= 100) {
            Assignment assignment = assignmentRepository.findById(id).get();
            assignment.setAuto_penalty(auto_penalty);
            assignmentRepository.save(assignment);
            return true;
        } else {
            return false;
        }
    }

    public List<AssignmentResponseDto> getAssignmentsByGroupId(int id) {
        List<Assignment> assignments = assignmentRepository.getAssignmentsByGroupId(id);
        List<AssignmentResponseDto> assignmentResponsDtos = new ArrayList<>();
        for (Assignment assignment : assignments) {
            assignmentResponsDtos.add(AssignmentResponseDto.builder()
                    .id(assignment.getId())
                    .userId(assignment.getUser().getId())
                    .groupId(assignment.getGroup().getId())
                    .taskDescription(assignment.getTaskDescription())
                    .creationDatetime(assignment.getCreationDatetime())
                    .lastModifiedDatetime(assignment.getLastModifiedDatetime())
                    .completionDatetime(assignment.getCompletionDatetime())
                    .title(assignment.getTitle())
                    .visible(assignment.getVisible())
                    .max_points(assignment.getMax_points())
                    .build());
        }
        return assignmentResponsDtos;
    }

    public List<AssignmentResponseDto> getUncheckedAssignmentsByGroup(int groupId) {
        List<Assignment> assignmentsInGroup = assignmentRepository.getAssignmentsByGroupId(groupId);
        List<AssignmentResponseDto> uncheckedAssignments = new ArrayList<>();
        for (Assignment assignment : assignmentsInGroup) {
            int assignmentId = assignment.getId();
            int solutionsCount = solutionRepository.countSolutionsByAssignmentId(assignmentId);
            int evaluationsCount = evaluationRepository.countEvaluationsByAssignment(assignmentId);
            if (solutionsCount != 0 && solutionsCount - evaluationsCount > 0) {
                uncheckedAssignments.add(
                        AssignmentResponseDto.builder()
                                .id(assignment.getId())
                                .userId(assignment.getUser().getId())
                                .groupId(assignment.getGroup().getId())
                                .taskDescription(assignment.getTaskDescription())
                                .creationDatetime(assignment.getCreationDatetime())
                                .lastModifiedDatetime(assignment.getLastModifiedDatetime())
                                .completionDatetime(assignment.getCompletionDatetime())
                                .title(assignment.getTitle())
                                .visible(assignment.getVisible())
                                .max_points(assignment.getMax_points())
                                .build()
                );
            }
        }
        return uncheckedAssignments;
    }

    public List<AssignmentResponseDto> getAllAssignmentsByGroupIdAndUserId(int groupId, int userId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(groupId, userId);
        List<AssignmentResponseDto> assignmentResponseDtoList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseDtoList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseDtoList;
    }

    public List<AssignmentResponseDto> getUndoneAssignmentsByGroupIdAndUserId(int groupId, int userId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(groupId, userId);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(groupId, userId);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseDto> assignmentResponseDtoList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseDtoList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseDtoList;
    }

    public List<AssignmentResponseDto> getUndoneAssignmentsByStudent(int studentId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(studentId);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByStudent(studentId);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseDto> assignmentResponseDtoList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseDtoList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseDtoList;
    }

    public List<AssignmentResponseDto> getDoneAssignmentsByGroupIdAndUserId(int groupId, int userId) {
        List<Assignment> assignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(groupId, userId);
        List<AssignmentResponseDto> assignmentResponseDtoList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseDtoList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseDtoList;
    }

    public List<AssignmentResponseDto> getDoneAssignmentsByStudent(int studentId) {
        List<Assignment> assignmentList = assignmentRepository.getDoneAssignmentsByStudent(studentId);
        List<AssignmentResponseDto> assignmentResponseDtoList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseDtoList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseDtoList;
    }

    public List<AssignmentResponseDto> getExpiredUndoneAssignmentsByGroupIdAndUserId(int groupId, int userId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(groupId, userId);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(groupId, userId);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseDto> assignmentResponseDtoList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if (assignment.getCompletionDatetime().isBefore(OffsetDateTime.now())) {
                assignmentResponseDtoList.add(buildAssignmentResponse(assignment));
            }
        });
        return assignmentResponseDtoList;
    }

    public List<AssignmentResponseDto> getExpiredUndoneAssignmentsByStudent(int studentId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(studentId);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByStudent(studentId);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseDto> assignmentResponseDtoList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if (assignment.getCompletionDatetime().isBefore(OffsetDateTime.now())) {
                assignmentResponseDtoList.add(buildAssignmentResponse(assignment));
            }
        });
        return assignmentResponseDtoList;
    }

    public List<AssignmentResponseDto> getNonExpiredUndoneAssignmentsByGroupIdAndUserId(int groupId, int userId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(groupId, userId);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(groupId, userId);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseDto> assignmentResponseDtoList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if (assignment.getCompletionDatetime().isAfter(OffsetDateTime.now())) {
                assignmentResponseDtoList.add(buildAssignmentResponse(assignment));
            }
        });
        return assignmentResponseDtoList;
    }

    public List<AssignmentResponseDto> getNonExpiredUndoneAssignmentsByStudent(int studentId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(studentId);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByStudent(studentId);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseDto> assignmentResponseDtoList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if (assignment.getCompletionDatetime().isAfter(OffsetDateTime.now())) {
                assignmentResponseDtoList.add(buildAssignmentResponse(assignment));
            }
        });
        return assignmentResponseDtoList;
    }

    public List<AssignmentResponseDto> getAllAssignmentsByStudent(int userId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(userId);
        List<AssignmentResponseDto> assignmentResponseDtoList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseDtoList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseDtoList;
    }

    public List<AssignmentResponseExtendedDto> getAssignmentsByGroupIdExtended(int id) {
        List<Assignment> assignments = assignmentRepository.getAssignmentsByGroupId(id);
        List<AssignmentResponseExtendedDto> assignmentResponses = new ArrayList<>();
        for (Assignment assignment : assignments) {
            assignmentResponses.add(buildAssignmentResponseExtended(assignment));
        }
        return assignmentResponses;
    }

    public List<AssignmentResponseExtendedDto> getUncheckedAssignmentsByGroupExtended(int groupId) {
        List<Assignment> assignmentsInGroup = assignmentRepository.getAssignmentsByGroupId(groupId);
        List<AssignmentResponseExtendedDto> uncheckedAssignments = new ArrayList<>();
        for (Assignment assignment : assignmentsInGroup) {
            int assignmentId = assignment.getId();
            int solutionsCount = solutionRepository.countSolutionsByAssignmentId(assignmentId);
            int evaluationsCount = evaluationRepository.countEvaluationsByAssignment(assignmentId);
            if (solutionsCount != 0 && solutionsCount - evaluationsCount > 0) {
                uncheckedAssignments.add(buildAssignmentResponseExtended(assignment));
            }
        }
        return uncheckedAssignments;
    }

    public List<AssignmentResponseExtendedDto> getAllAssignmentsByGroupIdAndUserIdExtended(int groupId, int userId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(groupId, userId);
        List<AssignmentResponseExtendedDto> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
        });
        return assignmentResponseList;
    }

    public List<AssignmentResponseExtendedDto> getUndoneAssignmentsByGroupIdAndUserIdExtended(int groupId, int userId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(groupId, userId);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(groupId, userId);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseExtendedDto> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
        });
        return assignmentResponseList;
    }

    public List<AssignmentResponseExtendedDto> getUndoneAssignmentsByStudentExtended(int studentId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(studentId);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByStudent(studentId);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseExtendedDto> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
        });
        return assignmentResponseList;
    }

    public List<AssignmentResponseExtendedDto> getDoneAssignmentsByGroupIdAndUserIdExtended(int groupId, int userId) {
        List<Assignment> assignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(groupId, userId);
        List<AssignmentResponseExtendedDto> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
        });
        return assignmentResponseList;
    }

    public List<AssignmentResponseExtendedDto> getDoneAssignmentsByStudentExtended(int studentId) {
        List<Assignment> assignmentList = assignmentRepository.getDoneAssignmentsByStudent(studentId);
        List<AssignmentResponseExtendedDto> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
        });
        return assignmentResponseList;
    }

    public List<AssignmentResponseExtendedDto> getExpiredUndoneAssignmentsByGroupIdAndUserIdExtended(int groupId, int userId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(groupId, userId);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(groupId, userId);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseExtendedDto> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if (assignment.getCompletionDatetime().isBefore(OffsetDateTime.now())) {
                assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
            }
        });
        return assignmentResponseList;
    }

    public List<AssignmentResponseExtendedDto> getExpiredUndoneAssignmentsByStudentExtended(int studentId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(studentId);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByStudent(studentId);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseExtendedDto> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if (assignment.getCompletionDatetime().isBefore(OffsetDateTime.now())) {
                assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
            }
        });
        return assignmentResponseList;
    }

    public List<AssignmentResponseExtendedDto> getNonExpiredUndoneAssignmentsByGroupIdAndUserIdExtended(int groupId, int userId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(groupId, userId);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(groupId, userId);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseExtendedDto> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if (assignment.getCompletionDatetime().isAfter(OffsetDateTime.now())) {
                assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
            }
        });
        return assignmentResponseList;
    }

    public List<AssignmentResponseExtendedDto> getNonExpiredUndoneAssignmentsByStudentExtended(int studentId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(studentId);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByStudent(studentId);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseExtendedDto> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if (assignment.getCompletionDatetime().isAfter(OffsetDateTime.now())) {
                assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
            }
        });
        return assignmentResponseList;
    }

    public List<AssignmentResponseExtendedDto> getAllAssignmentsByStudentExtended(int userId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(userId);
        List<AssignmentResponseExtendedDto> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
        });
        return assignmentResponseList;
    }

    private AssignmentResponseDto buildAssignmentResponse(Assignment assignment) {
        return AssignmentResponseDto.builder()
                .id(assignment.getId())
                .userId((assignment.getUser() == null) ? null : assignment.getUser().getId())
                .groupId((assignment.getGroup() == null) ? null : assignment.getGroup().getId())
                .taskDescription(assignment.getTaskDescription())
                .creationDatetime(assignment.getCreationDatetime())
                .lastModifiedDatetime(assignment.getLastModifiedDatetime())
                .completionDatetime(assignment.getCompletionDatetime())
                .title(assignment.getTitle())
                .visible(assignment.getVisible())
                .max_points(assignment.getMax_points())
                .auto_penalty(assignment.getAuto_penalty())
                .build();
    }

    private AssignmentResponseExtendedDto buildAssignmentResponseExtended(Assignment assignment) {
        User user = assignment.getUser();
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
        Group group = assignment.getGroup();
        GroupResponseDto groupResponseDto = GroupResponseDto.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .color(group.getColor())
                .isArchived(group.isArchived())
                .build();
        return AssignmentResponseExtendedDto.builder()
                .id(assignment.getId())
                .user(userResponseDto)
                .group(groupResponseDto)
                .taskDescription(assignment.getTaskDescription())
                .creationDatetime(assignment.getCreationDatetime())
                .lastModifiedDatetime(assignment.getLastModifiedDatetime())
                .completionDatetime(assignment.getCompletionDatetime())
                .title(assignment.getTitle())
                .visible(assignment.getVisible())
                .max_points(assignment.getMax_points())
                .auto_penalty(assignment.getAuto_penalty())
                .build();
    }

    private AssignmentResponseCalendarDto buildAssignmentResponseCalendar(Assignment assignment, CalendarStatus status) {
        return AssignmentResponseCalendarDto.builder()
                .id(assignment.getId())
                .userId(assignment.getUser().getId())
                .groupId(assignment.getGroup().getId())
                .taskDescription(assignment.getTaskDescription())
                .creationDatetime(assignment.getCreationDatetime())
                .completionDatetime(assignment.getCompletionDatetime())
                .lastModifiedDatetime(assignment.getLastModifiedDatetime())
                .title(assignment.getTitle())
                .visible(assignment.getVisible())
                .max_points(assignment.getMax_points())
                .auto_penalty(assignment.getAuto_penalty())
                .status(status)
                .build();
    }

    public boolean checkForSolutionToAssignment(int assignmentId) {
        return assignmentRepository.checkForSolutionToAssignment(assignmentId) != 0;
    }

    public boolean checkForFileToAssignment(int assignmentId) {
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(assignmentId);
        if (assignmentOptional.isPresent()) {
            Assignment assignment = assignmentOptional.get();
            return !assignment.getFiles().isEmpty();
        } else return false;
    }

    public List<AssignmentResponseCalendarDto> getAssignmentsByStudentCalendar(int studentId) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(studentId);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByStudent(studentId);
        assignmentList.removeAll(doneAssignmentList);
        List<Assignment> expiredUndoneAssignmentsList = new ArrayList<>();
        List<Assignment> nonExpiredUndoneAssignmentsList = new ArrayList<>();
        for (Assignment assignment : assignmentList) {
            if (assignment.getCompletionDatetime().isBefore(OffsetDateTime.now())) {
                expiredUndoneAssignmentsList.add(assignment);
            } else {
                nonExpiredUndoneAssignmentsList.add(assignment);
            }
        }
        List<AssignmentResponseCalendarDto> response = new ArrayList<>();
        for (Assignment assignment : doneAssignmentList) {
            response.add(buildAssignmentResponseCalendar(assignment, CalendarStatus.DONE));
        }
        for (Assignment assignment : expiredUndoneAssignmentsList) {
            response.add(buildAssignmentResponseCalendar(assignment, CalendarStatus.LATE));
        }
        for (Assignment assignment : nonExpiredUndoneAssignmentsList) {
            response.add(buildAssignmentResponseCalendar(assignment, CalendarStatus.TODO));
        }
        response.sort(Comparator.comparing(AssignmentResponseCalendarDto::getCompletionDatetime));
        return response;
    }
}
