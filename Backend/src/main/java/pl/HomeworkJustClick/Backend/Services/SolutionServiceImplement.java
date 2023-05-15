package pl.HomeworkJustClick.Backend.Services;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Assignment;
import pl.HomeworkJustClick.Backend.Entities.Solution;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Repositories.AssignmentRepository;
import pl.HomeworkJustClick.Backend.Repositories.SolutionRepository;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;
import pl.HomeworkJustClick.Backend.Responses.SolutionResponse;

import java.util.List;
import java.util.Optional;

@Service
public class SolutionServiceImplement implements SolutionService{

    EntityManager entityManager;

    public SolutionServiceImplement(EntityManager entityManager){this.entityManager = entityManager;}
    @Autowired
    SolutionRepository solutionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AssignmentRepository assignmentRepository;
    @Override
    public List<Solution> getAll() {
        return solutionRepository.findAll();
    }

    @Override
    public Optional<Solution> getById(int id) {
        return solutionRepository.findById(id);
    }

    @Override
    public SolutionResponse add(Solution solution) {
        entityManager.persist(solution);
        return SolutionResponse.builder()
                .id(solution.getId())
                .userId(solution.getUser().getId())
                .assignmentId(solution.getAssignment().getId())
                .creationDateTime(solution.getCreationDatetime())
                .lastModifiedDatetime(solution.getLastModifiedDatetime())
                .build();
    }

    @Override
    public Boolean delete(int id) {
        if (solutionRepository.existsById(id)) {
            solutionRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
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

    @Override
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
}
