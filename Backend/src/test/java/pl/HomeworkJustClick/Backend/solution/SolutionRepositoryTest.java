package pl.HomeworkJustClick.Backend.solution;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.HomeworkJustClick.Backend.assignment.Assignment;
import pl.HomeworkJustClick.Backend.assignment.AssignmentRepository;
import pl.HomeworkJustClick.Backend.evaluation.Evaluation;
import pl.HomeworkJustClick.Backend.evaluation.EvaluationRepository;
import pl.HomeworkJustClick.Backend.group.Group;
import pl.HomeworkJustClick.Backend.group.GroupRepository;
import pl.HomeworkJustClick.Backend.groupstudent.GroupStudent;
import pl.HomeworkJustClick.Backend.groupstudent.GroupStudentRepository;
import pl.HomeworkJustClick.Backend.groupteacher.GroupTeacher;
import pl.HomeworkJustClick.Backend.groupteacher.GroupTeacherRepository;
import pl.HomeworkJustClick.Backend.infrastructure.enums.Role;
import pl.HomeworkJustClick.Backend.user.User;
import pl.HomeworkJustClick.Backend.user.UserRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class SolutionRepositoryTest {

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
    void itShouldGetSolutionsByGroupId() {
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
        List<Solution> expectedList = new ArrayList<>();
        expectedList.add(solution);
        expectedList.add(solution2);
        expectedList.add(solution3);
        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getSolutionsByGroupId(group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetSolutionsByGroupId() {
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getSolutionsByGroupId(group.getId()+1);

        //then
        assertEquals(expectedList, actual);
    }

    @Test
    void itShouldGetSolutionsByAssignmentId() {
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getSolutionsByAssignmentId(assignment.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetSolutionsByAssignmentId_2() {
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
        List<Solution> expectedList = new ArrayList<>();
        expectedList.add(solution);
//        expectedList.add(solution2);
        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getSolutionsByAssignmentId(assignment2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetSolutionsByAssignmentId() {
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getSolutionsByAssignmentId(assignment2.getId()+1);

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldCountSolutionsByAssignmentId() {
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


        //when
        int actual = solutionRepository.countSolutionsByAssignmentId(assignment2.getId());

        //then
        assertEquals(2, actual);

    }

    @Test
    void itShouldCountSolutionsByAssignmentId_2() {
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


        //when
        int actual = solutionRepository.countSolutionsByAssignmentId(assignment2.getId()+1);

        //then
        assertEquals(0, actual);

    }

    @Test
    void itShouldGetSolutionsByGroupIdAndStudent_id() {
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
        List<Solution> expectedList = new ArrayList<>();
        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getSolutionsByUserAndGroup(user2.getId(), group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetSolutionsByGroupIdAndStudent_id_2() {
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
        expectedList.add(solution3);
        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getSolutionsByUserAndGroup(user3.getId(), group.getId());

        //then
        assertEquals(expectedList, actual);
    }

    @Test
    void itShouldNotGetSolutionsByGroupIdAndStudent_id() {
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
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
//        solutionRepository.save(solution);
//        solutionRepository.save(solution2);
//        solutionRepository.save(solution3);
//        solutionRepository.save(solution4);
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
////        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getSolutionsByUserAndGroup(user2.getId(), group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetSolutionsByStudent_id() {
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
        List<Solution> expectedList = new ArrayList<>();
        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getSolutionsByUser(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetSolutionsByStudent_id() {
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getSolutionsByUser(user2.getId()+10);

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetSolutionsByStudentIdAndAssignmentId() {
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
//        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        Optional<Solution> actual = solutionRepository.getSolutionByUserAndAssignment(user2.getId(), assignment.getId());

        //then
        assertTrue(actual.isPresent());
        assertEquals(solution2, actual.get());

    }

    @Test
    void itShouldNotGetSolutionsByStudentIdAndAssignmentId() {
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
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
//        solutionRepository.save(solution2);
        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        Optional<Solution> actual = solutionRepository.getSolutionByUserAndAssignment(user2.getId(), assignment.getId());

        //then
        assertTrue(actual.isEmpty());

    }

    @Test
    void itShouldGetUncheckedSolutionsByGroupId() {
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
        List<Solution> expectedList = new ArrayList<>();
        expectedList.add(solution);
        expectedList.add(solution2);
        expectedList.add(solution3);
        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByGroup(group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetUncheckedSolutionsByGroupId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
        expectedList.add(solution3);
        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByGroup(group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetUncheckedSolutionsByGroupId() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByGroup(group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetUncheckedSolutionsByGroupId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByGroup(group.getId()+10);

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetUncheckedSolutionsByStudentId() {
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
        List<Solution> expectedList = new ArrayList<>();
        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetUncheckedSolutionsByStudentId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
//        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
//        evaluationRepository.save(evaluation2);
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetUncheckedSolutionsByStudentId() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetUncheckedSolutionsByStudentId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByStudent(user2.getId()+10);

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetUncheckedSolutionsByStudentIdAndGroupId() {
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
        List<Solution> expectedList = new ArrayList<>();
        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByStudentAndGroup(user2.getId(), group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetUncheckedSolutionsByStudentIdAndGroupId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
//        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
//        evaluationRepository.save(evaluation2);
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByStudentAndGroup(user2.getId(), group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetUncheckedSolutionsByStudentIdAndGroupId() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByStudentAndGroup(user2.getId(), group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetUncheckedSolutionsByStudentIdAndGroupId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByStudentAndGroup(user2.getId()+10, group.getId()+10);

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetUncheckedSolutionsByAssignmentId() {
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByAssignment(assignment.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetUncheckedSolutionsByAssignmentId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
//        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
//        evaluationRepository.save(evaluation2);
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByAssignment(assignment.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetUncheckedSolutionsByAssignmentId() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByAssignment(assignment.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetUncheckedSolutionsByAssignmentId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByAssignment(assignment.getId()+10);

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetUncheckedSolutionsByTeacherId() {
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
        List<Solution> expectedList = new ArrayList<>();
        expectedList.add(solution);
        expectedList.add(solution2);
        expectedList.add(solution3);
        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByTeacher(user.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetUncheckedSolutionsByTeacherId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
        expectedList.add(solution3);
        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByTeacher(user.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetUncheckedSolutionsByTeacherId() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByTeacher(user.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetUncheckedSolutionsByTeacherId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getUncheckedSolutionsByTeacher(user.getId()+10);

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetCheckedSolutionsByGroupId() {
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByGroup(group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetCheckedSolutionsByGroupId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByGroup(group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetCheckedSolutionsByGroupId() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByGroup(group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetCheckedSolutionsByGroupId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByGroup(group.getId()+10);

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetCheckedSolutionsByStudentId() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetCheckedSolutionsByStudentId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetCheckedSolutionsByStudentId() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetCheckedSolutionsByStudentId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByStudent(user2.getId()+10);

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetCheckedSolutionsByStudentIdAndGroupId() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByStudentAndGroup(user2.getId(), group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetCheckedSolutionsByStudentIdAndGroupId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
//        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
//        evaluationRepository.save(evaluation2);
        List<Solution> expectedList = new ArrayList<>();
        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByStudentAndGroup(user2.getId(), group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetCheckedSolutionsByStudentIdAndGroupId() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByStudentAndGroup(user2.getId(), group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetCheckedSolutionsByStudentIdAndGroupId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByStudentAndGroup(user2.getId()+10, group.getId()+10);

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetCheckedSolutionsByAssignmentId() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByAssignment(assignment.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetCheckedSolutionsByAssignmentId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByAssignment(assignment.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetCheckedSolutionsByAssignmentId() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByAssignment(assignment.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetCheckedSolutionsByAssignmentId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByAssignment(assignment.getId()+10);

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetCheckedSolutionsByTeacherId() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
        expectedList.add(solution);
        expectedList.add(solution2);
        expectedList.add(solution3);
        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByTeacher(user.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetCheckedSolutionsByTeacherId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
        expectedList.add(solution);
        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByTeacher(user.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetCheckedSolutionsByTeacherId() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByTeacher(user.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetCheckedSolutionsByTeacherId_2() {
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
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation3 = new Evaluation(10.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation4 = new Evaluation(10.0, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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
        List<Solution> expectedList = new ArrayList<>();
//        expectedList.add(solution);
//        expectedList.add(solution2);
//        expectedList.add(solution3);
//        expectedList.add(solution4);


        //when
        List<Solution> actual = solutionRepository.getCheckedSolutionsByTeacher(user.getId()+10);

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldReturn1() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis", 5, false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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

        //when
        int actual = solutionRepository.checkForEvaluationToSolution(solution.getId());

        //then
        assertEquals(1, actual);
    }

    @Test
    void itShouldReturn0() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        User user3 = new User("jan_kowalski3@gmail.com", "123", true, Role.USER, 123456, "Jan3", "Kowalski3", 7, "x");
        Group group = new Group("Grupa", "opis", 5, false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), "");
        Solution solution3 = new Solution(user3, assignment2, group, OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user3, assignment, group, OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 16, 15, 0, 0, 0, ZoneOffset.UTC), "");
        Evaluation evaluation = new Evaluation(10.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
        Evaluation evaluation2 = new Evaluation(10.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 10.0);
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

        //when
        int actual = solutionRepository.checkForEvaluationToSolution(solution3.getId());

        //then
        assertEquals(0, actual);
    }
}