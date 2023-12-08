package pl.homeworkjustclick.evaluation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.homeworkjustclick.assignment.Assignment;
import pl.homeworkjustclick.assignment.AssignmentRepository;
import pl.homeworkjustclick.group.Group;
import pl.homeworkjustclick.group.GroupRepository;
import pl.homeworkjustclick.groupstudent.GroupStudent;
import pl.homeworkjustclick.groupstudent.GroupStudentRepository;
import pl.homeworkjustclick.groupteacher.GroupTeacher;
import pl.homeworkjustclick.groupteacher.GroupTeacherRepository;
import pl.homeworkjustclick.infrastructure.enums.Role;
import pl.homeworkjustclick.solution.Solution;
import pl.homeworkjustclick.solution.SolutionRepository;
import pl.homeworkjustclick.user.User;
import pl.homeworkjustclick.user.UserRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class EvaluationRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupTeacherRepository groupTeacherRepository;

    @Autowired
    private GroupStudentRepository groupStudentRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void itShouldGetEvaluationsByAssignmentId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
        evaluationRepository.save(evaluation);
        evaluationRepository.save(evaluation2);
        evaluationRepository.save(evaluation3);
        evaluationRepository.save(evaluation4);
        List<Evaluation> expectedList = new ArrayList<>();
//        expectedList.add(evaluation);
        expectedList.add(evaluation2);
//        expectedList.add(evaluation3);
        expectedList.add(evaluation4);


        //when
        List<Evaluation> actual = evaluationRepository.getEvaluationsByAssignment(assignment.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetEvaluationsByAssignmentId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
//        evaluationRepository.save(evaluation);
//        evaluationRepository.save(evaluation2);
//        evaluationRepository.save(evaluation3);
//        evaluationRepository.save(evaluation4);
        List<Evaluation> expectedList = new ArrayList<>();
//        expectedList.add(evaluation);
//        expectedList.add(evaluation2);
//        expectedList.add(evaluation3);
//        expectedList.add(evaluation4);


        //when
        List<Evaluation> actual = evaluationRepository.getEvaluationsByAssignment(assignment.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetEvaluationsByAssignmentId_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
        evaluationRepository.save(evaluation);
        evaluationRepository.save(evaluation2);
        evaluationRepository.save(evaluation3);
        evaluationRepository.save(evaluation4);
        List<Evaluation> expectedList = new ArrayList<>();
//        expectedList.add(evaluation);
//        expectedList.add(evaluation2);
//        expectedList.add(evaluation3);
//        expectedList.add(evaluation4);


        //when
        List<Evaluation> actual = evaluationRepository.getEvaluationsByAssignment(assignment.getId()+10);

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldCountEvaluationsByAssignmentId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
        evaluationRepository.save(evaluation);
        evaluationRepository.save(evaluation2);
        evaluationRepository.save(evaluation3);
        evaluationRepository.save(evaluation4);
        int expected = 2;


        //when
        int actual = evaluationRepository.countEvaluationsByAssignment(assignment.getId());

        //then
        assertEquals(expected, actual);

    }

    @Test
    void itShouldCountEvaluationsByAssignmentId_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
//        evaluationRepository.save(evaluation);
//        evaluationRepository.save(evaluation2);
//        evaluationRepository.save(evaluation3);
//        evaluationRepository.save(evaluation4);
        int expected = 0;


        //when
        int actual = evaluationRepository.countEvaluationsByAssignment(assignment.getId());

        //then
        assertEquals(expected, actual);

    }

    @Test
    void itShouldGetEvaluationsByStudentId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
        evaluationRepository.save(evaluation);
        evaluationRepository.save(evaluation2);
        evaluationRepository.save(evaluation3);
        evaluationRepository.save(evaluation4);
        List<Evaluation> expectedList = new ArrayList<>();
        expectedList.add(evaluation);
        expectedList.add(evaluation2);
//        expectedList.add(evaluation3);
//        expectedList.add(evaluation4);


        //when
        List<Evaluation> actual = evaluationRepository.getAllEvaluationsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetEvaluationsByStudentId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
//        evaluationRepository.save(evaluation);
//        evaluationRepository.save(evaluation2);
        evaluationRepository.save(evaluation3);
        evaluationRepository.save(evaluation4);
        List<Evaluation> expectedList = new ArrayList<>();
//        expectedList.add(evaluation);
//        expectedList.add(evaluation2);
//        expectedList.add(evaluation3);
//        expectedList.add(evaluation4);


        //when
        List<Evaluation> actual = evaluationRepository.getAllEvaluationsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetEvaluationsByStudentId_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
//        evaluationRepository.save(evaluation);
//        evaluationRepository.save(evaluation2);
//        evaluationRepository.save(evaluation3);
//        evaluationRepository.save(evaluation4);
        List<Evaluation> expectedList = new ArrayList<>();
//        expectedList.add(evaluation);
//        expectedList.add(evaluation2);
//        expectedList.add(evaluation3);
//        expectedList.add(evaluation4);


        //when
        List<Evaluation> actual = evaluationRepository.getAllEvaluationsByStudent(user2.getId()+10);

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetEvaluationsByStudentIdAndGroupId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
        evaluationRepository.save(evaluation);
        evaluationRepository.save(evaluation2);
        evaluationRepository.save(evaluation3);
        evaluationRepository.save(evaluation4);
        List<Evaluation> expectedList = new ArrayList<>();
        expectedList.add(evaluation);
        expectedList.add(evaluation2);
//        expectedList.add(evaluation3);
//        expectedList.add(evaluation4);


        //when
        List<Evaluation> actual = evaluationRepository.getAllEvaluationsByStudentInGroup(user2.getId(), group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetEvaluationsByStudentIdAndGroupId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
//        evaluationRepository.save(evaluation);
//        evaluationRepository.save(evaluation2);
        evaluationRepository.save(evaluation3);
        evaluationRepository.save(evaluation4);
        List<Evaluation> expectedList = new ArrayList<>();
//        expectedList.add(evaluation);
//        expectedList.add(evaluation2);
//        expectedList.add(evaluation3);
//        expectedList.add(evaluation4);


        //when
        List<Evaluation> actual = evaluationRepository.getAllEvaluationsByStudentInGroup(user2.getId(), group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetEvaluationsByStudentIdAndGroupId_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
//        evaluationRepository.save(evaluation);
//        evaluationRepository.save(evaluation2);
//        evaluationRepository.save(evaluation3);
//        evaluationRepository.save(evaluation4);
        List<Evaluation> expectedList = new ArrayList<>();
//        expectedList.add(evaluation);
//        expectedList.add(evaluation2);
//        expectedList.add(evaluation3);
//        expectedList.add(evaluation4);


        //when
        List<Evaluation> actual = evaluationRepository.getAllEvaluationsByStudentInGroup(user2.getId()+10, group.getId()+10);

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetEvaluationBySolutionId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
        evaluationRepository.save(evaluation);
        evaluationRepository.save(evaluation2);
        evaluationRepository.save(evaluation3);
        evaluationRepository.save(evaluation4);
        List<Evaluation> expectedList = new ArrayList<>();



        //when
        Optional<Evaluation> actual = evaluationRepository.getEvaluationBySolution(solution.getId());

        //then
        assertTrue(actual.isPresent());
        assertEquals(evaluation, actual.get());

    }

    @Test
    void itShouldNotGetEvaluationBySolutionId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
        evaluationRepository.save(evaluation);
        evaluationRepository.save(evaluation2);
        evaluationRepository.save(evaluation3);
        evaluationRepository.save(evaluation4);
        List<Evaluation> expectedList = new ArrayList<>();



        //when
        Optional<Evaluation> actual = evaluationRepository.getEvaluationBySolution(solution.getId()+1337);

        //then
        assertTrue(actual.isEmpty());

    }

    @Test
    void itShouldReturn1() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
        evaluationRepository.save(evaluation);
        evaluationRepository.save(evaluation2);
        evaluationRepository.save(evaluation3);
        evaluationRepository.save(evaluation4);

        //when
        int actual = evaluationRepository.checkForEvaluationToSolution(solution.getId());

        //then
        assertEquals(1,actual);

    }

    @Test
    void itShouldReturn0() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0, false);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
//        evaluationRepository.save(evaluation);
        evaluationRepository.save(evaluation2);
        evaluationRepository.save(evaluation3);
        evaluationRepository.save(evaluation4);

        //when
        int actual = evaluationRepository.checkForEvaluationToSolution(solution.getId());

        //then
        assertEquals(0,actual);

    }
}
