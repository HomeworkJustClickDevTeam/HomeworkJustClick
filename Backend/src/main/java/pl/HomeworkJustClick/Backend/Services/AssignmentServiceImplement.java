package pl.HomeworkJustClick.Backend.Services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class AssignmentServiceImplement implements AssignmentService {

    EntityManager entityManager;

    public AssignmentServiceImplement(EntityManager entityManager){this.entityManager = entityManager;}

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SolutionRepository solutionRepository;

    @Autowired
    EvaluationRepository evaluationRepository;

    @Override
    public List<Assignment> getAll() {
        return assignmentRepository.findAll();
    }


    @Override
    public Optional<Assignment> getById(int id) {
        return assignmentRepository.findById(id);
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
                .build();
    }

    @Override
    @Transactional
    public AssignmentResponse addWithUserAndGroup(Assignment assignment, int user_id, int group_id) {
        Optional<User> user = userRepository.findById(user_id);
        Optional<Group> group = groupRepository.findById(group_id);
        if(user.isPresent() && group.isPresent()) {
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
                    .build();
        } else {
            return AssignmentResponse.builder().build();
        }
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
                                .build()
                );
            }
        }
        return ucheckedAssignments;
    }

}
