package pl.HomeworkJustClick.Backend.Services;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Evaluation;
import pl.HomeworkJustClick.Backend.Entities.Solution;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Repositories.EvaluationRepository;
import pl.HomeworkJustClick.Backend.Repositories.SolutionRepository;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;
import pl.HomeworkJustClick.Backend.Responses.EvaluationResponse;

import java.util.List;

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
    public Evaluation getById(int id) {
        if(evaluationRepository.findById(id).isPresent()){
            return evaluationRepository.findById(id).get();
        }
        else {
            return null;
        }
    }

    @Override
    public EvaluationResponse add(Evaluation evaluation) {
        entityManager.persist(evaluation);
        return EvaluationResponse.builder()
                .id(evaluation.getId())
                .result(evaluation.getResult())
                .userId(evaluation.getUser().getId())
                .solutionId(evaluation.getSolution().getId())
                .creationDatetime(evaluation.getCreationDatetime())
                .lastModifiedDatetime(evaluation.getLastModifiedDatetime())
                .comment(evaluation.getComment())
                .grade(evaluation.getGrade())
                .build();
    }

    @Override
    public Boolean delete(int id) {
        try{
            evaluationRepository.deleteById(id);
            return true;
        }
        catch (IllegalArgumentException e){
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
            return null;
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
            return null;
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
            return null;
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
            return null;
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
            return null;
        }
    }
}
