package pl.HomeworkJustClick.Backend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Solution;
import pl.HomeworkJustClick.Backend.Responses.SolutionResponse;
import pl.HomeworkJustClick.Backend.Services.SolutionService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SolutionController {
    @Autowired
    SolutionService solutionService;

    @GetMapping("/solutions")
    public List<Solution> getAll(){return solutionService.getAll();}
    @GetMapping("/solution/{id}")
    public Solution getById(@PathVariable("id") int id){return solutionService.getById(id);}
    @PostMapping("/solution")
    public ResponseEntity<SolutionResponse> add(@RequestBody Solution solution){
        SolutionResponse response = solutionService.add(solution);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/solution/setUser/{user_id}/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("user_id") int userId, @PathVariable("id") int id){
        if(solutionService.changeUser(id, userId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    @PutMapping("/solution/setAssignment/{assignment_id}/{id}")
    public ResponseEntity<Void> updateAssignment(@PathVariable("assignment_id") int assignmentId, @PathVariable("id") int id){
        if(solutionService.changeAssignment(id, assignmentId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
