package pl.homeworkjustclick.evaluation;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.group.Group;
import pl.homeworkjustclick.group.GroupResponseDto;
import pl.homeworkjustclick.group.GroupService;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;
import pl.homeworkjustclick.infrastructure.exception.InvalidArgumentException;
import pl.homeworkjustclick.notification.NotificationCreateService;
import pl.homeworkjustclick.solution.Solution;
import pl.homeworkjustclick.solution.SolutionRepository;
import pl.homeworkjustclick.solution.SolutionResponseDto;
import pl.homeworkjustclick.solution.SolutionService;
import pl.homeworkjustclick.user.User;
import pl.homeworkjustclick.user.UserRepository;
import pl.homeworkjustclick.user.UserResponseDto;
import pl.homeworkjustclick.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class EvaluationService {
    private final EntityManager entityManager;
    private final EvaluationRepository repository;
    private final UserRepository userRepository;
    private final SolutionRepository solutionRepository;
    private final UserService userService;
    private final SolutionService solutionService;
    private final GroupService groupService;
    private final EvaluationMapper mapper;
    private final NotificationCreateService notificationCreateService;

    public Evaluation findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evaluation with id = " + id + " not found"));
    }

    public List<EvaluationResponseDto> getAll() {
        List<Evaluation> evaluationList = repository.findAll();
        List<EvaluationResponseDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponse(evaluation));
        });
        return responseList;
    }

    public EvaluationResponseDto getById(int id) {
        Optional<Evaluation> evaluationOptional = repository.findById(id);
        return evaluationOptional.map(this::buildEvaluationResponse).orElse(null);
    }

    public List<EvaluationResponseDto> findAllByGroupId(Integer groupId) {
        return repository.findAllByGroupId(groupId)
                .stream().map(mapper::map).toList();
    }

    @Transactional
    public EvaluationResponseDto add(Evaluation evaluation) {
        entityManager.persist(evaluation);
        return buildEvaluationResponse(evaluation);
    }

    @Transactional
    public EvaluationResponseExtendedDto create(EvaluationDto evaluationDto) {
        if (repository.existsBySolutionId(evaluationDto.getSolutionId())) {
            throw new InvalidArgumentException("Evaluation with solutionId = " + evaluationDto.getSolutionId() + " already exists");
        }
        validateDto(evaluationDto);
        var evaluation = mapper.map(evaluationDto);
        setRelationFields(evaluation, evaluationDto);
        notificationCreateService.createEvaluationNotification(evaluation.getSolution().getUser(), evaluation.getSolution().getAssignment(), evaluation.getGroup());
        return mapper.mapExtended(repository.save(evaluation));
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
        List<Evaluation> evaluationList = repository.findAll();
        List<EvaluationResponseExtendedDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponseExtended(evaluation));
        });
        return responseList;
    }

    public EvaluationResponseExtendedDto getByIdExtended(int id) {
        Optional<Evaluation> evaluationOptional = repository.findById(id);
        return evaluationOptional.map(this::buildEvaluationResponseExtended).orElse(null);
    }


    public List<EvaluationResponseDto> getAllEvaluationsByStudent(int student_id) {
        List<Evaluation> evaluationList = repository.getAllEvaluationsByStudent(student_id);
        List<EvaluationResponseDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponse(evaluation));
        });
        return responseList;
    }

    public List<EvaluationResponseDto> getAllEvaluationsByStudentInGroup(int student_id, int group_id) {
        List<Evaluation> evaluationList = repository.getAllEvaluationsByStudentInGroup(student_id, group_id);
        List<EvaluationResponseDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponse(evaluation));
        });
        return responseList;
    }

    public List<Evaluation> getEvaluationsByAssignment(Integer assignmentId) {
        return repository.getEvaluationsByAssignment(assignmentId);
    }

    public List<EvaluationResponseDto> getAllEvaluationsByAssignment(int assignment_id) {
        List<Evaluation> evaluationList = repository.getEvaluationsByAssignment(assignment_id);
        List<EvaluationResponseDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponse(evaluation));
        });
        return responseList;
    }

    public EvaluationResponseDto getEvaluationBySolution(int solution_id) {
        Optional<Evaluation> evaluationOptional = repository.getEvaluationBySolution(solution_id);
        return evaluationOptional.map(this::buildEvaluationResponse).orElse(null);
    }

    public List<EvaluationResponseExtendedDto> getAllEvaluationsByStudentExtended(int student_id) {
        List<Evaluation> evaluationList = repository.getAllEvaluationsByStudent(student_id);
        List<EvaluationResponseExtendedDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponseExtended(evaluation));
        });
        return responseList;
    }

    public List<EvaluationResponseExtendedDto> getAllEvaluationsByStudentInGroupExtended(int student_id, int group_id) {
        List<Evaluation> evaluationList = repository.getAllEvaluationsByStudentInGroup(student_id, group_id);
        List<EvaluationResponseExtendedDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponseExtended(evaluation));
        });
        return responseList;
    }

    public List<EvaluationResponseExtendedDto> getAllEvaluationsByAssignmentExtended(int assignment_id) {
        List<Evaluation> evaluationList = repository.getEvaluationsByAssignment(assignment_id);
        List<EvaluationResponseExtendedDto> responseList = new ArrayList<>();
        evaluationList.forEach(evaluation -> {
            responseList.add(buildEvaluationResponseExtended(evaluation));
        });
        return responseList;
    }

    public EvaluationResponseExtendedDto getEvaluationBySolutionExtended(int solution_id) {
        Optional<Evaluation> evaluationOptional = repository.getEvaluationBySolution(solution_id);
        return evaluationOptional.map(this::buildEvaluationResponseExtended).orElse(null);
    }

    public List<EvaluationResponseExtendedDto> getReportedEvaluationsByUserId(Integer userId) {
        return repository.findReportedEvaluationsByUserId(userId)
                .stream().map(mapper::mapExtended).toList();
    }

    public List<EvaluationResponseExtendedDto> getReportedEvaluationsByGroupId(Integer groupId) {
        return repository.findReportedEvaluationsByGroupId(groupId)
                .stream().map(mapper::mapExtended).toList();
    }

    public List<EvaluationResponseExtendedDto> getReportedEvaluationsByUserIdAndGroupId(Integer userId, Integer groupId) {
        return repository.findReportedEvaluationsByUserIdAndGroupId(userId, groupId)
                .stream().map(mapper::mapExtended).toList();
    }

    public Boolean checkForEvaluationToSolution(int solution_id) {
        return repository.checkForEvaluationToSolution(solution_id) != 0;
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
    public void delete(int id) {
        var evaluation = findById(id);
        repository.delete(evaluation);
    }

    @Transactional
    public Boolean changeResultById(int id, Double result) {
        if (repository.findById(id).isPresent()) {
            Evaluation evaluation = repository.findById(id).get();
            evaluation.setResult(result);
            repository.save(evaluation);
            return true;
        }
        else{
            return false;
        }
    }

    @Transactional
    public Boolean changeUserById(int id, int userId) {
        if (repository.findById(id).isPresent() && userRepository.findById(userId).isPresent()) {
            Evaluation evaluation = repository.findById(id).get();
            User user = userRepository.findById(userId).get();
            evaluation.setUser(user);
            repository.save(evaluation);
            return true;
        }
        else{
            return false;
        }
    }

    @Transactional
    public Boolean changeSolutionById(int id, int solutionId) {
        if (repository.findById(id).isPresent() && solutionRepository.findById(solutionId).isPresent()) {
            Evaluation evaluation = repository.findById(id).get();
            Solution solution = solutionRepository.findById(solutionId).get();
            evaluation.setSolution(solution);
            repository.save(evaluation);
            return true;
        }
        else{
            return false;
        }
    }


    @Transactional
    public Boolean changeGradeById(int id, Double grade) {
        if (repository.findById(id).isPresent()) {
            Evaluation evaluation = repository.findById(id).get();
            evaluation.setGrade(grade);
            repository.save(evaluation);
            return true;
        }
        else{
            return false;
        }
    }

    @Transactional
    public EvaluationResponseExtendedDto update(Integer id, EvaluationDto evaluationDto) {
        if (repository.existsBySolutionId(evaluationDto.getSolutionId()) && !repository.existsBySolutionIdAndId(evaluationDto.getSolutionId(), id)) {
            throw new InvalidArgumentException("Evaluation with solutionId = " + evaluationDto.getSolutionId() + " already exists");
        }
        validateDto(evaluationDto);
        var evaluation = findById(id);
        mapper.map(evaluation, evaluationDto);
        setRelationFields(evaluation, evaluationDto);
        return mapper.mapExtended(repository.save(evaluation));
    }

    private void validateDto(EvaluationDto evaluationDto) {
        if (solutionService.getGroupBySolutionId(evaluationDto.getSolutionId()).getId() != evaluationDto.getGroupId()) {
            throw new InvalidArgumentException("Invalid groupId");
        }
        if (userService.getTeachersByGroup(evaluationDto.getGroupId()).stream().filter(user -> user.getId() == evaluationDto.getUserId()).count() == 0) {
            throw new InvalidArgumentException("Given user is not teacher in group");
        }
    }

    private void setRelationFields(Evaluation evaluation, EvaluationDto evaluationDto) {
        var user = userService.findById(evaluationDto.getUserId());
        var solution = solutionService.findById(evaluationDto.getSolutionId());
        var group = groupService.findById(evaluationDto.getGroupId());
        evaluation.setUser(user);
        evaluation.setSolution(solution);
        evaluation.setGroup(group);
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
