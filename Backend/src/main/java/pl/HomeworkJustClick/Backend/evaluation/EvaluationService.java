package pl.HomeworkJustClick.Backend.evaluation;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.group.Group;
import pl.HomeworkJustClick.Backend.group.GroupResponseDto;
import pl.HomeworkJustClick.Backend.infrastructure.exception.EntityNotFoundException;
import pl.HomeworkJustClick.Backend.solution.Solution;
import pl.HomeworkJustClick.Backend.solution.SolutionRepository;
import pl.HomeworkJustClick.Backend.solution.SolutionResponseDto;
import pl.HomeworkJustClick.Backend.user.User;
import pl.HomeworkJustClick.Backend.user.UserRepository;
import pl.HomeworkJustClick.Backend.user.UserResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EntityManager entityManager;

    private final EvaluationRepository evaluationRepository;

    private final UserRepository userRepository;

    private final SolutionRepository solutionRepository;

    public Evaluation findById(Integer id) {
        return evaluationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evaluation with id = " + id + " not found"));
    }

    public List<EvaluationResponseDto> getAll() {
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        List<EvaluationResponseDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponse(evaluation));
        });
        return responseList;
    }

    public EvaluationResponseDto getById(int id) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(id);
        return evaluationOptional.map(this::buildEvaluationResponse).orElse(null);
    }

    @Transactional
    public EvaluationResponseDto add(Evaluation evaluation) {
        entityManager.persist(evaluation);
        return buildEvaluationResponse(evaluation);
    }

    @Transactional
    public EvaluationResponseDto addWithUserAndSolution(Evaluation evaluation, int user_id, int solution_id) {
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
                return EvaluationResponseDto.builder().forbidden(true).build();
            }
        } else {
            return EvaluationResponseDto.builder().build();
        }
    }

    public List<EvaluationResponseExtendedDto> getAllExtended() {
        List<Evaluation> evaluationList = evaluationRepository.findAll();
        List<EvaluationResponseExtendedDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponseExtended(evaluation));
        });
        return responseList;
    }

    public EvaluationResponseExtendedDto getByIdExtended(int id) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.findById(id);
        return evaluationOptional.map(this::buildEvaluationResponseExtended).orElse(null);
    }

    @Transactional
    public EvaluationResponseExtendedDto addExtended(Evaluation evaluation) {
        entityManager.persist(evaluation);
        return buildEvaluationResponseExtended(evaluation);
    }

    @Transactional
    public EvaluationResponseExtendedDto addWithUserAndSolutionExtended(Evaluation evaluation, int user_id, int solution_id) {
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
                return EvaluationResponseExtendedDto.builder().forbidden(true).build();
            }
        } else {
            return EvaluationResponseExtendedDto.builder().build();
        }
    }

    @Transactional
    public Boolean delete(int id) {
        if(evaluationRepository.existsById(id)){
            evaluationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
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

    @Transactional
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

    @Transactional
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


    @Transactional
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

    public List<EvaluationResponseDto> getAllEvaluationsByStudent(int student_id) {
        List<Evaluation> evaluationList = evaluationRepository.getAllEvaluationsByStudent(student_id);
        List<EvaluationResponseDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponse(evaluation));
        });
        return responseList;
    }

    public List<EvaluationResponseDto> getAllEvaluationsByStudentInGroup(int student_id, int group_id) {
        List<Evaluation> evaluationList = evaluationRepository.getAllEvaluationsByStudentInGroup(student_id, group_id);
        List<EvaluationResponseDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponse(evaluation));
        });
        return responseList;
    }

    public List<EvaluationResponseDto> getAllEvaluationsByAssignment(int assignment_id) {
        List<Evaluation> evaluationList = evaluationRepository.getEvaluationsByAssignment(assignment_id);
        List<EvaluationResponseDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponse(evaluation));
        });
        return responseList;
    }

    public EvaluationResponseDto getEvaluationBySolution(int solution_id) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.getEvaluationBySolution(solution_id);
        return evaluationOptional.map(this::buildEvaluationResponse).orElse(null);
    }

    public List<EvaluationResponseExtendedDto> getAllEvaluationsByStudentExtended(int student_id) {
        List<Evaluation> evaluationList = evaluationRepository.getAllEvaluationsByStudent(student_id);
        List<EvaluationResponseExtendedDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponseExtended(evaluation));
        });
        return responseList;
    }

    public List<EvaluationResponseExtendedDto> getAllEvaluationsByStudentInGroupExtended(int student_id, int group_id) {
        List<Evaluation> evaluationList = evaluationRepository.getAllEvaluationsByStudentInGroup(student_id, group_id);
        List<EvaluationResponseExtendedDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponseExtended(evaluation));
        });
        return responseList;
    }

    public List<EvaluationResponseExtendedDto> getAllEvaluationsByAssignmentExtended(int assignment_id) {
        List<Evaluation> evaluationList = evaluationRepository.getEvaluationsByAssignment(assignment_id);
        List<EvaluationResponseExtendedDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponseExtended(evaluation));
        });
        return responseList;
    }

    public EvaluationResponseExtendedDto getEvaluationBySolutionExtended(int solution_id) {
        Optional<Evaluation> evaluationOptional = evaluationRepository.getEvaluationBySolution(solution_id);
        return evaluationOptional.map(this::buildEvaluationResponseExtended).orElse(null);
    }

    public Boolean checkForEvaluationToSolution(int solution_id) {
        return evaluationRepository.checkForEvaluationToSolution(solution_id) != 0;
    }

    private EvaluationResponseDto buildEvaluationResponse(Evaluation evaluation) {
        return EvaluationResponseDto.builder()
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

    private EvaluationResponseExtendedDto buildEvaluationResponseExtended(Evaluation evaluation) {
        User user = evaluation.getUser();
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
        Solution solution = evaluation.getSolution();
        SolutionResponseDto solutionResponseDto = SolutionResponseDto.builder()
                .id(solution.getId())
                .userId(solution.getUser().getId())
                .groupId(solution.getGroup().getId())
                .assignmentId(solution.getAssignment().getId())
                .creationDateTime(solution.getCreationDatetime())
                .lastModifiedDatetime(solution.getLastModifiedDatetime())
                .comment(solution.getComment())
                .build();
        Group group = evaluation.getGroup();
        GroupResponseDto groupResponseDto = GroupResponseDto.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .color(group.getColor())
                .isArchived(group.isArchived())
                .build();
        return EvaluationResponseExtendedDto.builder()
                .id(evaluation.getId())
                .result(evaluation.getResult())
                .user(userResponseDto)
                .solution(solutionResponseDto)
                .group(groupResponseDto)
                .creationDatetime(evaluation.getCreationDatetime())
                .lastModifiedDatetime(evaluation.getLastModifiedDatetime())
                .grade(evaluation.getGrade())
                .build();
    }
}
