package pl.HomeworkJustClick.Backend.Controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Assignment;
import pl.HomeworkJustClick.Backend.Responses.AssignmentResponse;
import pl.HomeworkJustClick.Backend.Services.AssignmentService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Assignment")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping("/assignments")
    public List<Assignment> getAll(){return assignmentService.getAll();}
    @GetMapping("/assignment/{id}")
    public ResponseEntity<Assignment> getById(@PathVariable("id") int id){
        Optional<Assignment> assignment = assignmentService.getById(id);
        return assignment.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/assignments/byGroupId/{id}")
    public List<AssignmentResponse> getAssignmentsByGroupId(@PathVariable("id") int id) {
        return assignmentService.getAssignmentsByGroupId(id);
    }

    @PostMapping("/assignment")
    public ResponseEntity<AssignmentResponse> add(@RequestBody Assignment assignment){
        AssignmentResponse response = assignmentService.add(assignment);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/assignment/withUserAndGroup/{user_id}/{group_id}")
    public ResponseEntity<AssignmentResponse> addWithUserAndGroup(@PathVariable("user_id") int user_id, @PathVariable("group_id") int group_id, @RequestBody Assignment assignment) {
        AssignmentResponse response = assignmentService.addWithUserAndGroup(assignment, user_id, group_id);
        if(response.getId()!=0) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response.isForbidden()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/assignment/{id}")
    public ResponseEntity<Void> delete (@PathVariable("id") int id){
        if(assignmentService.delete(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") int id, @RequestBody Assignment assignment) {
        if(assignmentService.update(id, assignment)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/visibility/{id}")
    public ResponseEntity<Void> updateVisibility(@PathVariable("id") int id, @RequestBody Boolean visible){
        if(assignmentService.changeVisibility(id, visible)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/assignment/title/{id}")
    public ResponseEntity<Void> updateTitle(@PathVariable("id") int id, @RequestBody String title){
        if(assignmentService.changeTitleById(id, title)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/taskDescription/{id}")
    public ResponseEntity<Void> updateTaskDescription(@PathVariable("id") int id, @RequestBody String taskDescription){
        if(assignmentService.changeTaskDescriptionById(id, taskDescription)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/assignment/completionDatetime/{id}")
    public ResponseEntity<Void> updateCompletionDatetime(@PathVariable("id") int id, @RequestBody OffsetDateTime completionDatetime){
        if(assignmentService.changeCompletionDatetime(id, completionDatetime)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/assignment/user/{user_id}/{assignment_id}")
    public ResponseEntity<Void> updateUser(@PathVariable("user_id") int userId, @PathVariable("assignment_id") int assignmentId){
        if(assignmentService.changeUser(assignmentId, userId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/assignment/group/{group_id}/{assignment_id}")
    public ResponseEntity<Void> updateGroup(@PathVariable("group_id") int groupId, @PathVariable("assignment_id") int assignmentId){
        if(assignmentService.changeGroup(assignmentId, groupId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/max_points/{assignment_id}")
    public ResponseEntity<Void> updatePoints(@PathVariable("assignment_id") int id, @RequestBody int points) {
        if(assignmentService.changeMaxPoints(id, points)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/assignments/unchecked/{group_id}")
    public List<AssignmentResponse> getUncheckedAssignmentsInGroup(@PathVariable("group_id") int group_id){
        return assignmentService.getUncheckedAssignmentsByGroup(group_id);
    }

    @GetMapping("/assignments/allByGroupAndStudent/{group_id}/{student_id}")
    public List<Assignment> getAllAssigmentsByUserAndGroup(@PathVariable("group_id") int group_id, @PathVariable("student_id") int user_id) {
        return assignmentService.getAllAssignmentsByGroupIdAndUserId(group_id, user_id);
    }

    @GetMapping("/assignments/undoneByGroupAndStudent/{group_id}/{student_id}")
    public List<Assignment> getUndoneAssignmentsByGroupAndStudent(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        return assignmentService.getUndoneAssignmentsByGroupIdAndUserId(group_id, student_id);
    }

    @GetMapping("/assignments/undoneByStudent/{student_id}")
    public List<Assignment> getUndoneAssignmentsByStudent(@PathVariable("student_id") int student_id) {
        return assignmentService.getUndoneAssignmentsByStudent(student_id);
    }

    @GetMapping("/assignments/doneByGroupAndStudent/{group_id}/{student_id}")
    public List<Assignment> getDoneAssignmentsByGroupAndStudent(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        return assignmentService.getDoneAssignmentsByGroupIdAndUserId(group_id, student_id);
    }

    @GetMapping("/assignments/doneByStudent/{student_id}")
    public List<Assignment> getDoneAssignmentsByStudent(@PathVariable("student_id") int student_id) {
        return assignmentService.getDoneAssignmentsByStudent(student_id);
    }

    @GetMapping("/assignments/expiredUndoneByGroupAndStudent/{group_id}/{student_id}")
    public List<Assignment> getExpiredUndoneAssignmentsByGroupAndStudent(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        return assignmentService.getExpiredUndoneAssignmentsByGroupIdAndUserId(group_id, student_id);
    }

    @GetMapping("/assignments/expiredUndoneByStudent/{student_id}")
    public List<Assignment> getExpiredUndoneAssignmentsByStudent(@PathVariable("student_id") int student_id) {
        return assignmentService.getExpiredUndoneAssignmentsByStudent(student_id);
    }

    @GetMapping("/assignments/nonExpiredUndoneByGroupAndStudent/{group_id}/{student_id}")
    public List<Assignment> getNonExpiredUndoneAssignmentsByGroupAndStudent(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        return assignmentService.getNonExpiredUndoneAssignmentsByGroupIdAndUserId(group_id, student_id);
    }

    @GetMapping("/assignments/nonExpiredUndoneByStudent/{student_id}")
    public List<Assignment> getNonExpiredUndoneAssignmentsByStudent(@PathVariable("student_id") int student_id) {
        return assignmentService.getNonExpiredUndoneAssignmentsByStudent(student_id);
    }

    @GetMapping("/assignments/byStudent/{student_id}")
    public List<Assignment> getAllStudentAssignments(@PathVariable("student_id") int id){
        return assignmentService.getAllAssignmentsByStudent(id);
    }
}
