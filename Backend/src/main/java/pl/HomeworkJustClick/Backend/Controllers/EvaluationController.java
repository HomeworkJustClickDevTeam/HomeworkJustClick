package pl.HomeworkJustClick.Backend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Evaluation;
import pl.HomeworkJustClick.Backend.Responses.EvaluationResponse;
import pl.HomeworkJustClick.Backend.Services.EvaluationService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EvaluationController {
    @Autowired
    EvaluationService evaluationService;
    @GetMapping("/evaluations")
    public List<Evaluation> getAll(){return evaluationService.getAll();}
    @GetMapping("/evaluations/{id}")
    public Evaluation getById(@PathVariable("id") int id){return evaluationService.getById(id);}
    @PostMapping("/evaluation")
    public ResponseEntity<EvaluationResponse> add(@RequestBody Evaluation evaluation){
        EvaluationResponse response = evaluationService.add(evaluation);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/evaluation/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id){
        if(evaluationService.delete(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/evaluation/result/{id}")
    public ResponseEntity<Void> updateResult(@PathVariable("id") int id, @RequestBody Double result){
        if(evaluationService.changeResultById(id, result)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }
    @PutMapping("/evaluation/user/{user_id}/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("user_id") int userId, @PathVariable("id") int id){
        if(evaluationService.changeUserById(id, userId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    @PutMapping("/evaluation/solution/{solution_id}/{id}")
    public ResponseEntity<Void> updateSolution(@PathVariable("solution_id") int solutionId, @PathVariable("id") int id){
        if(evaluationService.changeSolutionById(id, solutionId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    @PutMapping("/evaluation/comment/{id}")
    public ResponseEntity<Void> updateComment(@RequestBody String comment, @PathVariable("id") int id){
        if(evaluationService.changeCommentById(id, comment)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/evaluation/grade/{id}")
    public ResponseEntity<Void> updateGrade(@RequestBody Double grade, @PathVariable("id") int id){
        if(evaluationService.changeGradeById(id, grade)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
