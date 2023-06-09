package pl.HomeworkJustClick.Backend.Repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.HomeworkJustClick.Backend.Entities.*;
import pl.HomeworkJustClick.Backend.Enums.Role;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
    void itShouldGetUndoneAssignmentsByStudentAndGroup() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        List<Assignment> actual = assignmentRepository.getUndoneAssignmentsByGroupIdAndUserId(group.getId(), user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetUndoneAssignmentsByStudentAndGroup_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        expectedList.add(assignment);
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getUndoneAssignmentsByGroupIdAndUserId(group.getId(), user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetUndoneAssignmentsByStudentAndGroup() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
//        expectedList.add(assignment);
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getUndoneAssignmentsByGroupIdAndUserId(group.getId(), user2.getId());

        //then
        assertEquals(expectedList, actual);
    }

    @Test
    void itShouldGetUndoneAssignmentsByStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        List<Assignment> actual = assignmentRepository.getUndoneAssignmentsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetUndoneAssignmentsByStudent_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        expectedList.add(assignment);
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getUndoneAssignmentsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetUndoneAssignmentsByStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
//        expectedList.add(assignment);
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getUndoneAssignmentsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);
    }

    @Test
    void itShouldNotGetDoneAssignmentsByStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        expectedList.add(assignment);
        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getDoneAssignmentsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);
    }

    @Test
    void itShouldNotGetDoneAssignmentsByStudentAndGroup() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        expectedList.add(assignment);
        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getDoneAssignmentsByGroupIdAndUserId(group.getId(), user2.getId());

        //then
        assertEquals(expectedList, actual);
    }

    @Test
    void itShouldGetExpiredUndoneAssignmentsByGroupAndStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
//        expectedList.add(assignment);
        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getExpiredUndoneAssignmentsByGroupIdAndUserId(group.getId(), user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetExpiredUndoneAssignmentsByGroupAndStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
//        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
//        expectedList.add(assignment);
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getExpiredUndoneAssignmentsByGroupIdAndUserId(group.getId(), user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetExpiredUndoneAssignmentsByGroupAndStudent_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getExpiredUndoneAssignmentsByGroupIdAndUserId(group.getId(), user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetExpiredUndoneAssignmentsByStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
//        expectedList.add(assignment);
        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getExpiredUndoneAssignmentsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetExpiredUndoneAssignmentsByStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
//        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
//        expectedList.add(assignment);
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getExpiredUndoneAssignmentsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetExpiredUndoneAssignmentsByStudent_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getExpiredUndoneAssignmentsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetNonExpiredUndoneAssignmentsByGroupAndStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
        expectedList.add(assignment);
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getNonExpiredUndoneAssignmentsByGroupIdAndUserId(group.getId(), user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetNonExpiredUndoneAssignmentsByGroupAndStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
//        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
        expectedList.add(assignment);
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getNonExpiredUndoneAssignmentsByGroupIdAndUserId(group.getId(), user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetNonExpiredUndoneAssignmentsByGroupAndStudent_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
//        expectedList.add(assignment);
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getNonExpiredUndoneAssignmentsByGroupIdAndUserId(group.getId(), user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetNonExpiredUndoneAssignmentsByStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
        expectedList.add(assignment);
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getNonExpiredUndoneAssignmentsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetNonExpiredUndoneAssignmentsByStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupStudentRepository.save(groupStudent);
//        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        List<Assignment> expectedList = new ArrayList<>();
//        expectedList.add(assignment);
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getNonExpiredUndoneAssignmentsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetNonExpiredUndoneAssignmentsByStudent_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
//        expectedList.add(assignment);
//        expectedList.add(assignment2);

        //when
        List<Assignment> actual = assignmentRepository.getNonExpiredUndoneAssignmentsByStudent(user2.getId());

        //then
        assertEquals(expectedList, actual);
    }

    @Test
    void itShouldGetAllAssignmentsByStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        Assignment assignment = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
        Assignment assignment2 = new Assignment(user,group,"opis", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"tytul",true,100);
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
}