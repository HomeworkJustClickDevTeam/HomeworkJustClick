package pl.HomeworkJustClick.Backend.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Assignment;
import pl.HomeworkJustClick.Backend.Responses.AssignmentResponse;
import pl.HomeworkJustClick.Backend.Services.AssignmentService;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AssignmentController {
    @Autowired
    AssignmentService assignmentService;

    @GetMapping("/assignments")
    public List<Assignment> getAll(){return assignmentService.getAll();}
    @GetMapping("/assignment/{id}")
    public Assignment getById(@PathVariable("id") int id){return assignmentService.getById(id);}
    @PostMapping("/assignment")
    public ResponseEntity<AssignmentResponse> add(@RequestBody Assignment assignment){
        if(assignmentService.add(assignment)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @DeleteMapping("/assignment/{id}")
    public ResponseEntity<Void> delete (@PathVariable("id") int id){
        if(assignmentService.delete(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/assignment/title/{id}")
    public ResponseEntity<Void> updateTitle(@PathVariable("id") int id, @RequestBody String title){
        if(assignmentService.changeTitleById(id, title)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/assignment/taskDescription/{id}")
    public ResponseEntity<Void> updateTaskDescription(@PathVariable("id") int id, @RequestBody String taskDescription){
        if(assignmentService.changeTaskDescriptionById(id, taskDescription)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    @PutMapping("/assignment/completionDatetime/{id}")
    public ResponseEntity<Void> updateCompletionDatetime(@PathVariable("id") int id, @RequestBody OffsetDateTime completionDatetime){
        if(assignmentService.changeCompletionDatetime(id, completionDatetime)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
