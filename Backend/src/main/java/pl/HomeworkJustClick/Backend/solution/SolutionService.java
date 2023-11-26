package pl.HomeworkJustClick.Backend.solution;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.assignment.Assignment;
import pl.HomeworkJustClick.Backend.assignment.AssignmentRepository;
import pl.HomeworkJustClick.Backend.assignment.AssignmentResponseDto;
import pl.HomeworkJustClick.Backend.group.Group;
import pl.HomeworkJustClick.Backend.group.GroupRepository;
import pl.HomeworkJustClick.Backend.group.GroupResponseDto;
import pl.HomeworkJustClick.Backend.infrastructure.enums.CalendarStatus;
import pl.HomeworkJustClick.Backend.user.User;
import pl.HomeworkJustClick.Backend.user.UserRepository;
import pl.HomeworkJustClick.Backend.user.UserResponseDto;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class SolutionService {

    private final EntityManager entityManager;

    private final SolutionRepository solutionRepository;

    private final UserRepository userRepository;

    private final AssignmentRepository assignmentRepository;
    private final GroupRepository groupRepository;

    public List<SolutionResponseDto> getAll() {
        List<Solution> solutionList = solutionRepository.findAll();
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutionList.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    public SolutionResponseDto getById(int id) {
        Optional<Solution> solutionOptional = solutionRepository.findById(id);
        return solutionOptional.map(this::buildSolutionResponse).orElse(null);
    }

    public List<SolutionResponseExtendedDto> getAllExtended() {
        List<Solution> solutionList = solutionRepository.findAll();
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutionList.forEach(solution -> {
            responseList.add(buildSolutionResponseExtended(solution));
        });
        return responseList;
    }

    public SolutionResponseExtendedDto getByIdExtended(int id) {
        Optional<Solution> solutionOptional = solutionRepository.findById(id);
        return solutionOptional.map(this::buildSolutionResponseExtended).orElse(null);
    }

    @Transactional
    public SolutionResponseDto add(Solution solution) {
        entityManager.persist(solution);
        return SolutionResponseDto.builder()
                .id(solution.getId())
                .userId(solution.getUser().getId())
                .assignmentId(solution.getAssignment().getId())
                .creationDateTime(solution.getCreationDatetime())
                .lastModifiedDatetime(solution.getLastModifiedDatetime())
                .comment(solution.getComment())
                .build();
    }

    @Transactional
    public SolutionResponseDto addWithUserAndAssignment(Solution solution, int user_id, int assignment_id) {
        Optional<User> user = userRepository.findById(user_id);
        Optional<Assignment> assignment = assignmentRepository.findById(assignment_id);
        if(user.isPresent() && assignment.isPresent()) {
            List<User> userList = userRepository.getStudentsByGroupId(assignment.get().getGroup().getId());
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
                return SolutionResponseDto.builder()
                        .id(solution.getId())
                        .userId(user_id)
                        .assignmentId(assignment_id)
                        .groupId(assignment.get().getGroup().getId())
                        .creationDateTime(solution.getCreationDatetime())
                        .lastModifiedDatetime(solution.getLastModifiedDatetime())
                        .comment(solution.getComment())
                        .build();
            } else {
                return SolutionResponseDto.builder().forbidden(true).build();
            }
        } else {
            return SolutionResponseDto.builder().build();
        }
    }

    @Transactional
    public Boolean delete(int id) {
        if (solutionRepository.existsById(id)) {
            solutionRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
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

    @Transactional
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

    public List<SolutionResponseDto> getSolutionsByGroupId(int id) {
        List<Solution> solutions = solutionRepository.getSolutionsByGroupId(id);
        List<SolutionResponseDto> solutionResponsDtos = new ArrayList<>();
        for(Solution solution : solutions) {
            solutionResponsDtos.add(SolutionResponseDto.builder()
                    .id(solution.getId())
                    .userId(solution.getUser().getId())
                    .assignmentId(solution.getAssignment().getId())
                    .groupId(solution.getGroup().getId())
                    .creationDateTime(solution.getCreationDatetime())
                    .lastModifiedDatetime(solution.getLastModifiedDatetime())
                    .comment(solution.getComment())
                    .build());
        }
        return solutionResponsDtos;
    }

    public List<SolutionResponseDto> getSolutionsByAssignmentId(int id) {
        List<Solution> solutions = solutionRepository.getSolutionsByAssignmentId(id);
        List<SolutionResponseDto> solutionResponsDtos = new ArrayList<>();
        for(Solution solution : solutions) {
            solutionResponsDtos.add(SolutionResponseDto.builder()
                    .id(solution.getId())
                    .userId(solution.getUser().getId())
                    .assignmentId(solution.getAssignment().getId())
                    .groupId(solution.getGroup().getId())
                    .creationDateTime(solution.getCreationDatetime())
                    .lastModifiedDatetime(solution.getLastModifiedDatetime())
                    .comment(solution.getComment())
                    .build());
        }
        return solutionResponsDtos;
    }

    public List<SolutionResponseDto> getLateSolutionsByGroup(int group_id) {
        List<Solution> solutions = solutionRepository.getSolutionsByGroupId(group_id);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if(solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    public List<SolutionResponseDto> getLateSolutionsByUserAndGroup(int user_id, int group_id) {
        List<Solution> solutions = solutionRepository.getSolutionsByUserAndGroup(user_id, group_id);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if(solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    public List<SolutionResponseDto> getLateSolutionsByAssignment(int assignment_id) {
        List<Solution> solutions = solutionRepository.getSolutionsByAssignmentId(assignment_id);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if(solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    public List<SolutionResponseDto> getLateSolutionsByStudent(int user_id) {
        List<Solution> solutions = solutionRepository.getSolutionsByUser(user_id);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if(solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    public List<SolutionResponseDto> getUncheckedSolutionsByGroup(int group_id) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByGroup(group_id);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    public List<SolutionResponseDto> getCheckedSolutionsByGroup(int group_id) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByGroup(group_id);

        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    public List<SolutionResponseDto> getUncheckedSolutionsByStudent(int student_id) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByStudent(student_id);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    public List<SolutionResponseDto> getCheckedSolutionsByStudent(int student_id) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByStudent(student_id);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    public List<SolutionResponseDto> getUncheckedSolutionsByStudentAndGroup(int student_id, int group_id) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByStudentAndGroup(student_id, group_id);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    public List<SolutionResponseDto> getCheckedSolutionsByStudentAndGroup(int student_id, int group_id) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByStudentAndGroup(student_id, group_id);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    public List<SolutionResponseDto> getUncheckedSolutionsByAssignment(int assignment_id) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByAssignment(assignment_id);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    public List<SolutionResponseDto> getCheckedSolutionsByAssignment(int assignment_id) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByAssignment(assignment_id);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    public List<SolutionResponseDto> getUncheckedSolutionsByTeacher(int teacher_id) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByTeacher(teacher_id);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    public List<SolutionResponseDto> getCheckedSolutionsByTeacher(int teacher_id) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByTeacher(teacher_id);
        List<SolutionResponseDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponse(solution));
        });
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getSolutionsByGroupIdExtended(int id) {
        List<Solution> solutions = solutionRepository.getSolutionsByGroupId(id);
        List<SolutionResponseExtendedDto> solutionResponses = new ArrayList<>();
        for(Solution solution : solutions) {
            solutionResponses.add(buildSolutionResponseExtended(solution));
        }
        return solutionResponses;
    }

    public List<SolutionResponseExtendedDto> getSolutionsByAssignmentIdExtended(int id) {
        List<Solution> solutions = solutionRepository.getSolutionsByAssignmentId(id);
        List<SolutionResponseExtendedDto> solutionResponses = new ArrayList<>();
        for(Solution solution : solutions) {
            solutionResponses.add(buildSolutionResponseExtended(solution));
        }
        return solutionResponses;
    }

    public List<SolutionResponseExtendedDto> getLateSolutionsByGroupExtended(int group_id) {
        List<Solution> solutions = solutionRepository.getSolutionsByGroupId(group_id);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if(solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> {
            responseList.add(buildSolutionResponseExtended(solution));
        });
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getLateSolutionsByUserAndGroupExtended(int user_id, int group_id) {
        List<Solution> solutions = solutionRepository.getSolutionsByUserAndGroup(user_id, group_id);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if(solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> {
            responseList.add(buildSolutionResponseExtended(solution));
        });
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getLateSolutionsByAssignmentExtended(int assignment_id) {
        List<Solution> solutions = solutionRepository.getSolutionsByAssignmentId(assignment_id);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if(solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> {
            responseList.add(buildSolutionResponseExtended(solution));
        });
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getLateSolutionsByStudentExtended(int user_id) {
        List<Solution> solutions = solutionRepository.getSolutionsByUser(user_id);
        List<Solution> lateSolutions = new ArrayList<>();
        solutions.forEach(solution -> {
            if(solution.getAssignment().getCompletionDatetime().isBefore(solution.getCreationDatetime())) {
                lateSolutions.add(solution);
            }
        });
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        lateSolutions.forEach(solution -> {
            responseList.add(buildSolutionResponseExtended(solution));
        });
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getUncheckedSolutionsByGroupExtended(int group_id) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByGroup(group_id);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponseExtended(solution));
        });
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getCheckedSolutionsByGroupExtended(int group_id) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByGroup(group_id);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponseExtended(solution));
        });
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getUncheckedSolutionsByStudentExtended(int student_id) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByStudent(student_id);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponseExtended(solution));
        });
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getCheckedSolutionsByStudentExtended(int student_id) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByStudent(student_id);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponseExtended(solution));
        });
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getUncheckedSolutionsByStudentAndGroupExtended(int student_id, int group_id) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByStudentAndGroup(student_id, group_id);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponseExtended(solution));
        });
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getCheckedSolutionsByStudentAndGroupExtended(int student_id, int group_id) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByStudentAndGroup(student_id, group_id);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponseExtended(solution));
        });
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getUncheckedSolutionsByAssignmentExtended(int assignment_id) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByAssignment(assignment_id);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponseExtended(solution));
        });
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getCheckedSolutionsByAssignmentExtended(int assignment_id) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByAssignment(assignment_id);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponseExtended(solution));
        });
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getUncheckedSolutionsByTeacherExtended(int teacher_id) {
        List<Solution> solutions = solutionRepository.getUncheckedSolutionsByTeacher(teacher_id);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponseExtended(solution));
        });
        return responseList;
    }

    public List<SolutionResponseExtendedDto> getCheckedSolutionsByTeacherExtended(int teacher_id) {
        List<Solution> solutions = solutionRepository.getCheckedSolutionsByTeacher(teacher_id);
        List<SolutionResponseExtendedDto> responseList = new ArrayList<>();
        solutions.forEach(solution -> {
            responseList.add(buildSolutionResponseExtended(solution));
        });
        return responseList;
    }

    private SolutionResponseDto buildSolutionResponse(Solution solution) {
        return SolutionResponseDto.builder()
                .id(solution.getId())
                .userId(solution.getUser().getId())
                .groupId(solution.getGroup().getId())
                .assignmentId(solution.getAssignment().getId())
                .creationDateTime(solution.getCreationDatetime())
                .lastModifiedDatetime(solution.getLastModifiedDatetime())
                .comment(solution.getComment())
                .build();
    }

    private SolutionResponseExtendedDto buildSolutionResponseExtended(Solution solution) {
        User user = solution.getUser();
        Group group = solution.getGroup();
        Assignment assignment = solution.getAssignment();
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
        GroupResponseDto groupResponseDto = GroupResponseDto.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .color(group.getColor())
                .isArchived(group.isArchived())
                .build();
        AssignmentResponseDto assignmentResponseDto = AssignmentResponseDto.builder()
                .id(assignment.getId())
                .userId((assignment.getUser() == null) ? null : assignment.getUser().getId())
                .groupId((assignment.getGroup() == null) ? null : assignment.getGroup().getId())
                .taskDescription(assignment.getTaskDescription())
                .creationDatetime(assignment.getCreationDatetime())
                .lastModifiedDatetime(assignment.getLastModifiedDatetime())
                .completionDatetime(assignment.getCompletionDatetime())
                .title(assignment.getTitle())
                .visible(assignment.getVisible())
                .max_points(assignment.getMax_points())
                .auto_penalty(assignment.getAuto_penalty())
                .build();

        return SolutionResponseExtendedDto.builder()
                .id(solution.getId())
                .user(userResponseDto)
                .group(groupResponseDto)
                .assignment(assignmentResponseDto)
                .creationDateTime(solution.getCreationDatetime())
                .lastModifiedDatetime(solution.getLastModifiedDatetime())
                .comment(solution.getComment())
                .build();
    }

    private SolutionResponseCalendarDto buildSolutionResponseCalendar(Solution solution, CalendarStatus calendarStatus) {
        return SolutionResponseCalendarDto.builder()
                .id(solution.getId())
                .userId(solution.getUser().getId())
                .groupId(solution.getGroup().getId())
                .assignmentId(solution.getAssignment().getId())
                .creationDateTime(solution.getCreationDatetime())
                .lastModifiedDatetime(solution.getLastModifiedDatetime())
                .comment(solution.getComment())
                .status(calendarStatus)
                .build();
    }

    public boolean checkForEvaluationToSolution(int solution_id) {
        return solutionRepository.checkForEvaluationToSolution(solution_id) != 0;
    }

    public boolean checkForFileToSolution(int solution_id) {
        Optional<Solution> solutionOptional = solutionRepository.findById(solution_id);
        if (solutionOptional.isPresent()) {
            Solution solution = solutionOptional.get();
            return !solution.getFiles().isEmpty();
        } else return false;
    }

    public SolutionResponseDto getCheckedSolutionByUserAssignmentGroup(int user_id, int group_id, int assignment_id) {
        if(assignmentRepository.findById(assignment_id).isPresent() && groupRepository.findById(group_id).isPresent() && userRepository.findById(user_id).isPresent()){
            Optional<Solution> solution = solutionRepository.getCheckedSolutionByUserAssignmentGroup(assignment_id, user_id, group_id);
            if (solution.isPresent()){
                return buildSolutionResponse(solution.get());
            }
            else{
                return SolutionResponseDto.builder().build();
            }
        }
        else{
            return SolutionResponseDto.builder().forbidden(true).build();
        }

    }

    public SolutionResponseDto getUncheckedSolutionByUserAssignmentGroup(int user_id, int group_id, int assignment_id) {
        if(assignmentRepository.findById(assignment_id).isPresent() && groupRepository.findById(group_id).isPresent() && userRepository.findById(user_id).isPresent()){
            Optional<Solution> solution = solutionRepository.getUncheckedSolutionByUserAssignmentGroup(assignment_id, user_id, group_id);
            if (solution.isPresent()){
                return buildSolutionResponse(solution.get());
            } else {
                return SolutionResponseDto.builder().build();
            }
        } else {
            return SolutionResponseDto.builder().forbidden(true).build();
        }

    }

    public List<SolutionResponseCalendarDto> getSolutionsByTeacherCalender(int teacher_id) {
        List<Solution> checkedSolutionsList = solutionRepository.getCheckedSolutionsByTeacher(teacher_id);
        List<Solution> uncheckedSolutionsList = solutionRepository.getUncheckedSolutionsByTeacher(teacher_id);
        List<Solution> expiredUncheckedSolutionsList = new ArrayList<>();
        List<Solution> nonExpiredUncheckedSolutionsList = new ArrayList<>();
        for (Solution solution : uncheckedSolutionsList) {
            if (solution.getCreationDatetime().plusDays(14).isBefore(OffsetDateTime.now())) {
                expiredUncheckedSolutionsList.add(solution);
            } else {
                nonExpiredUncheckedSolutionsList.add(solution);
            }
        }
        List<SolutionResponseCalendarDto> response = new ArrayList<>();
        for (Solution solution : checkedSolutionsList) {
            response.add(buildSolutionResponseCalendar(solution, CalendarStatus.DONE));
        }
        for (Solution solution : expiredUncheckedSolutionsList) {
            response.add(buildSolutionResponseCalendar(solution, CalendarStatus.LATE));
        }
        for (Solution solution : nonExpiredUncheckedSolutionsList) {
            response.add(buildSolutionResponseCalendar(solution, CalendarStatus.TODO));
        }
        response.sort(Comparator.comparing(SolutionResponseCalendarDto::getCreationDateTime));
        return response;
    }
}
