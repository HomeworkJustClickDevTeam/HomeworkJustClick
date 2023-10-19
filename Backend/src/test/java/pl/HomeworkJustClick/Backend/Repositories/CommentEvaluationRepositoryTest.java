package pl.HomeworkJustClick.Backend.Repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.HomeworkJustClick.Backend.assignment.Assignment;
import pl.HomeworkJustClick.Backend.assignment.AssignmentRepository;
import pl.HomeworkJustClick.Backend.comment.Comment;
import pl.HomeworkJustClick.Backend.comment.CommentRepository;
import pl.HomeworkJustClick.Backend.commentevaluation.CommentEvaluation;
import pl.HomeworkJustClick.Backend.commentevaluation.CommentEvaluationRepository;
import pl.HomeworkJustClick.Backend.evaluation.Evaluation;
import pl.HomeworkJustClick.Backend.evaluation.EvaluationRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CommentEvaluationRepositoryTest {

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
    private CommentRepository commentRepository;

    @Autowired
    private CommentEvaluationRepository commentEvaluationRepository;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void itShouldGetCommentEvaluationByCommentIdAndEvaluationId() {
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
        Comment comment = new Comment("Tytul", "Opis", user);
        CommentEvaluation commentEvaluation = new CommentEvaluation(evaluation, comment, "");
        Comment comment2 = new Comment("Tytul", "Opis", user);
        CommentEvaluation commentEvaluation2 = new CommentEvaluation(evaluation2, comment2, "");
        Comment comment3 = new Comment("Tytul", "Opis", user);
        CommentEvaluation commentEvaluation3 = new CommentEvaluation(evaluation3, comment3, "");
        Comment comment4 = new Comment("Tytul", "Opis", user);
        CommentEvaluation commentEvaluation4 = new CommentEvaluation(evaluation, comment4, "");
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
        commentRepository.save(comment);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);
        commentEvaluationRepository.save(commentEvaluation);
        commentEvaluationRepository.save(commentEvaluation2);
        commentEvaluationRepository.save(commentEvaluation3);
        commentEvaluationRepository.save(commentEvaluation4);

        //when
        Optional<CommentEvaluation> actual = commentEvaluationRepository.getCommentEvaluationByCommentAndEvaluation(comment.getId(), evaluation.getId());

        //then
        assertTrue(actual.isPresent());
        assertEquals(commentEvaluation, actual.get());

    }

    @Test
    void itShouldNotGetCommentEvaluationByCommentIdAndEvaluationId() {
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
        Comment comment = new Comment("Tytul", "Opis", user);
        CommentEvaluation commentEvaluation = new CommentEvaluation(evaluation, comment, "");
        Comment comment2 = new Comment("Tytul", "Opis", user);
        CommentEvaluation commentEvaluation2 = new CommentEvaluation(evaluation2, comment2, "");
        Comment comment3 = new Comment("Tytul", "Opis", user);
        CommentEvaluation commentEvaluation3 = new CommentEvaluation(evaluation3, comment3, "");
        Comment comment4 = new Comment("Tytul", "Opis", user);
        CommentEvaluation commentEvaluation4 = new CommentEvaluation(evaluation, comment4, "");
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
        commentRepository.save(comment);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);
        commentEvaluationRepository.save(commentEvaluation);
        commentEvaluationRepository.save(commentEvaluation2);
        commentEvaluationRepository.save(commentEvaluation3);
        commentEvaluationRepository.save(commentEvaluation4);

        //when
        Optional<CommentEvaluation> actual = commentEvaluationRepository.getCommentEvaluationByCommentAndEvaluation(comment2.getId(), evaluation.getId());

        //then
        assertTrue(actual.isEmpty());

    }
}
