package pl.HomeworkJustClick.Backend.Services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Assignment;
import pl.HomeworkJustClick.Backend.Entities.Evaluation;
import pl.HomeworkJustClick.Backend.Entities.Solution;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Repositories.EvaluationRepository;
import pl.HomeworkJustClick.Backend.Repositories.SolutionRepository;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;
import pl.HomeworkJustClick.Backend.Responses.EvaluationResponse;
import pl.HomeworkJustClick.Backend.Responses.SolutionResponse;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluationServiceImplement implements EvaluationService {
    EntityManager entityManager;
    public EvaluationServiceImplement(EntityManager entityManager){this.entityManager = entityManager;}

    @Autowired
    EvaluationRepository evaluationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SolutionRepository solutionRepository;

    @Override
    public List<Evaluation> getAll() {
        return evaluationRepository.findAll();
    }

    @Override
    public Optional<Evaluation> getById(int id) {
        return evaluationRepository.findById(id);
    }

    @Override
    public EvaluationResponse add(Evaluation evaluation) {
        entityManager.persist(evaluation);
        return EvaluationResponse.builder()
                .id(evaluation.getId())
                .result(evaluation.getResult())
                .userId(evaluation.getUser().getId())
                .solutionId(evaluation.getSolution().getId())
                .groupId(evaluation.getGroup().getId())
                .creationDatetime(evaluation.getCreationDatetime())
                .lastModifiedDatetime(evaluation.getLastModifiedDatetime())
                .comment(evaluation.getComment())
                .grade(evaluation.getGrade())
                .build();
    }

    @Override
    @Transactional
    public EvaluationResponse addWithUserAndSolution(Evaluation evaluation, int user_id, int solution_id) {
        Optional<User> user = userRepository.findById(user_id);
        Optional<Solution> solution = solutionRepository.findById(solution_id);
        if(user.isPresent() && solution.isPresent()) {
            evaluation.setSolution(solution.get());
            evaluation.setUser(user.get());
            evaluation.setGroup(solution.get().getGroup());
            entityManager.persist(evaluation);
            return EvaluationResponse.builder()
                    .id(evaluation.getId())
                    .result(evaluation.getResult())
                    .userId(evaluation.getUser().getId())
                    .solutionId(evaluation.getSolution().getId())
                    .groupId(evaluation.getGroup().getId())
                    .creationDatetime(evaluation.getCreationDatetime())
                    .lastModifiedDatetime(evaluation.getLastModifiedDatetime())
                    .comment(evaluation.getComment())
                    .grade(evaluation.getGrade())
                    .build();
        } else {
            return EvaluationResponse.builder().build();
        }
    }

    @Override
    public Boolean delete(int id) {
        if(evaluationRepository.existsById(id)){
            evaluationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean changeResultById(int id, Double result) {
        if(evaluationRepository.findById(id).isPresent()){
            Evaluation evaluation = evaluationRepository.findById(id).get();
            evaluation.setResult(result);
            evaluationRepository.save(evaluation);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public Boolean changeUserById(int id, int userId) {
        if(evaluationRepository.findById(id).isPresent() && userRepository.findById(userId).isPresent()){
            Evaluation evaluation = evaluationRepository.findById(id).get();
            User user = userRepository.findById(userId).get();
            evaluation.setUser(user);
            evaluationRepository.save(evaluation);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public Boolean changeSolutionById(int id, int solutionId) {
        if(evaluationRepository.findById(id).isPresent() && solutionRepository.findById(solutionId).isPresent()){
            Evaluation evaluation = evaluationRepository.findById(id).get();
            Solution solution = solutionRepository.findById(solutionId).get();
            evaluation.setSolution(solution);
            evaluationRepository.save(evaluation);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public Boolean changeCommentById(int id, String comment) {
        if(evaluationRepository.findById(id).isPresent()){
            Evaluation evaluation = evaluationRepository.findById(id).get();
            evaluation.setComment(comment);
            evaluationRepository.save(evaluation);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public Boolean changeGradeById(int id, Double grade) {
        if(evaluationRepository.findById(id).isPresent()){
            Evaluation evaluation = evaluationRepository.findById(id).get();
            evaluation.setGrade(grade);
            evaluationRepository.save(evaluation);
            return true;
        }
        else{
            return false;
        }
    }
}
