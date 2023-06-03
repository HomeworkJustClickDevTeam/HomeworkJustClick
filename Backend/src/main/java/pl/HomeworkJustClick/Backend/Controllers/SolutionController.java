package pl.HomeworkJustClick.Backend.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;

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

    @PostMapping("/solution/withUserAndAssignment/{user_id}/{assignment_id}")
    public ResponseEntity<SolutionResponse> addWithUserAndAssignment(@RequestBody Solution solution, @PathVariable("user_id") int user_id, @PathVariable("assignment_id") int assignment_id) {
        SolutionResponse response = solutionService.addWithUserAndAssignment(solution,user_id, assignment_id);
        if(response.getId()!=0) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response.isForbidden()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
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

    @GetMapping("/solutions/byGroup/{id}")
    public List<SolutionResponse> getSolutionsByGroupId(@PathVariable("id") int id) {
        return solutionService.getSolutionsByGroupId(id);
    }

    @GetMapping("/solutions/byAssignment/{id}")
    public List<SolutionResponse> getSolutionsByAssignmentId(@PathVariable("id") int id) {
        return solutionService.getSolutionsByAssignmentId(id);
    }

    @GetMapping("/solutions/lateByGroup/{group_id}")
    public List<Solution> getLateSolutionsByGroupId(@PathVariable("group_id") int group_id) {
        return solutionService.getLateSolutionsByGroup(group_id);
    }

    @GetMapping("/solutions/lateByGroupAndStudent/{group_id}/{student_id}")
    public List<Solution> getLateSolutionsByGroupIdAndStudentId(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        return solutionService.getLateSolutionsByUserAndGroup(student_id, group_id);
    }

    @GetMapping("/solutions/lateByAssignment/{assignment_id}")
    public List<Solution> getLateSolutionsByAssignmentId(@PathVariable("assignment_id") int assignment_id) {
        return solutionService.getLateSolutionsByAssignment(assignment_id);
    }

    @GetMapping("/solutions/lateByStudent/{student_id}")
    public List<Solution> getLateSolutionsByStudentId(@PathVariable("student_id") int student_id) {
        return solutionService.getLateSolutionsByStudent(student_id);
    }
}
