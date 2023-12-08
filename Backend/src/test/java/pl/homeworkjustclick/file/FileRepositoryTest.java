package pl.homeworkjustclick.file;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.homeworkjustclick.assignment.Assignment;
import pl.homeworkjustclick.assignment.AssignmentRepository;
import pl.homeworkjustclick.evaluation.Evaluation;
import pl.homeworkjustclick.evaluation.EvaluationRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class FileRepositoryTest {

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

    @Autowired
    private FileRepository fileRepository;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void itShouldGetFilesByAssignmentId() {
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
        File file = new File("plik.txt", "txt", "123", assignment, null);
        File file2 = new File("plik2.txt", "txt", "222", assignment2, null);
        File file3 = new File("plik3.txt", "txt", "321", null, solution);
        File file4 = new File("plik4.txt", "txt", "444", null, solution2);
        File file5 = new File("plik5.txt", "txt", "555", null, solution3);
        File file6 = new File("plik6.txt", "txt", "666", null, solution4);
        File file7 = new File("plik7.txt", "txt", "777", null, solution4);
        File file8 = new File("plik.txt", "txt", "888", assignment, null);
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
        fileRepository.save(file);
        fileRepository.save(file2);
        fileRepository.save(file3);
        fileRepository.save(file4);
        fileRepository.save(file5);
        fileRepository.save(file6);
        fileRepository.save(file7);
        fileRepository.save(file8);
        List<File> expectedList = new ArrayList<>();
        expectedList.add(file);
        expectedList.add(file8);


        //when
        List<File> actual = fileRepository.getFilesByAssignmentId(assignment.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetFilesByAssignmentId() {
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
        File file = new File("plik.txt", "txt", "123", assignment, null);
        File file2 = new File("plik2.txt", "txt", "222", assignment2, null);
        File file3 = new File("plik3.txt", "txt", "321", null, solution);
        File file4 = new File("plik4.txt", "txt", "444", null, solution2);
        File file5 = new File("plik5.txt", "txt", "555", null, solution3);
        File file6 = new File("plik6.txt", "txt", "666", null, solution4);
        File file7 = new File("plik7.txt", "txt", "777", null, solution4);
        File file8 = new File("plik.txt", "txt", "888", assignment, null);
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
//        fileRepository.save(file);
        fileRepository.save(file2);
        fileRepository.save(file3);
        fileRepository.save(file4);
        fileRepository.save(file5);
        fileRepository.save(file6);
        fileRepository.save(file7);
//        fileRepository.save(file8);
        List<File> expectedList = new ArrayList<>();
//        expectedList.add(file);
//        expectedList.add(file8);


        //when
        List<File> actual = fileRepository.getFilesByAssignmentId(assignment.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldGetFilesBySolutionId() {
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
        File file = new File("plik.txt", "txt", "123", assignment, null);
        File file2 = new File("plik2.txt", "txt", "222", assignment2, null);
        File file3 = new File("plik3.txt", "txt", "321", null, solution);
        File file4 = new File("plik4.txt", "txt", "444", null, solution2);
        File file5 = new File("plik5.txt", "txt", "555", null, solution3);
        File file6 = new File("plik6.txt", "txt", "666", null, solution4);
        File file7 = new File("plik7.txt", "txt", "777", null, solution4);
        File file8 = new File("plik.txt", "txt", "888", assignment, null);
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
        fileRepository.save(file);
        fileRepository.save(file2);
        fileRepository.save(file3);
        fileRepository.save(file4);
        fileRepository.save(file5);
        fileRepository.save(file6);
        fileRepository.save(file7);
        fileRepository.save(file8);
        List<File> expectedList = new ArrayList<>();
        expectedList.add(file6);
        expectedList.add(file7);


        //when
        List<File> actual = fileRepository.getFilesBySolutionId(solution4.getId());

        //then
        assertEquals(expectedList, actual);

    }

    @Test
    void itShouldNotGetFilesBySolutionId() {
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
        File file = new File("plik.txt", "txt", "123", assignment, null);
        File file2 = new File("plik2.txt", "txt", "222", assignment2, null);
        File file3 = new File("plik3.txt", "txt", "321", null, solution);
        File file4 = new File("plik4.txt", "txt", "444", null, solution2);
        File file5 = new File("plik5.txt", "txt", "555", null, solution3);
        File file6 = new File("plik6.txt", "txt", "666", null, solution4);
        File file7 = new File("plik7.txt", "txt", "777", null, solution4);
        File file8 = new File("plik.txt", "txt", "888", assignment, null);
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
        fileRepository.save(file);
        fileRepository.save(file2);
        fileRepository.save(file3);
        fileRepository.save(file4);
        fileRepository.save(file5);
//        fileRepository.save(file6);
//        fileRepository.save(file7);
        fileRepository.save(file8);
        List<File> expectedList = new ArrayList<>();
//        expectedList.add(file);
//        expectedList.add(file8);


        //when
        List<File> actual = fileRepository.getFilesBySolutionId(solution4.getId());

        //then
        assertEquals(expectedList, actual);

    }
}
