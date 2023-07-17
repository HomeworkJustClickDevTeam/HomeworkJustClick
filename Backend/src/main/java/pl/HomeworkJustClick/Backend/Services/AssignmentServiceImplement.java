package pl.HomeworkJustClick.Backend.Services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Assignment;
import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Enums.CalendarStatus;
import pl.HomeworkJustClick.Backend.Repositories.*;
import pl.HomeworkJustClick.Backend.Responses.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImplement implements AssignmentService {

    private final EntityManager entityManager;

    private final AssignmentRepository assignmentRepository;

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    private final SolutionRepository solutionRepository;

    private final EvaluationRepository evaluationRepository;

    @Override
    public List<AssignmentResponse> getAll() {
        List<Assignment> assignmentList = assignmentRepository.findAll();
        List<AssignmentResponse> responseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            responseList.add(buildAssignmentResponse(assignment));
        });
        return responseList;
    }

    @Override
    public List<AssignmentResponseExtended> getAllExtended() {
        List<Assignment> assignmentList = assignmentRepository.findAll();
        List<AssignmentResponseExtended> responseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            responseList.add(buildAssignmentResponseExtended(assignment));
        });
        return responseList;
    }


    @Override
    public AssignmentResponse getById(int id) {
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(id);
        return assignmentOptional.map(this::buildAssignmentResponse).orElse(null);
    }

    @Override
    public AssignmentResponseExtended getByIdExtended(int id) {
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(id);
        return assignmentOptional.map(this::buildAssignmentResponseExtended).orElse(null);
    }

    @Override
    @Transactional
    public AssignmentResponse add(Assignment assignment) {
        entityManager.persist(assignment);
        return AssignmentResponse.builder()
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

    @Override
    @Transactional
    public AssignmentResponse addWithUserAndGroup(Assignment assignment, int user_id, int group_id) {
        Optional<User> user = userRepository.findById(user_id);
        Optional<Group> group = groupRepository.findById(group_id);
        if(user.isPresent() && group.isPresent()) {
            List<User> userList = userRepository.getTeachersByGroupId(group_id);
            AtomicBoolean ok = new AtomicBoolean(false);
            userList.forEach(user1 -> {
                if (user1.getId() == user_id) {
                    ok.set(true);
                }
            });
            if(ok.get()) {
                assignment.setGroup(group.get());
                assignment.setUser(user.get());
                entityManager.persist(assignment);
                return AssignmentResponse.builder()
                        .id(assignment.getId())
                        .userId(user_id)
                        .groupId(group_id)
                        .taskDescription(assignment.getTaskDescription())
                        .creationDatetime(assignment.getCreationDatetime())
                        .lastModifiedDatetime(assignment.getLastModifiedDatetime())
                        .completionDatetime(assignment.getCompletionDatetime())
                        .title(assignment.getTitle())
                        .visible(assignment.getVisible())
                        .max_points(assignment.getMax_points())
                        .build();
            } else {
                return AssignmentResponse.builder().forbidden(true).build();
            }
        } else {
            return AssignmentResponse.builder().build();
        }
    }

    @Override
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
        if (updatedAssignment.getMax_points() >= 0 && updatedAssignment.getMax_points() != assignment.getMax_points()) {
            assignment.setMax_points(updatedAssignment.getMax_points());
        }
        if (updatedAssignment.getAuto_penalty() >= 0 && updatedAssignment.getAuto_penalty() <= 100 && updatedAssignment.getAuto_penalty() != assignment.getAuto_penalty()) {
            assignment.setAuto_penalty(updatedAssignment.getAuto_penalty());
        }
        updatedAssignment.setUser(assignment.getUser());
        updatedAssignment.setGroup(assignment.getGroup());
        assignmentRepository.save(updatedAssignment);
        return true;
    }

    @Override
    public Boolean delete(int id) {
        if(assignmentRepository.existsById(id)) {
            assignmentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public List<AssignmentResponse> getAssignmentsByGroupId(int id) {
        List<Assignment> assignments = assignmentRepository.getAssignmentsByGroupId(id);
        List<AssignmentResponse> assignmentResponses = new ArrayList<>();
        for (Assignment assignment : assignments) {
            assignmentResponses.add(AssignmentResponse.builder()
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
        return assignmentResponses;
    }

    @Override
    public List<AssignmentResponse> getUncheckedAssignmentsByGroup(int group_id) {
        List<Assignment> assignmentsInGroup = assignmentRepository.getAssignmentsByGroupId(group_id);
        List<AssignmentResponse> uncheckedAssignments = new ArrayList<>();
        for(Assignment assignment : assignmentsInGroup) {
            int assignment_id = assignment.getId();
            int solutions_count = solutionRepository.countSolutionsByAssignmentId(assignment_id);
            int evaluations_count = evaluationRepository.countEvaluationsByAssignment(assignment_id);
            if (solutions_count != 0 && solutions_count - evaluations_count > 0){
                uncheckedAssignments.add(
                        AssignmentResponse.builder()
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

    @Override
    public List<AssignmentResponse> getAllAssignmentsByGroupIdAndUserId(int group_id, int user_id) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(group_id, user_id);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponse> getUndoneAssignmentsByGroupIdAndUserId(int group_id, int user_id){
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(group_id, user_id);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(group_id, user_id);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponse> getUndoneAssignmentsByStudent(int student_id) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(student_id);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByStudent(student_id);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponse> getDoneAssignmentsByGroupIdAndUserId(int group_id, int user_id) {
        List<Assignment> assignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(group_id, user_id);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponse> getDoneAssignmentsByStudent(int student_id){
        List<Assignment> assignmentList = assignmentRepository.getDoneAssignmentsByStudent(student_id);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponse> getExpiredUndoneAssignmentsByGroupIdAndUserId(int group_id, int user_id) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(group_id, user_id);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(group_id, user_id);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if(assignment.getCompletionDatetime().isBefore(OffsetDateTime.now())) {
                assignmentResponseList.add(buildAssignmentResponse(assignment));
            }
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponse> getExpiredUndoneAssignmentsByStudent(int student_id){
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(student_id);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByStudent(student_id);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if(assignment.getCompletionDatetime().isBefore(OffsetDateTime.now())) {
                assignmentResponseList.add(buildAssignmentResponse(assignment));
            }
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponse> getNonExpiredUndoneAssignmentsByGroupIdAndUserId(int group_id, int user_id) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(group_id, user_id);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(group_id, user_id);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if(assignment.getCompletionDatetime().isAfter(OffsetDateTime.now())) {
                assignmentResponseList.add(buildAssignmentResponse(assignment));
            }
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponse> getNonExpiredUndoneAssignmentsByStudent(int student_id) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(student_id);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByStudent(student_id);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if(assignment.getCompletionDatetime().isAfter(OffsetDateTime.now())) {
                assignmentResponseList.add(buildAssignmentResponse(assignment));
            }
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponse> getAllAssignmentsByStudent(int user_id){
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(user_id);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponseExtended> getAssignmentsByGroupIdExtended(int id) {
        List<Assignment> assignments = assignmentRepository.getAssignmentsByGroupId(id);
        List<AssignmentResponseExtended> assignmentResponses = new ArrayList<>();
        for(Assignment assignment : assignments) {
            assignmentResponses.add(buildAssignmentResponseExtended(assignment));
        }
        return assignmentResponses;
    }

    @Override
    public List<AssignmentResponseExtended> getUncheckedAssignmentsByGroupExtended(int group_id) {
        List<Assignment> assignmentsInGroup = assignmentRepository.getAssignmentsByGroupId(group_id);
        List<AssignmentResponseExtended> uncheckedAssignments = new ArrayList<>();
        for(Assignment assignment : assignmentsInGroup) {
            int assignment_id = assignment.getId();
            int solutions_count = solutionRepository.countSolutionsByAssignmentId(assignment_id);
            int evaluations_count = evaluationRepository.countEvaluationsByAssignment(assignment_id);
            if (solutions_count != 0 && solutions_count - evaluations_count > 0){
                uncheckedAssignments.add(buildAssignmentResponseExtended(assignment));
            }
        }
        return uncheckedAssignments;
    }

    @Override
    public List<AssignmentResponseExtended> getAllAssignmentsByGroupIdAndUserIdExtended(int group_id, int user_id) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(group_id, user_id);
        List<AssignmentResponseExtended> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponseExtended> getUndoneAssignmentsByGroupIdAndUserIdExtended(int group_id, int user_id){
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(group_id, user_id);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(group_id, user_id);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseExtended> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponseExtended> getUndoneAssignmentsByStudentExtended(int student_id) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(student_id);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByStudent(student_id);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseExtended> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponseExtended> getDoneAssignmentsByGroupIdAndUserIdExtended(int group_id, int user_id) {
        List<Assignment> assignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(group_id, user_id);
        List<AssignmentResponseExtended> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponseExtended> getDoneAssignmentsByStudentExtended(int student_id){
        List<Assignment> assignmentList = assignmentRepository.getDoneAssignmentsByStudent(student_id);
        List<AssignmentResponseExtended> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponseExtended> getExpiredUndoneAssignmentsByGroupIdAndUserIdExtended(int group_id, int user_id) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(group_id, user_id);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(group_id, user_id);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseExtended> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if(assignment.getCompletionDatetime().isBefore(OffsetDateTime.now())) {
                assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
            }
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponseExtended> getExpiredUndoneAssignmentsByStudentExtended(int student_id){
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(student_id);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByStudent(student_id);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseExtended> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if(assignment.getCompletionDatetime().isBefore(OffsetDateTime.now())) {
                assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
            }
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponseExtended> getNonExpiredUndoneAssignmentsByGroupIdAndUserIdExtended(int group_id, int user_id) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(group_id, user_id);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(group_id, user_id);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseExtended> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if(assignment.getCompletionDatetime().isAfter(OffsetDateTime.now())) {
                assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
            }
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponseExtended> getNonExpiredUndoneAssignmentsByStudentExtended(int student_id) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(student_id);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByStudent(student_id);
        assignmentList.removeAll(doneAssignmentList);
        List<AssignmentResponseExtended> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            if(assignment.getCompletionDatetime().isAfter(OffsetDateTime.now())) {
                assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
            }
        });
        return assignmentResponseList;
    }


    @Override
    public List<AssignmentResponseExtended> getAllAssignmentsByStudentExtended(int user_id){
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(user_id);
        List<AssignmentResponseExtended> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponseExtended(assignment));
        });
        return assignmentResponseList;
    }

    private AssignmentResponse buildAssignmentResponse(Assignment assignment) {
        return AssignmentResponse.builder()
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

    private AssignmentResponseExtended buildAssignmentResponseExtended(Assignment assignment) {
        User user = assignment.getUser();
        UserResponse userResponse = UserResponse.builder()
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
        GroupResponse groupResponse = GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .color(group.getColor())
                .isArchived(group.isArchived())
                .build();
        return AssignmentResponseExtended.builder()
                .id(assignment.getId())
                .user(userResponse)
                .group(groupResponse)
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

    private AssignmentResponseCalendar buildAssignmentResponseCalendar(Assignment assignment, CalendarStatus status) {
        return AssignmentResponseCalendar.builder()
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

    @Override
    public boolean checkForSolutionToAssignment(int assignment_id) {
        return assignmentRepository.checkForSolutionToAssignment(assignment_id) != 0;
    }

    @Override
    public boolean checkForFileToAssignment(int assignment_id) {
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(assignment_id);
        if (assignmentOptional.isPresent()) {
            Assignment assignment = assignmentOptional.get();
            return !assignment.getFiles().isEmpty();
        } else return false;
    }

    public List<AssignmentResponseCalendar> getAssignmentsByStudentCalendar(int student_id) {
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByStudent(student_id);
        List<Assignment> doneAssignmentList = assignmentRepository.getDoneAssignmentsByStudent(student_id);
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
        List<AssignmentResponseCalendar> response = new ArrayList<>();
        for (Assignment assignment : doneAssignmentList) {
            response.add(buildAssignmentResponseCalendar(assignment, CalendarStatus.DONE));
        }
        for (Assignment assignment : expiredUndoneAssignmentsList) {
            response.add(buildAssignmentResponseCalendar(assignment, CalendarStatus.LATE));
        }
        for (Assignment assignment : nonExpiredUndoneAssignmentsList) {
            response.add(buildAssignmentResponseCalendar(assignment, CalendarStatus.TODO));
        }
        response.sort(Comparator.comparing(AssignmentResponseCalendar::getCompletionDatetime));
        return response;
    }
}
