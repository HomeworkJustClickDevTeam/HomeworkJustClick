package pl.HomeworkJustClick.Backend.Services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Assignment;
import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Repositories.*;
import pl.HomeworkJustClick.Backend.Responses.AssignmentResponse;

import java.time.OffsetDateTime;
import java.util.ArrayList;
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
    public AssignmentResponse getById(int id) {
        Optional<Assignment> assignmentOptional = assignmentRepository.findById(id);
        return assignmentOptional.map(this::buildAssignmentResponse).orElse(null);
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
            List<User> userList = userRepository.getGroupTeachersByGroup(group_id);
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
                return AssignmentResponse.builder().build();
            }
        } else {
            return AssignmentResponse.builder().forbidden(true).build();
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
        if (!updatedAssignment.getUser().equals(assignment.getUser())) {
            assignment.setUser(updatedAssignment.getUser());
        }
        if (!updatedAssignment.getGroup().equals(assignment.getGroup())) {
            assignment.setGroup(updatedAssignment.getGroup());
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
        if(assignmentRepository.findById(id).isPresent() && points >= 0) {
            Assignment assignment = assignmentRepository.findById(id).get();
            assignment.setMax_points(points);
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
        for(Assignment assignment : assignments) {
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
        List<AssignmentResponse> ucheckedAssignments = new ArrayList<>();
        for(Assignment assignment : assignmentsInGroup) {
            int assignment_id = assignment.getId();
            int solutions_count = solutionRepository.countSolutionsByAssignmentId(assignment_id);
            int evaluations_count = evaluationRepository.countEvaluationsByAssignment(assignment_id);
            if (solutions_count != 0 && solutions_count - evaluations_count > 0){
                ucheckedAssignments.add(
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
        return ucheckedAssignments;
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
        List<Assignment> assignmentList = assignmentRepository.getUndoneAssignmentsByGroupIdAndUserId(group_id, user_id);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponse> getUndoneAssignmentsByStudent(int student_id) {
        List<Assignment> assignmentList = assignmentRepository.getUndoneAssignmentsByStudent(student_id);
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
        List<Assignment> assignmentList = assignmentRepository.getExpiredUndoneAssignmentsByGroupIdAndUserId(group_id, user_id);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponse> getExpiredUndoneAssignmentsByStudent(int student_id){
        List<Assignment> assignmentList = assignmentRepository.getExpiredUndoneAssignmentsByStudent(student_id);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponse> getNonExpiredUndoneAssignmentsByGroupIdAndUserId(int group_id, int user_id) {
        List<Assignment> assignmentList = assignmentRepository.getNonExpiredUndoneAssignmentsByGroupIdAndUserId(group_id, user_id);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponse> getNonExpiredUndoneAssignmentsByStudent(int student_id) {
        List<Assignment> assignmentList = assignmentRepository.getNonExpiredUndoneAssignmentsByStudent(student_id);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponse(assignment));
        });
        return assignmentResponseList;
    }

    @Override
    public List<AssignmentResponse> getAllAssignmentsByStudent(int user_id){
        List<Assignment> assignmentList = assignmentRepository.getAllAssignmentsByUser(user_id);
        List<AssignmentResponse> assignmentResponseList = new ArrayList<>();
        assignmentList.forEach(assignment -> {
            assignmentResponseList.add(buildAssignmentResponse(assignment));
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
                .build();
    }

}
