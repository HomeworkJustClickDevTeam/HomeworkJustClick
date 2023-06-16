package pl.HomeworkJustClick.Backend.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.HomeworkJustClick.Backend.Auth.RegisterRequest;
import pl.HomeworkJustClick.Backend.Entities.*;
import pl.HomeworkJustClick.Backend.Enums.Role;
import pl.HomeworkJustClick.Backend.Repositories.*;
import pl.HomeworkJustClick.Backend.Services.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/api")
@Tag(name = "Index", description = "Generate model")
@ApiResponse(
        responseCode = "200",
        description = "OK."
)
@AllArgsConstructor
public class IndexController {

    private UserRepository userRepository;

    private GroupRepository groupRepository;

    private GroupTeacherRepository groupTeacherRepository;

    private GroupStudentRepository groupStudentRepository;

    private AssignmentRepository assignmentRepository;

    private SolutionRepository solutionRepository;

    private EvaluationRepository evaluationRepository;

    private FileRepository fileRepository;

    private AuthenticationService authenticationService;

    @PostMapping("/generateModel")
    @Operation(
            summary = "Generates sample data.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "string",
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )
    public ResponseEntity<Void> generateModel() {
        User user = new User("dadwa@wda.com", "$2a$10$OX66s75MRAav3e5Mcov0/e1tN9EqzkzenPBOTrnj4j1I8Fzxdv0aO", true, Role.USER, 123123, "Jan", "Kowalski", 0);
        User user2 = new User("anna_malinowska@gmail.com", "$2a$10$b9GkDe6hi6o5SoRhsRp0PuUBo8tIWzpAJaCYeqsTHfA/90hvHDKeu", true, Role.USER, 321321, "Anna", "Malinowska", 1);
        User user3 = new User("adam_nowak@gmail.com", "$2a$10$ktWVwnOZvlhmTSfYonNw6ur1fpDISQiwOzto9S2wJfd0oh1tJ7Wmi", true, Role.USER, 222222, "Adam", "Nowak", 2);
        User user4 = new User("zofia_danielska@gmail.com", "$2a$10$N0zNzGXIT9u5dOJ/htLEfeFc9XruzVuEJ1qplNa9/47vEYISDdh6a", true, Role.USER, 333333, "Zofia", "Danielska", 3);
        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        Group group = new Group("Podstawy programowania", "Grupa 11",5,false);
        Group group1 = new Group("Analiza matematyczna", "Grupa 12", 8, false);
        groupRepository.save(group);
        groupRepository.save(group1);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupTeacher groupTeacher1 = new GroupTeacher(group, user3, "");
        GroupTeacher groupTeacher2 = new GroupTeacher(group1, user, "");
        GroupTeacher groupTeacher3 = new GroupTeacher(group1, user2, "");
        GroupStudent groupStudent = new GroupStudent(group, user2, "");
        GroupStudent groupStudent1 = new GroupStudent(group, user4, "");
        GroupStudent groupStudent2 = new GroupStudent(group1, user3, "");
        GroupStudent groupStudent3 = new GroupStudent(group1, user4, "");
        groupTeacherRepository.save(groupTeacher);
        groupTeacherRepository.save(groupTeacher1);
        groupTeacherRepository.save(groupTeacher2);
        groupTeacherRepository.save(groupTeacher3);
        groupStudentRepository.save(groupStudent);
        groupStudentRepository.save(groupStudent1);
        groupStudentRepository.save(groupStudent2);
        groupStudentRepository.save(groupStudent3);
        Assignment assignment = new Assignment(user,group,"Instrukcje warunkowe", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"Instrukcje warunkowe",true,100);
        Assignment assignment2 = new Assignment(user3,group,"Pętle", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"Pętle",true,100);
        Assignment assignment3 = new Assignment(user,group,"Funkcje", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"Funkcje",true,100);
        Assignment assignment4 = new Assignment(user2,group1,"Całki oznaczone", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"Całki oznaczone",true,100);
        Assignment assignment5 = new Assignment(user2,group1,"Szeregi", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,6,15,15,0,0,0, ZoneOffset.UTC),"Szeregi",true,100);
        Assignment assignment6 = new Assignment(user,group1,"Całki nieoznaczone", OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,15,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,30,15,0,0,0, ZoneOffset.UTC),"Całki nieoznaczone",true,100);
        assignmentRepository.save(assignment);
        assignmentRepository.save(assignment2);
        assignmentRepository.save(assignment3);
        assignmentRepository.save(assignment4);
        assignmentRepository.save(assignment5);
        assignmentRepository.save(assignment6);
        Solution solution = new Solution(user2, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution2 = new Solution(user2, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
//        Solution solution3 = new Solution(user2, assignment3, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution4 = new Solution(user4, assignment, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution5 = new Solution(user4, assignment2, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution6 = new Solution(user4, assignment3, group, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution7 = new Solution(user3, assignment4, group1, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution8 = new Solution(user3, assignment5, group1, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution10 = new Solution(user4, assignment4, group1, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        Solution solution11 = new Solution(user4, assignment5, group1, OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC),OffsetDateTime.of(2023,5,16,15,0,0,0, ZoneOffset.UTC), "");
        solutionRepository.save(solution);
        solutionRepository.save(solution2);
//        solutionRepository.save(solution3);
        solutionRepository.save(solution4);
        solutionRepository.save(solution5);
        solutionRepository.save(solution6);
        solutionRepository.save(solution7);
        solutionRepository.save(solution8);
        solutionRepository.save(solution11);
        solutionRepository.save(solution10);
        Evaluation evaluation = new Evaluation(3.0, user, solution, group, OffsetDateTime.now(), OffsetDateTime.now(), 3.0);
        Evaluation evaluation2 = new Evaluation(4.0, user, solution2, group, OffsetDateTime.now(), OffsetDateTime.now(), 4.0);
//        Evaluation evaluation3 = new Evaluation(5.0, user, solution3, group, OffsetDateTime.now(), OffsetDateTime.now(), 5.0);
        Evaluation evaluation4 = new Evaluation(3.5, user, solution4, group, OffsetDateTime.now(), OffsetDateTime.now(), 3.5);
        Evaluation evaluation5 = new Evaluation(4.0, user, solution5, group, OffsetDateTime.now(), OffsetDateTime.now(), 4.0);
        Evaluation evaluation6 = new Evaluation(4.5, user, solution6, group, OffsetDateTime.now(), OffsetDateTime.now(), 4.5);
        Evaluation evaluation7 = new Evaluation(5.0, user, solution7, group, OffsetDateTime.now(), OffsetDateTime.now(), 5.0);
        Evaluation evaluation8 = new Evaluation(3.0, user, solution8, group, OffsetDateTime.now(), OffsetDateTime.now(), 3.0);
        evaluationRepository.save(evaluation);
        evaluationRepository.save(evaluation2);
//        evaluationRepository.save(evaluation3);
        evaluationRepository.save(evaluation4);
        evaluationRepository.save(evaluation5);
        evaluationRepository.save(evaluation6);
        evaluationRepository.save(evaluation7);
        evaluationRepository.save(evaluation8);
        File file = new File("plik.txt", "txt", "123", assignment, null);
        File file2 = new File("plik2.txt", "txt", "222", assignment2, null);
        File file3 = new File("plik3.txt", "txt", "321", assignment3, null);
        File file4 = new File("plik4.txt", "txt", "444", assignment3, null);
        File file5 = new File("plik5.txt", "txt", "555", assignment4, null);
        File file6 = new File("plik6.txt", "txt", "666", assignment5, null);
        File file7 = new File("plik7.txt", "txt", "777", assignment5, null);
        File file8 = new File("plik.txt", "txt", "888", null, solution);
        File file9 = new File("plik.txt", "txt", "123333", null, solution);
        File file10 = new File("plik2.txt", "txt", "222333", null, solution2);
        File file11 = new File("plik3.txt", "txt", "32133", null, solution2);
//        File file12 = new File("plik4.txt", "txt", "4443", null, solution3);
        File file13 = new File("plik5.txt", "txt", "55533", null, solution4);
        File file14 = new File("plik6.txt", "txt", "6663", null, solution5);
        File file15 = new File("plik7.txt", "txt", "7773", null, solution6);
        File file16 = new File("plik.txt", "txt", "8883", null, solution6);
        File file17 = new File("plik5.txt", "txt", "5553344", null, solution7);
        File file18 = new File("plik6.txt", "txt", "6663444", null, solution10);
        File file19 = new File("plik7.txt", "txt", "7773555", null, solution10);
        File file20 = new File("plik.txt", "txt", "8883666", null, solution10);
        fileRepository.save(file);
        fileRepository.save(file2);
        fileRepository.save(file3);
        fileRepository.save(file4);
        fileRepository.save(file5);
        fileRepository.save(file6);
        fileRepository.save(file7);
        fileRepository.save(file8);
        fileRepository.save(file9);
        fileRepository.save(file10);
        fileRepository.save(file11);
//        fileRepository.save(file12);
        fileRepository.save(file13);
        fileRepository.save(file14);
        fileRepository.save(file15);
        fileRepository.save(file16);
        fileRepository.save(file17);
        fileRepository.save(file18);
        fileRepository.save(file19);
        fileRepository.save(file20);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
