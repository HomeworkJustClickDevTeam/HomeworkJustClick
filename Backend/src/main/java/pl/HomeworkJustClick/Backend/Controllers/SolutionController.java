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
import pl.HomeworkJustClick.Backend.Responses.SolutionResponse;
import pl.HomeworkJustClick.Backend.Services.SolutionService;

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
                                    array = @ArraySchema(schema = @Schema(implementation = Solution.class))
                            )
                    )
            }
    )
    public List<SolutionResponse> getAll(){return solutionService.getAll();}
    @GetMapping("/solution/{id}")
    @Operation(
            summary = "Gets solution by it's id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No solution with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Solution.class))

                    )
            }
    )
    public ResponseEntity<SolutionResponse> getById(@PathVariable("id") int id){
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

    @DeleteMapping("/solution/{id}")
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

    public ResponseEntity<Void> delete(@PathVariable("id") int id){
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
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SolutionResponse.class)
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
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/solution/user/{user_id}/{id}")
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
    public ResponseEntity<Void> updateUser(@PathVariable("user_id") int userId, @PathVariable("id") int id){
        if(solutionService.changeUserById(id, userId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/solution/assignment/{assignment_id}/{id}")
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
    public List<SolutionResponse> getLateSolutionsByGroupId(@PathVariable("group_id") int group_id) {
        return solutionService.getLateSolutionsByGroup(group_id);
    }

    @GetMapping("/solutions/lateByGroupAndStudent/{group_id}/{student_id}")
    public List<SolutionResponse> getLateSolutionsByGroupIdAndStudentId(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        return solutionService.getLateSolutionsByUserAndGroup(student_id, group_id);
    }

    @GetMapping("/solutions/lateByAssignment/{assignment_id}")
    public List<SolutionResponse> getLateSolutionsByAssignmentId(@PathVariable("assignment_id") int assignment_id) {
        return solutionService.getLateSolutionsByAssignment(assignment_id);
    }

    @GetMapping("/solutions/lateByStudent/{student_id}")
    public List<SolutionResponse> getLateSolutionsByStudentId(@PathVariable("student_id") int student_id) {
        return solutionService.getLateSolutionsByStudent(student_id);
    }
}
