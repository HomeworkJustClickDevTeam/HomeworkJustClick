package pl.HomeworkJustClick.Backend.assignment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.HomeworkJustClick.Backend.group.Group;
import pl.HomeworkJustClick.Backend.group.GroupRepository;
import pl.HomeworkJustClick.Backend.groupstudent.GroupStudent;
import pl.HomeworkJustClick.Backend.groupstudent.GroupStudentRepository;
import pl.HomeworkJustClick.Backend.groupteacher.GroupTeacher;
import pl.HomeworkJustClick.Backend.groupteacher.GroupTeacherRepository;
import pl.HomeworkJustClick.Backend.infrastructure.enums.Role;
import pl.HomeworkJustClick.Backend.solution.Solution;
import pl.HomeworkJustClick.Backend.solution.SolutionRepository;
import pl.HomeworkJustClick.Backend.user.User;
import pl.HomeworkJustClick.Backend.user.UserRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
public class AssignmentRepositoryTest {

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

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void itShouldGetAssignmentsByGroupId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
        expectedList.add(assignment);
        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getAssignmentsByGroupId(group.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetAssignmentsByGroupId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
        expectedList.add(assignment);
        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getAssignmentsByGroupId(group.getId()+1);

        //then
        assertNotEquals(expectedList, actual);

    }

    @Test
    void itShouldGetAllAssignmentsAssignedToStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
        expectedList.add(assignment);
        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(group.getId(), user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetAllAssignmentsAssignedToStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
        expectedList.add(assignment);
        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(group.getId()+1, user2.getId());

        //then
        assertNotEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetAllAssignmentsAssignedToStudent_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
        expectedList.add(assignment);
        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(group.getId(), user2.getId()+1);

        //then
        assertNotEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetAllAssignmentsAssignedToStudent_3() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
        expectedList.add(assignment);
        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getAllAssignmentsByGroupIdAndUserId(group.getId()+1, user2.getId()+1);

        //then
        assertNotEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetDoneAssignmentsByStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
//        expectedList.add(assignment);
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getDoneAssignmentsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetDoneAssignmentsByStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        List<Assignment> expectedList = new ArrayList<>();
//        expectedList.add(assignment);
        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getDoneAssignmentsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetDoneAssignmentsByStudent_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,6,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,16,15,0,0,0, ZoneOffset.UTC), "");
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        List<Assignment> expectedList = new ArrayList<>();
        expectedList.add(assignment2);
        expectedList.add(assignment);

        //when
        List<Assignment> actual = assignmentRepository.getDoneAssignmentsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);
    }

    @Test
    void itShouldNotGetDoneAssignmentsByStudentAndGroup() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
//        expectedList.add(assignment);
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(group.getId(), user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetDoneAssignmentsByStudentAndGroup_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        List<Assignment> expectedList = new ArrayList<>();
//        expectedList.add(assignment);
        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(group.getId(), user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetDoneAssignmentsByStudentAndGroup() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment, group, OffsetDateTime.of(2023,6,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,16,15,0,0,0, ZoneOffset.UTC), "");
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
        List<Assignment> expectedList = new ArrayList<>();
        expectedList.add(assignment2);
        expectedList.add(assignment);

        //when
        List<Assignment> actual = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(group.getId(), user2.getId());

        //then
        assertEquals(expectedList, actual);
    }

    @Test
    void itShouldGetAllAssignmentsByStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        List<Assignment> expectedList = new ArrayList<>();
        expectedList.add(assignment);
        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getAllAssignmentsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);
    }

    @Test
    void itShouldNotGetAllAssignmentsByStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
//        assignmentRepository.save(assignment);
//        assignmentRepository.save(assignment2);
//        solutionRepository.save(solution);
        List<Assignment> expectedList = new ArrayList<>();
//        expectedList.add(assignment);
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getAllAssignmentsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);
    }

    @Test
    void itShouldReturn1() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        //when
        int actual = assignmentRepository.checkForSolutionToAssignment(assignment.getId());
        //then
        assertEquals(1, actual);
    }

    @Test
    void itShouldReturn0() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 6, 15, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Assignment assignment2 = new Assignment(user, group, "opis", OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 15, 15, 0, 0, 0, ZoneOffset.UTC), OffsetDateTime.of(2023, 5, 30, 15, 0, 0, 0, ZoneOffset.UTC), "tytul", true, 100, 50);
        Solution solution = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        solutionRepository.save(solution);
        //when
        int actual = assignmentRepository.checkForSolutionToAssignment(assignment2.getId());
        //then
        assertEquals(0, actual);
    }
}
