package pl.HomeworkJustClick.Backend.Services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Assignment;
import pl.HomeworkJustClick.Backend.Repositories.AssignmentRepository;
import pl.HomeworkJustClick.Backend.Responses.AssignmentResponse;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AssignmentServiceImplement implements AssignmentService {

    EntityManager entityManager;

    public AssignmentServiceImplement(EntityManager entityManager){this.entityManager = entityManager;}

    @Autowired
    AssignmentRepository assignmentRepository;
    @Override
    public List<Assignment> getAll() {
        return assignmentRepository.findAll();
    }


    @Override
    public Assignment getById(int id) {
        if(assignmentRepository.findById(id).isPresent()){
            return assignmentRepository.findById(id).get();
        }
        else{
            return null;
        }
    }

    @Override
    public Boolean add(Assignment assignment) {
        assignmentRepository.save(assignment);
        return true;
    }

    @Override
    public Boolean delete(int id) {
        try{
            assignmentRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e){
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
            return null;
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
            return null;
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
            return null;
        }
    }
}
