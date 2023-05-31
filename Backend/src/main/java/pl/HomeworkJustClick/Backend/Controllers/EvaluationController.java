package pl.HomeworkJustClick.Backend.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Evaluation;
import pl.HomeworkJustClick.Backend.Responses.EvaluationResponse;
import pl.HomeworkJustClick.Backend.Services.EvaluationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @GetMapping("/evaluations")
    public List<Evaluation> getAll(){return evaluationService.getAll();}
    @GetMapping("/evaluations/{id}")
    public ResponseEntity<Evaluation> getById(@PathVariable("id") int id){
        Optional<Evaluation> evaluation = evaluationService.getById(id);
        return evaluation.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/evaluation")
    public ResponseEntity<EvaluationResponse> add(@RequestBody Evaluation evaluation){
        EvaluationResponse response = evaluationService.add(evaluation);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/evaluation/withUserAndSolution/{user_id}/{solution_id}")
    public ResponseEntity<EvaluationResponse> addWithUserAndSolution(@RequestBody Evaluation evaluation, @PathVariable("user_id") int user_id, @PathVariable("solution_id") int solution_id) {
        EvaluationResponse response = evaluationService.addWithUserAndSolution(evaluation, user_id, solution_id);
        if(response.getId()!=0) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response.isForbidden()){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/evaluation/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id){
        if(evaluationService.delete(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/evaluation/result/{id}")
    public ResponseEntity<Void> updateResult(@PathVariable("id") int id, @RequestBody Double result){
        if(evaluationService.changeResultById(id, result)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @PutMapping("/evaluation/user/{user_id}/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("user_id") int userId, @PathVariable("id") int id){
        if(evaluationService.changeUserById(id, userId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/evaluation/solution/{solution_id}/{id}")
    public ResponseEntity<Void> updateSolution(@PathVariable("solution_id") int solutionId, @PathVariable("id") int id){
        if(evaluationService.changeSolutionById(id, solutionId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/evaluation/grade/{id}")
    public ResponseEntity<Void> updateGrade(@RequestBody Double grade, @PathVariable("id") int id){
        if(evaluationService.changeGradeById(id, grade)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/evaluations/byStudent/{student_id}")
    public List<Evaluation> getAllEvaluationsByStudent(@PathVariable("student_id") int student_id) {
        return evaluationService.getAllEvaluationsByStudent(student_id);
    }

    @GetMapping("/evaluations/byStudentAndGroup/{student_id}/{group_id}")
    public List<Evaluation> getAllEvaluationsByStudentInGroup(@PathVariable("student_id") int student_id, @PathVariable("group_id") int group_id) {
        return evaluationService.getAllEvaluationsByStudentInGroup(student_id, group_id);
    }

    @GetMapping("/evaluations/byAssignment/{assignment_id}")
    public List<Evaluation> getAllEvaluationsByAssignment(@PathVariable("assignment_id") int assignment_id) {
        return evaluationService.getAllEvaluationsByAssignment(assignment_id);
    }

    @GetMapping("/evaluation/bySolution/{solution_id}")
    public Evaluation getEvaluationBySolution(@PathVariable("solution_id") int solution_id) {
        return evaluationService.getEvaluationBySolution(solution_id);
    }

}
