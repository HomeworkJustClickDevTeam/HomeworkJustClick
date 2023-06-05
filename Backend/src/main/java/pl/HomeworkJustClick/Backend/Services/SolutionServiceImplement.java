package pl.HomeworkJustClick.Backend.Services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Assignment;
import pl.HomeworkJustClick.Backend.Entities.Solution;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Repositories.AssignmentRepository;
import pl.HomeworkJustClick.Backend.Repositories.SolutionRepository;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;
import pl.HomeworkJustClick.Backend.Responses.SolutionResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class SolutionServiceImplement implements SolutionService{

    private final EntityManager entityManager;

    private final SolutionRepository solutionRepository;

    private final UserRepository userRepository;

    private final AssignmentRepository assignmentRepository;
    @Override
    public List<SolutionResponse> getAll() {
        List<Solution> solutionList = solutionRepository.findAll();
        List<SolutionResponse> responseList = new ArrayList<>();
        solutionList.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    @Override
    public SolutionResponse getById(int id) {
        Optional<Solution> solutionOptional = solutionRepository.findById(id);
        return solutionOptional.map(this::buildSolutionResponse).orElse(null);
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
                .comment(solution.getComment())
                .build();
    }

    @Override
    @Transactional
    public SolutionResponse addWithUserAndAssignment(Solution solution, int user_id, int assignment_id) {
        Optional<User> user = userRepository.findById(user_id);
        Optional<Assignment> assignment = assignmentRepository.findById(assignment_id);
        if(user.isPresent() && assignment.isPresent()) {
            List<User> userList = userRepository.getGroupStudentsByGroup(assignment.get().getGroup().getId());
            System.out.println(assignment.get().getGroup().getId());
            AtomicBoolean ok = new AtomicBoolean(false);
            userList.forEach(user1 -> {
                if (user1.getId() == user_id) {
                    ok.set(true);
                }
            });
            if (solutionRepository.getSolutionByUserAndAssignment(user_id, assignment_id).isPresent()) {
                ok.set(false);
            }
            if(ok.get()) {
                solution.setAssignment(assignment.get());
                solution.setUser(user.get());
                solution.setGroup(assignment.get().getGroup());
                entityManager.persist(solution);
                return SolutionResponse.builder()
                        .id(solution.getId())
                        .userId(user_id)
                        .assignmentId(assignment_id)
                        .groupId(assignment.get().getGroup().getId())
                        .creationDateTime(solution.getCreationDatetime())
                        .lastModifiedDatetime(solution.getLastModifiedDatetime())
                        .comment(solution.getComment())
                        .build();
            } else {
                return SolutionResponse.builder().forbidden(true).build();
            }
        } else {
            return SolutionResponse.builder().build();
        }
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

    @Override
    public List<SolutionResponse> getSolutionsByGroupId(int id) {
        List<Solution> solutions = solutionRepository.getSolutionsByGroupId(id);
        List<SolutionResponse> solutionResponses = new ArrayList<>();
        for(Solution solution : solutions) {
            solutionResponses.add(SolutionResponse.builder()
                    .id(solution.getId())
                    .userId(solution.getUser().getId())
                    .assignmentId(solution.getAssignment().getId())
                    .groupId(solution.getGroup().getId())
                    .creationDateTime(solution.getCreationDatetime())
                    .lastModifiedDatetime(solution.getLastModifiedDatetime())
                    .comment(solution.getComment())
                    .build());
        }
        return solutionResponses;
    }

    @Override
    public List<SolutionResponse> getSolutionsByAssignmentId(int id) {
        List<Solution> solutions = solutionRepository.getSolutionsByAssignmentId(id);
        List<SolutionResponse> solutionResponses = new ArrayList<>();
        for(Solution solution : solutions) {
            solutionResponses.add(SolutionResponse.builder()
                    .id(solution.getId())
                    .userId(solution.getUser().getId())
                    .assignmentId(solution.getAssignment().getId())
                    .groupId(solution.getGroup().getId())
                    .creationDateTime(solution.getCreationDatetime())
                    .lastModifiedDatetime(solution.getLastModifiedDatetime())
                    .comment(solution.getComment())
                    .build());
        }
        return solutionResponses;
    }

    @Override
    public List<SolutionResponse> getLateSolutionsByGroup(int group_id) {
        List<Solution> solutions = solutionRepository.getSolutionsByGroupId(group_id);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if(solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponse> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    @Override
    public List<SolutionResponse> getLateSolutionsByUserAndGroup(int user_id, int group_id) {
        List<Solution> solutions = solutionRepository.getSolutionsByUserAndGroup(user_id, group_id);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if(solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponse> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    @Override
    public List<SolutionResponse> getLateSolutionsByAssignment(int assignment_id) {
        List<Solution> solutions = solutionRepository.getSolutionsByAssignmentId(assignment_id);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if(solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponse> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    @Override
    public List<SolutionResponse> getLateSolutionsByStudent(int user_id) {
        List<Solution> solutions = solutionRepository.getSolutionsByUser(user_id);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if(solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponse> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    @Override
    public List<SolutionResponse> getUncheckedSolutionsByGroup(int group_id) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByGroup(group_id);
        List<SolutionResponse> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    @Override
    public List<SolutionResponse> getCheckedSolutionsByGroup(int group_id) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByGroup(group_id);

        List<SolutionResponse> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    @Override
    public List<SolutionResponse> getUncheckedSolutionsByStudent(int student_id) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByStudent(student_id);
        List<SolutionResponse> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    @Override
    public List<SolutionResponse> getCheckedSolutionsByStudent(int student_id) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByStudent(student_id);
        List<SolutionResponse> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    @Override
    public List<SolutionResponse> getUncheckedSolutionsByStudentAndGroup(int student_id, int group_id) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByStudentAndGroup(student_id, group_id);
        List<SolutionResponse> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    @Override
    public List<SolutionResponse> getCheckedSolutionsByStudentAndGroup(int student_id, int group_id) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByStudentAndGroup(student_id, group_id);
        List<SolutionResponse> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    @Override
    public List<SolutionResponse> getUncheckedSolutionsByAssignment(int assignment_id) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByAssignment(assignment_id);
        List<SolutionResponse> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    @Override
    public List<SolutionResponse> getCheckedSolutionsByAssignment(int assignment_id) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByAssignment(assignment_id);
        List<SolutionResponse> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    @Override
    public List<SolutionResponse> getUncheckedSolutionsByTeacher(int teacher_id) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByTeacher(teacher_id);
        List<SolutionResponse> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    @Override
    public List<SolutionResponse> getCheckedSolutionsByTeacher(int teacher_id) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByTeacher(teacher_id);
        List<SolutionResponse> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    private SolutionResponse buildSolutionResponse(Solution solution) {
        return SolutionResponse.builder()
                .id(solution.getId())
                .userId(solution.getUser().getId())
                .groupId(solution.getGroup().getId())
                .assignmentId(solution.getAssignment().getId())
                .creationDateTime(solution.getCreationDatetime())
                .lastModifiedDatetime(solution.getLastModifiedDatetime())
                .comment(solution.getComment())
                .build();
    }
}
