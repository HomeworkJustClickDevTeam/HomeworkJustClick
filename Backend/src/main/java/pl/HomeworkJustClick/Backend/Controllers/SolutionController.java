package pl.HomeworkJustClick.Backend.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Solution;
import pl.HomeworkJustClick.Backend.Responses.SolutionResponse;
import pl.HomeworkJustClick.Backend.Services.SolutionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Solution")
public class SolutionController {
    @Autowired
    SolutionService solutionService;

    @GetMapping("/solutions")
    public List<Solution> getAll(){return solutionService.getAll();}
    @GetMapping("/solution/{id}")
    public ResponseEntity<Solution> getById(@PathVariable("id") int id){
        Optional<Solution> solution = solutionService.getById(id);
        return solution.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/solution")
    public ResponseEntity<SolutionResponse> add(@RequestBody Solution solution){
        SolutionResponse response = solutionService.add(solution);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/solution/user/{user_id}/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("user_id") int userId, @PathVariable("id") int id){
        if(solutionService.changeUserById(id, userId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/solution/assignment/{assignment_id}/{id}")
    public ResponseEntity<Void> updateAssignment(@PathVariable("assignment_id") int assignmentId, @PathVariable("id") int id){
        if(solutionService.changeAssignmentById(id, assignmentId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
