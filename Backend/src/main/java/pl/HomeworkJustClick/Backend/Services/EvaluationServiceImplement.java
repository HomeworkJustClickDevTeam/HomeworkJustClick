package pl.HomeworkJustClick.Backend.Services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Evaluation;
import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.Solution;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Repositories.EvaluationRepository;
import pl.HomeworkJustClick.Backend.Repositories.SolutionRepository;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;
import pl.HomeworkJustClick.Backend.Responses.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImplement implements EvaluationService {

    private final EntityManager entityManager;

    private final EvaluationRepository evaluationRepository;

    private final UserRepository userRepository;

    private final SolutionRepository solutionRepository;

    @Override
    public List<EvaluationResponse> getAll() {
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        List<EvaluationResponse> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponse(evaluation));
        });
        return responseList;
    }

    @Override
    public EvaluationResponse getById(int id) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(id);
        return evaluationOptional.map(this::buildEvaluationResponse).orElse(null);
    }

    @Override
    public EvaluationResponse add(Evaluation evaluation) {
        entityManager.persist(evaluation);
        return buildEvaluationResponse(evaluation);
    }

    @Override
    @Transactional
    public EvaluationResponse addWithUserAndSolution(Evaluation evaluation, int user_id, int solution_id) {
        Optional<User> user = userRepository.findById(user_id);
        Optional<Solution> solution = solutionRepository.findById(solution_id);
        if(user.isPresent() && solution.isPresent() ) {
            List<User> userList = userRepository.getTeachersByGroupId(solution.get().getGroup().getId());
            AtomicBoolean ok = new AtomicBoolean(false);
            userList.forEach(user1 -> {
                if (user1.getId() == user_id) {
                    ok.set(true);
                }
            });
            if(ok.get() && !checkForEvaluationToSolution(solution_id)) {
                var assignment = solution.get().getAssignment();
                if (assignment.getAuto_penalty() > 0) {
                    if (assignment.getCompletionDatetime().isBefore(solution.get().getCreationDatetime())) {
                        double penalty = evaluation.getResult() * ((double) assignment.getAuto_penalty() / 100);
                        evaluation.setResult(evaluation.getResult() - penalty);
                    }
                }
                evaluation.setSolution(solution.get());
                evaluation.setUser(user.get());
                evaluation.setGroup(solution.get().getGroup());
                entityManager.persist(evaluation);
                return buildEvaluationResponse(evaluation);
            } else {
                return EvaluationResponse.builder().forbidden(true).build();
            }
        } else {
            return EvaluationResponse.builder().build();
        }
    }

    @Override
    public List<EvaluationResponseExtended> getAllExtended() {
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        List<EvaluationResponseExtended> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponseExtended(evaluation));
        });
        return responseList;
    }

    @Override
    public EvaluationResponseExtended getByIdExtended(int id) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(id);
        return evaluationOptional.map(this::buildEvaluationResponseExtended).orElse(null);
    }

    @Override
    public EvaluationResponseExtended addExtended(Evaluation evaluation) {
        entityManager.persist(evaluation);
        return buildEvaluationResponseExtended(evaluation);
    }

    @Override
    @Transactional
    public EvaluationResponseExtended addWithUserAndSolutionExtended(Evaluation evaluation, int user_id, int solution_id) {
        Optional<User> user = userRepository.findById(user_id);
        Optional<Solution> solution = solutionRepository.findById(solution_id);
        if(user.isPresent() && solution.isPresent()) {
            List<User> userList = userRepository.getTeachersByGroupId(solution.get().getGroup().getId());
            AtomicBoolean ok = new AtomicBoolean(false);
            userList.forEach(user1 -> {
                if (user1.getId() == user_id) {
                    ok.set(true);
                }
            });
            if(ok.get() && !checkForEvaluationToSolution(solution_id)) {
                var assignment = solution.get().getAssignment();
                if (assignment.getAuto_penalty() > 0) {
                    if (assignment.getCompletionDatetime().isBefore(solution.get().getCreationDatetime())) {
                        double penalty = evaluation.getResult() * ((double) assignment.getAuto_penalty() / 100);
                        evaluation.setResult(evaluation.getResult() - penalty);
                    }
                }
                evaluation.setSolution(solution.get());
                evaluation.setUser(user.get());
                evaluation.setGroup(solution.get().getGroup());
                entityManager.persist(evaluation);
                return buildEvaluationResponseExtended(evaluation);
            } else {
                return EvaluationResponseExtended.builder().forbidden(true).build();
            }
        } else {
            return EvaluationResponseExtended.builder().build();
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

    @Override
    public List<EvaluationResponse> getAllEvaluationsByStudent(int student_id) {
        List<Evaluation> evaluationList = evaluationRepository.getAllEvaluationsByStudent(student_id);
        List<EvaluationResponse> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponse(evaluation));
        });
        return responseList;
    }

    @Override
    public List<EvaluationResponse> getAllEvaluationsByStudentInGroup(int student_id, int group_id){
        List<Evaluation> evaluationList = evaluationRepository.getAllEvaluationsByStudentInGroup(student_id, group_id);
        List<EvaluationResponse> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponse(evaluation));
        });
        return responseList;
    }

    @Override
    public List<EvaluationResponse> getAllEvaluationsByAssignment(int assignment_id){
        List<Evaluation> evaluationList = evaluationRepository.getEvaluationsByAssignment(assignment_id);
        List<EvaluationResponse> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponse(evaluation));
        });
        return responseList;
    }

    @Override
    public EvaluationResponse getEvaluationBySolution(int solution_id){
        Optional<Evaluation> evaluationOptional = evaluationRepository.getEvaluationBySolution(solution_id);
        return evaluationOptional.map(this::buildEvaluationResponse).orElse(null);
    }

    @Override
    public List<EvaluationResponseExtended> getAllEvaluationsByStudentExtended(int student_id) {
        List<Evaluation> evaluationList = evaluationRepository.getAllEvaluationsByStudent(student_id);
        List<EvaluationResponseExtended> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponseExtended(evaluation));
        });
        return responseList;
    }

    @Override
    public List<EvaluationResponseExtended> getAllEvaluationsByStudentInGroupExtended(int student_id, int group_id){
        List<Evaluation> evaluationList = evaluationRepository.getAllEvaluationsByStudentInGroup(student_id, group_id);
        List<EvaluationResponseExtended> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponseExtended(evaluation));
        });
        return responseList;
    }

    @Override
    public List<EvaluationResponseExtended> getAllEvaluationsByAssignmentExtended(int assignment_id){
        List<Evaluation> evaluationList = evaluationRepository.getEvaluationsByAssignment(assignment_id);
        List<EvaluationResponseExtended> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponseExtended(evaluation));
        });
        return responseList;
    }

    @Override
    public EvaluationResponseExtended getEvaluationBySolutionExtended(int solution_id){
        Optional<Evaluation> evaluationOptional = evaluationRepository.getEvaluationBySolution(solution_id);
        return evaluationOptional.map(this::buildEvaluationResponseExtended).orElse(null);
    }

    @Override
    public Boolean checkForEvaluationToSolution(int solution_id) {
        return evaluationRepository.checkForEvaluationToSolution(solution_id) != 0;
    }

    private EvaluationResponse buildEvaluationResponse(Evaluation evaluation) {
        return EvaluationResponse.builder()
                .id(evaluation.getId())
                .result(evaluation.getResult())
                .userId(evaluation.getUser().getId())
                .solutionId(evaluation.getSolution().getId())
                .groupId(evaluation.getGroup().getId())
                .creationDatetime(evaluation.getCreationDatetime())
                .lastModifiedDatetime(evaluation.getLastModifiedDatetime())
                .grade(evaluation.getGrade())
                .build();
    }

    private EvaluationResponseExtended buildEvaluationResponseExtended(Evaluation evaluation) {
        User user = evaluation.getUser();
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
        Solution solution = evaluation.getSolution();
        SolutionResponse solutionResponse = SolutionResponse.builder()
                .id(solution.getId())
                .userId(solution.getUser().getId())
                .groupId(solution.getGroup().getId())
                .assignmentId(solution.getAssignment().getId())
                .creationDateTime(solution.getCreationDatetime())
                .lastModifiedDatetime(solution.getLastModifiedDatetime())
                .comment(solution.getComment())
                .build();
        Group group = evaluation.getGroup();
        GroupResponse groupResponse = GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .color(group.getColor())
                .isArchived(group.isArchived())
                .build();
        return EvaluationResponseExtended.builder()
                .id(evaluation.getId())
                .result(evaluation.getResult())
                .user(userResponse)
                .solution(solutionResponse)
                .group(groupResponse)
                .creationDatetime(evaluation.getCreationDatetime())
                .lastModifiedDatetime(evaluation.getLastModifiedDatetime())
                .grade(evaluation.getGrade())
                .build();
    }
}
