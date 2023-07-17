package pl.HomeworkJustClick.Backend.Controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Assignment;
import pl.HomeworkJustClick.Backend.Responses.AssignmentResponse;
import pl.HomeworkJustClick.Backend.Responses.AssignmentResponseCalendar;
import pl.HomeworkJustClick.Backend.Responses.AssignmentResponseExtended;
import pl.HomeworkJustClick.Backend.Services.AssignmentService;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Assignment", description = "Assignment related calls.")
@ApiResponse(
        responseCode = "403",
        description = "Something is wrong with the token.",
        content = @Content()
)
@ApiResponse(
        responseCode = "200",
        description = "OK."
)
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping("/assignments")
    @Operation(
            summary = "Returns list of all assignments in DB.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public List<AssignmentResponse> getAll(){
        List<AssignmentResponse> responseList = assignmentService.getAll();
        responseList.sort(Comparator.comparing(AssignmentResponse::getCompletionDatetime));
        return responseList;
    }

    @GetMapping("/extended/assignments")
    @Operation(
            summary = "Returns list of all assignments with user and group in DB.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseExtended.class))
                            )
                    )
            }
    )
    public List<AssignmentResponseExtended> getAllExtended(){
        List<AssignmentResponseExtended> responseList = assignmentService.getAllExtended();
        responseList.sort(Comparator.comparing(AssignmentResponseExtended::getCompletionDatetime));
        return responseList;
    }

    @GetMapping("/assignment/{assignment_id}")
    @Operation(
            summary = "Returns assignment by it's id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignment with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssignmentResponse.class))

                    )
            }
    )
    public ResponseEntity<AssignmentResponse> getById(@PathVariable("assignment_id") int id){
        AssignmentResponse assignmentResponse = assignmentService.getById(id);
        if(assignmentResponse!=null) {
            return new ResponseEntity<>(assignmentResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/extended/assignment/{assignment_id}")
    @Operation(
            summary = "Returns assignment by it's id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignment with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssignmentResponse.class))

                    )
            }
    )
    public ResponseEntity<AssignmentResponseExtended> getByIdExtended(@PathVariable("assignment_id") int id){
        AssignmentResponseExtended assignmentResponse = assignmentService.getByIdExtended(id);
        if(assignmentResponse!=null) {
            return new ResponseEntity<>(assignmentResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/assignments/byGroupId/{group_id}")
    @Operation(
            summary = "Returns list of all assignments in the given group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignment with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponse>> getAssignmentsByGroupId(@PathVariable("group_id") int id) {
        List<AssignmentResponse> responseList = assignmentService.getAssignmentsByGroupId(id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList,HttpStatus.NOT_FOUND);
        }
        else {
            responseList.sort(Comparator.comparing(AssignmentResponse::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/byGroupId/{group_id}")
    @Operation(
            summary = "Returns list of all assignments in the given group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignment with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtended>> getAssignmentsByGroupIdExtended(@PathVariable("group_id") int id) {
        List<AssignmentResponseExtended> responseList = assignmentService.getAssignmentsByGroupIdExtended(id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList,HttpStatus.NOT_FOUND);
        }
        else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtended::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @PostMapping("/assignment")
    @Hidden
    public ResponseEntity<AssignmentResponse> add(@RequestBody Assignment assignment){
        AssignmentResponse response = assignmentService.add(assignment);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/assignment/withUserAndGroup/{user_id}/{group_id}")
    @Operation(
            summary = "Creates assignment with user and group already attached to it.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No user or group with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssignmentResponse.class))

                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The user is not a teacher in the given group",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<AssignmentResponse> addWithUserAndGroup(@PathVariable("user_id") int user_id, @PathVariable("group_id") int group_id, @RequestBody Assignment assignment) {
        AssignmentResponse response = assignmentService.addWithUserAndGroup(assignment, user_id, group_id);
        if(response.getId()!=0) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response.isForbidden()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/assignment/{assignment_id}")
    @Operation(
            summary = "Deletes assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> delete (@PathVariable("assignment_id") int id){
        if(assignmentService.delete(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/{assignment_id}")
    @Operation(
            summary = "Changes assignment with given id.",
            description = "Change whole assignment object for a given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id in the DB.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> update(@PathVariable("assignment_id") int id, @RequestBody Assignment assignment) {
        if(assignmentService.update(id, assignment)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/visibility/{assignment_id}")
    @Operation(
            summary = "Changes visibility of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateVisibility(@PathVariable("assignment_id") int id, @RequestBody Boolean visible){
        if(assignmentService.changeVisibility(id, visible)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/assignment/title/{assignment_id}")
    @Operation(
            summary = "Changes title of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id.",
                            content = @Content
                    )
            }
    )

    public ResponseEntity<Void> updateTitle(@PathVariable("assignment_id") int id, @RequestBody String title){
        if(assignmentService.changeTitleById(id, title)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/taskDescription/{assignment_id}")
    @Operation(
            summary = "Changes task description of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateTaskDescription(@PathVariable("assignment_id") int id, @RequestBody String taskDescription){
        if(assignmentService.changeTaskDescriptionById(id, taskDescription)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/completionDatetime/{assignment_id}")
    @Operation(
            summary = "Changes completion time of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateCompletionDatetime(@PathVariable("assignment_id") int id, @RequestBody OffsetDateTime completionDatetime){
        if(assignmentService.changeCompletionDatetime(id, completionDatetime)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/user/{user_id}/{assignment_id}")
    @Operation(
            summary = "Changes user of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment or user with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateUser(@PathVariable("user_id") int userId, @PathVariable("assignment_id") int assignmentId){
        if(assignmentService.changeUser(assignmentId, userId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/group/{group_id}/{assignment_id}")
    @Operation(
            summary = "Changes group of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment or group with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateGroup(@PathVariable("group_id") int groupId, @PathVariable("assignment_id") int assignmentId){
        if(assignmentService.changeGroup(assignmentId, groupId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/max_points/{assignment_id}")
    @Operation(
            summary = "Changes max points of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updatePoints(@PathVariable("assignment_id") int id, @RequestBody int points) {
        if (assignmentService.changeMaxPoints(id, points)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/auto_penalty/{assignment_id}")
    @Operation(
            summary = "Changes auto penalty of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updatePenalty(@PathVariable("assignment_id") int id, @RequestBody int auto_penalty) {
        if (assignmentService.changeAutoPenalty(id, auto_penalty)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/assignments/unchecked/{group_id}")
    @Operation(
            summary = "Returns list of unchecked assignments from group with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No unchecked assignments in the group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponse>> getUncheckedAssignmentsInGroup(@PathVariable("group_id") int group_id){
        List<AssignmentResponse> responseList = assignmentService.getUncheckedAssignmentsByGroup(group_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponse::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/allByGroupAndStudent/{group_id}/{student_id}")
    @Operation(
            summary = "Returns list of assignments of a student for a given group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignments in the group for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponse>> getAllAssignmentsByUserAndGroup(@PathVariable("group_id") int group_id, @PathVariable("student_id") int user_id) {
        List<AssignmentResponse> responseList = assignmentService.getAllAssignmentsByGroupIdAndUserId(group_id, user_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponse::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/undoneByGroupAndStudent/{group_id}/{student_id}")
    @Operation(
            summary = "Returns list of undone assignments of a student for a given group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone assignments in the group for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponse>> getUndoneAssignmentsByGroupAndStudent(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        List<AssignmentResponse> responseList = assignmentService.getUndoneAssignmentsByGroupIdAndUserId(group_id, student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponse::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/undoneByStudent/{student_id}")
    @Operation(
            summary = "Returns list of undone assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponse>> getUndoneAssignmentsByStudent(@PathVariable("student_id") int student_id) {
        List<AssignmentResponse> responseList = assignmentService.getUndoneAssignmentsByStudent(student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponse::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/doneByGroupAndStudent/{group_id}/{student_id}")
    @Operation(
            summary = "Returns list of done assignments of a student in the group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No done assignments for the student in the group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponse>> getDoneAssignmentsByGroupAndStudent(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        List<AssignmentResponse> responseList = assignmentService.getDoneAssignmentsByGroupIdAndUserId(group_id, student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponse::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/doneByStudent/{student_id}")
    @Operation(
            summary = "Returns list of done assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No done assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponse>> getDoneAssignmentsByStudent(@PathVariable("student_id") int student_id) {
        List<AssignmentResponse> responseList = assignmentService.getDoneAssignmentsByStudent(student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponse::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/expiredUndoneByGroupAndStudent/{group_id}/{student_id}")
    @Operation(
            summary = "Returns list of undone expired assignments of a student in the group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone expired assignments for the student and group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponse>> getExpiredUndoneAssignmentsByGroupAndStudent(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        List<AssignmentResponse> responseList = assignmentService.getExpiredUndoneAssignmentsByGroupIdAndUserId(group_id, student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponse::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/expiredUndoneByStudent/{student_id}")
    @Operation(
            summary = "Returns list of undone expired assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone expired assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponse>> getExpiredUndoneAssignmentsByStudent(@PathVariable("student_id") int student_id) {
        List<AssignmentResponse> responseList = assignmentService.getExpiredUndoneAssignmentsByStudent(student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        }else {
            responseList.sort(Comparator.comparing(AssignmentResponse::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/nonExpiredUndoneByGroupAndStudent/{group_id}/{student_id}")
    @Operation(
            summary = "Returns list of undone non expired assignments of a student and group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone non expired assignments for the student and group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponse>> getNonExpiredUndoneAssignmentsByGroupAndStudent(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        List<AssignmentResponse> responseList = assignmentService.getNonExpiredUndoneAssignmentsByGroupIdAndUserId(group_id, student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        }else {
            responseList.sort(Comparator.comparing(AssignmentResponse::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/nonExpiredUndoneByStudent/{student_id}")
    @Operation(
            summary = "Returns list of undone non expired assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone non expired assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponse>> getNonExpiredUndoneAssignmentsByStudent(@PathVariable("student_id") int student_id) {
        List<AssignmentResponse> responseList = assignmentService.getNonExpiredUndoneAssignmentsByStudent(student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponse::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/byStudent/{student_id}")
    @Operation(
            summary = "Returns list of all student's assignments.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponse>> getAllStudentAssignments(@PathVariable("student_id") int id){
        List<AssignmentResponse> responseList = assignmentService.getAllAssignmentsByStudent(id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponse::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/unchecked/{group_id}")
    @Operation(
            summary = "Returns list of unchecked assignments from group with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No unchecked assignments in the group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtended>> getUncheckedAssignmentsInGroupExtended(@PathVariable("group_id") int group_id){
        List<AssignmentResponseExtended> responseList = assignmentService.getUncheckedAssignmentsByGroupExtended(group_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtended::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/allByGroupAndStudent/{group_id}/{student_id}")
    @Operation(
            summary = "Returns list of assignments of a student for a given group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignments in the group for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtended>> getAllAssignmentsByUserAndGroupExtended(@PathVariable("group_id") int group_id, @PathVariable("student_id") int user_id) {
        List<AssignmentResponseExtended> responseList = assignmentService.getAllAssignmentsByGroupIdAndUserIdExtended(group_id, user_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtended::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/undoneByGroupAndStudent/{group_id}/{student_id}")
    @Operation(
            summary = "Returns list of undone assignments of a student for a given group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone assignments in the group for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtended>> getUndoneAssignmentsByGroupAndStudentExtended(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        List<AssignmentResponseExtended> responseList = assignmentService.getUndoneAssignmentsByGroupIdAndUserIdExtended(group_id, student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtended::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/undoneByStudent/{student_id}")
    @Operation(
            summary = "Returns list of undone assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtended>> getUndoneAssignmentsByStudentExtended(@PathVariable("student_id") int student_id) {
        List<AssignmentResponseExtended> responseList = assignmentService.getUndoneAssignmentsByStudentExtended(student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtended::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/doneByGroupAndStudent/{group_id}/{student_id}")
    @Operation(
            summary = "Returns list of done assignments of a student in the group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No done assignments for the student in the group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtended>> getDoneAssignmentsByGroupAndStudentExtended(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        List<AssignmentResponseExtended> responseList = assignmentService.getDoneAssignmentsByGroupIdAndUserIdExtended(group_id, student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtended::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/doneByStudent/{student_id}")
    @Operation(
            summary = "Returns list of done assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No done assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtended>> getDoneAssignmentsByStudentExtended(@PathVariable("student_id") int student_id) {
        List<AssignmentResponseExtended> responseList = assignmentService.getDoneAssignmentsByStudentExtended(student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtended::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/expiredUndoneByGroupAndStudent/{group_id}/{student_id}")
    @Operation(
            summary = "Returns list of undone expired assignments of a student in the group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone expired assignments for the student and group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtended>> getExpiredUndoneAssignmentsByGroupAndStudentExtended(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        List<AssignmentResponseExtended> responseList = assignmentService.getExpiredUndoneAssignmentsByGroupIdAndUserIdExtended(group_id, student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtended::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/expiredUndoneByStudent/{student_id}")
    @Operation(
            summary = "Returns list of undone expired assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone expired assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtended>> getExpiredUndoneAssignmentsByStudentExtended(@PathVariable("student_id") int student_id) {
        List<AssignmentResponseExtended> responseList = assignmentService.getExpiredUndoneAssignmentsByStudentExtended(student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        }else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtended::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/nonExpiredUndoneByGroupAndStudent/{group_id}/{student_id}")
    @Operation(
            summary = "Returns list of undone non expired assignments of a student and group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone non expired assignments for the student and group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtended>> getNonExpiredUndoneAssignmentsByGroupAndStudentExtended(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        List<AssignmentResponseExtended> responseList = assignmentService.getNonExpiredUndoneAssignmentsByGroupIdAndUserIdExtended(group_id, student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        }else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtended::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/nonExpiredUndoneByStudent/{student_id}")
    @Operation(
            summary = "Returns list of undone non expired assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone non expired assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtended>> getNonExpiredUndoneAssignmentsByStudentExtended(@PathVariable("student_id") int student_id) {
        List<AssignmentResponseExtended> responseList = assignmentService.getNonExpiredUndoneAssignmentsByStudentExtended(student_id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtended::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/byStudent/{student_id}")
    @Operation(
            summary = "Returns list of all student's assignments.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtended>> getAllStudentAssignmentsExtended(@PathVariable("student_id") int id){
        List<AssignmentResponseExtended> responseList = assignmentService.getAllAssignmentsByStudentExtended(id);
        if(responseList.isEmpty()){
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtended::getCompletionDatetime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignment/checkForSolution/{assignment_id}")
    @Operation(
            summary = "Checks if assignment with given id has any solution attached to it. Returns true if so, false otherwise. False may also indicate that there is no assignment in the DB with given id.",
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
    public ResponseEntity<Boolean> checkForSolutionToAssignment(@PathVariable("assignment_id") int id) {
        return new ResponseEntity<>(assignmentService.checkForSolutionToAssignment(id), HttpStatus.OK);
    }

    @GetMapping("/assignment/checkForFile/{assignment_id}")
    @Operation(
            summary = "Checks if assignment with given id has any file attached to it. Returns true if so, false otherwise. False may also indicate that there is no assignment in the DB with given id.",
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
    public ResponseEntity<Boolean> checkForFileToAssignment(@PathVariable("assignment_id") int assignment_id) {
        return new ResponseEntity<>(assignmentService.checkForFileToAssignment(assignment_id), HttpStatus.OK);
    }

    @GetMapping("/assignments/calendarListByStudent/{student_id}")
    @Operation(
            summary = "Returns list of all student's assignments to calendar.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponse.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseCalendar>> getAssignmentsByStudentCalendar(@PathVariable("student_id") int student_id) {
        return new ResponseEntity<>(assignmentService.getAssignmentsByStudentCalendar(student_id), HttpStatus.OK);
    }
}
