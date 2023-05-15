package pl.HomeworkJustClick.Backend.Services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Assignment;
import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Repositories.AssignmentRepository;
import pl.HomeworkJustClick.Backend.Repositories.GroupRepository;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;
import pl.HomeworkJustClick.Backend.Responses.AssignmentResponse;

import java.time.OffsetDateTime;
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


}
