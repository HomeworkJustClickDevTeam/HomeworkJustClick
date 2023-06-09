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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Solution;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Responses.AssignmentResponse;
import pl.HomeworkJustClick.Backend.Responses.SolutionResponse;
import pl.HomeworkJustClick.Backend.Services.SolutionService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Solution", description = "Solution related calls.")
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
public class SolutionController {

    private final SolutionService solutionService;

    @GetMapping("/solutions")
    @Operation(
            summary = "Returns list of all solutions in DB.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    public List<SolutionResponse> getAll(){
        List<SolutionResponse> responseList = solutionService.getAll();
        responseList.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
        return responseList;
    }
    @GetMapping("/solution/{solution_id}")
    @Operation(
            summary = "Returns solution by it's id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No solution with this id in the DB.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SolutionResponse.class))

                    )
            }
    )
    public ResponseEntity<SolutionResponse> getById(@PathVariable("solution_id") int id){
        SolutionResponse solution = solutionService.getById(id);
        if (solution != null) {
            return new ResponseEntity<>(solution, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/solution")
    @Hidden
    public ResponseEntity<SolutionResponse> add(@RequestBody Solution solution){
        SolutionResponse response = solutionService.add(solution);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/solution/{solution_id}")
    @Operation(
            summary = "Deletes solution with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing solution with this id.",
                            content = @Content
                    )
            }
    )

    public ResponseEntity<Void> delete(@PathVariable("solution_id") int id){
        if(solutionService.delete(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/solution/withUserAndAssignment/{user_id}/{assignment_id}")
    @Operation(
            summary = "Creates solution with user and assignment already attached to it.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find user or assignment with given id.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SolutionResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "This user does not have access to the assignment or solution with this user and assignment already exists.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )

                    )
            }
    )
    public ResponseEntity<SolutionResponse> addWithUserAndAssignment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Fields: 'creationDatetime', 'lastModifiedDatetime', 'id' can not be changed manually but are needed in JSON. 'lastModifiedDatetime' updates by itself when the solution object changes. 'creationDatetime' is unchangeable.")
            @RequestBody Solution solution, @PathVariable("user_id") int user_id, @PathVariable("assignment_id") int assignment_id) {
        SolutionResponse response = solutionService.addWithUserAndAssignment(solution,user_id, assignment_id);
        if(response.getId()!=0) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response.isForbidden()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/solution/user/{user_id}/{solution_id}")
    @Operation(
            summary = "Changes user associated with solution.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find user or solution with given id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateUser(@PathVariable("user_id") int userId, @PathVariable("solution_id") int id){
        if(solutionService.changeUserById(id, userId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/solution/assignment/{assignment_id}/{solution_id}")
    @Operation(
            summary = "Changes assignment associated with solution.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find assignment or solution with given id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateAssignment(@PathVariable("assignment_id") int assignmentId, @PathVariable("solution_id") int id){
        if(solutionService.changeAssignmentById(id, assignmentId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Returns all solutions within a group with a given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find solution for a group with given id.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    @GetMapping("/solutions/byGroup/{group_id}")
    public ResponseEntity<List<SolutionResponse>> getSolutionsByGroupId(@PathVariable("group_id") int id) {
        if(solutionService.getSolutionsByGroupId(id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponse> responseList = solutionService.getSolutionsByGroupId(id);
            responseList.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Returns list of all solutions for a given assignment id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find solution associated with the assignment.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    @GetMapping("/solutions/byAssignment/{assignment_id}")
    public ResponseEntity<List<SolutionResponse>> getSolutionsByAssignmentId(@PathVariable("assignment_id") int id) {
        if(solutionService.getSolutionsByAssignmentId(id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponse> responseList = solutionService.getSolutionsByAssignmentId(id);
            responseList.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Returns list of all solutions handed late within the group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find solutions handed late in the group.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    @GetMapping("/solutions/lateByGroup/{group_id}")
    public ResponseEntity<List<SolutionResponse>> getLateSolutionsByGroupId(@PathVariable("group_id") int group_id) {
        if(solutionService.getLateSolutionsByGroup(group_id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponse> responseList = solutionService.getLateSolutionsByGroup(group_id);
            responseList.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Returns list of all solutions handed late within the group by the given student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find solutions or user in the group.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    @GetMapping("/solutions/lateByGroupAndStudent/{group_id}/{student_id}")
    public ResponseEntity<List<SolutionResponse>> getLateSolutionsByGroupIdAndStudentId(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        if(solutionService.getLateSolutionsByUserAndGroup(student_id, group_id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponse> responseList = solutionService.getLateSolutionsByUserAndGroup(student_id, group_id);
            responseList.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Returns list of all solutions handed late for a given assignment.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find solutions handed late for the assignment.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    @GetMapping("/solutions/lateByAssignment/{assignment_id}")
    public ResponseEntity<List<SolutionResponse>> getLateSolutionsByAssignmentId(@PathVariable("assignment_id") int assignment_id) {
        if(solutionService.getLateSolutionsByAssignment(assignment_id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponse> responseList = solutionService.getLateSolutionsByAssignment(assignment_id);
            responseList.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Returns list of all solutions handed late by a given user.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find solutions handed late by the user.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    @GetMapping("/solutions/lateByStudent/{student_id}")
    public ResponseEntity<List<SolutionResponse>> getLateSolutionsByStudentId(@PathVariable("student_id") int student_id) {
        if(solutionService.getLateSolutionsByStudent(student_id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponse> responseList = solutionService.getLateSolutionsByStudent(student_id);
            responseList.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/uncheckedByGroup/{group_id}")
    @Operation(
            summary = "Returns list of all unchecked solutions in the group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any unchecked solutions in this group.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponse>> getUncheckedSolutionsByGroup(@PathVariable("group_id") int group_id){
        List<SolutionResponse> response = solutionService.getUncheckedSolutionsByGroup(group_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/uncheckedByStudent/{student_id}")
    @Operation(
            summary = "Returns list of all unchecked solutions by a given student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any unchecked solutions for the student.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponse>> getUncheckedSolutionsByStudent(@PathVariable("student_id") int student_id){
        List<SolutionResponse> response = solutionService.getUncheckedSolutionsByStudent(student_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/uncheckedByStudentAndGroup/{student_id}/{group_id}")
    @Operation(
            summary = "Returns list of all unchecked solutions by a given student in a group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any unchecked solutions for the student in the group.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponse>> getUncheckedSolutionsByStudentAndGroup(@PathVariable("student_id") int student_id, @PathVariable("group_id") int group_id){
        List<SolutionResponse> response = solutionService.getUncheckedSolutionsByStudentAndGroup(student_id, group_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/uncheckedByAssignment/{assignment_id}")
    @Operation(
            summary = "Returns list of all unchecked solutions for a given assignment.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any unchecked solutions for the assignment.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponse>> getUncheckedSolutionsByAssignment(@PathVariable("assignment_id") int assignment_id){
        List<SolutionResponse> response = solutionService.getUncheckedSolutionsByAssignment(assignment_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/uncheckedByTeacher/{teacher_id}")
    @Operation(
            summary = "Returns list of all unchecked solutions by a given teacher.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any unchecked solutions by this teacher.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponse>> getUncheckedSolutionsByTeacher(@PathVariable("teacher_id") int teacher_id){
        List<SolutionResponse> response = solutionService.getUncheckedSolutionsByTeacher(teacher_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/checkedByGroup/{group_id}")
    @Operation(
            summary = "Returns list of all checked solutions in the group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any checked solutions in this group.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponse>> getCheckedSolutionsByGroup(@PathVariable("group_id") int group_id){
        List<SolutionResponse> response = solutionService.getCheckedSolutionsByGroup(group_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/checkedByStudent/{student_id}")
    @Operation(
            summary = "Returns list of all checked solutions by a given student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any checked solutions for the student.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponse>> getCheckedSolutionsByStudent(@PathVariable("student_id") int student_id){
        List<SolutionResponse> response = solutionService.getCheckedSolutionsByStudent(student_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/checkedByStudentAndGroup/{student_id}/{group_id}")
    @Operation(
            summary = "Returns list of all checked solutions by a given student in a group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any checked solutions for the student in the group.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponse>> getCheckedSolutionsByStudentAndGroup(@PathVariable("student_id") int student_id, @PathVariable("group_id") int group_id){
        List<SolutionResponse> response = solutionService.getCheckedSolutionsByStudentAndGroup(student_id, group_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/checkedByAssignment/{assignment_id}")
    @Operation(
            summary = "Returns list of all checked solutions for a given assignment.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any checked solutions for the assignment.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponse>> getCheckedSolutionsByAssignment(@PathVariable("assignment_id") int assignment_id){
        List<SolutionResponse> response = solutionService.getCheckedSolutionsByAssignment(assignment_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/checkedByTeacher/{teacher_id}")
    @Operation(
            summary = "Returns list of all checked solutions by a given teacher.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any checked solutions by this teacher.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponse.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponse>> getCheckedSolutionsByTeacher(@PathVariable("teacher_id") int teacher_id){
        List<SolutionResponse> response = solutionService.getCheckedSolutionsByTeacher(teacher_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponse::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

}
