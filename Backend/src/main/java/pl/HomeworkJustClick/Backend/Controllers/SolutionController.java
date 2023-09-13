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
import pl.HomeworkJustClick.Backend.assignment.AssignmentResponseDto;
import pl.HomeworkJustClick.Backend.solution.*;

import java.util.Comparator;
import java.util.List;

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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    public List<SolutionResponseDto> getAll() {
        List<SolutionResponseDto> responseList = solutionService.getAll();
        responseList.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
        return responseList;
    }

    @GetMapping("/extended/solutions")
    @Operation(
            summary = "Returns list of all solutions in DB.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    public List<SolutionResponseExtendedDto> getAllExtended() {
        List<SolutionResponseExtendedDto> responseList = solutionService.getAllExtended();
        responseList.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SolutionResponseDto.class))

                    )
            }
    )
    public ResponseEntity<SolutionResponseDto> getById(@PathVariable("solution_id") int id) {
        SolutionResponseDto solution = solutionService.getById(id);
        if (solution != null) {
            return new ResponseEntity<>(solution, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/extended/solution/{solution_id}")
    @Operation(
            summary = "Returns solution by it's id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No solution with this id in the DB.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SolutionResponseExtendedDto.class))

                    )
            }
    )
    public ResponseEntity<SolutionResponseExtendedDto> getByIdExtended(@PathVariable("solution_id") int id) {
        SolutionResponseExtendedDto solution = solutionService.getByIdExtended(id);
        if (solution != null) {
            return new ResponseEntity<>(solution, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/solution")
    @Hidden
    public ResponseEntity<SolutionResponseDto> add(@RequestBody Solution solution) {
        SolutionResponseDto response = solutionService.add(solution);
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SolutionResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "This user does not have access to the assignment or solution with this user and assignment already exists.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )

                    )
            }
    )
    public ResponseEntity<SolutionResponseDto> addWithUserAndAssignment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Fields: 'creationDatetime', 'lastModifiedDatetime', 'id' can not be changed manually but are needed in JSON. 'lastModifiedDatetime' updates by itself when the solution object changes. 'creationDatetime' is unchangeable.")
            @RequestBody Solution solution, @PathVariable("user_id") int user_id, @PathVariable("assignment_id") int assignment_id) {
        SolutionResponseDto response = solutionService.addWithUserAndAssignment(solution, user_id, assignment_id);
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    @GetMapping("/solutions/byGroup/{group_id}")
    public ResponseEntity<List<SolutionResponseDto>> getSolutionsByGroupId(@PathVariable("group_id") int id) {
        if(solutionService.getSolutionsByGroupId(id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponseDto> responseList = solutionService.getSolutionsByGroupId(id);
            responseList.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    @GetMapping("/solutions/byAssignment/{assignment_id}")
    public ResponseEntity<List<SolutionResponseDto>> getSolutionsByAssignmentId(@PathVariable("assignment_id") int id) {
        if(solutionService.getSolutionsByAssignmentId(id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponseDto> responseList = solutionService.getSolutionsByAssignmentId(id);
            responseList.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    @GetMapping("/solutions/lateByGroup/{group_id}")
    public ResponseEntity<List<SolutionResponseDto>> getLateSolutionsByGroupId(@PathVariable("group_id") int group_id) {
        if(solutionService.getLateSolutionsByGroup(group_id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponseDto> responseList = solutionService.getLateSolutionsByGroup(group_id);
            responseList.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    @GetMapping("/solutions/lateByGroupAndStudent/{group_id}/{student_id}")
    public ResponseEntity<List<SolutionResponseDto>> getLateSolutionsByGroupIdAndStudentId(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        if(solutionService.getLateSolutionsByUserAndGroup(student_id, group_id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponseDto> responseList = solutionService.getLateSolutionsByUserAndGroup(student_id, group_id);
            responseList.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    @GetMapping("/solutions/lateByAssignment/{assignment_id}")
    public ResponseEntity<List<SolutionResponseDto>> getLateSolutionsByAssignmentId(@PathVariable("assignment_id") int assignment_id) {
        if(solutionService.getLateSolutionsByAssignment(assignment_id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponseDto> responseList = solutionService.getLateSolutionsByAssignment(assignment_id);
            responseList.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    @GetMapping("/solutions/lateByStudent/{student_id}")
    public ResponseEntity<List<SolutionResponseDto>> getLateSolutionsByStudentId(@PathVariable("student_id") int student_id) {
        if(solutionService.getLateSolutionsByStudent(student_id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponseDto> responseList = solutionService.getLateSolutionsByStudent(student_id);
            responseList.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseDto>> getUncheckedSolutionsByGroup(@PathVariable("group_id") int group_id) {
        List<SolutionResponseDto> response = solutionService.getUncheckedSolutionsByGroup(group_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseDto>> getUncheckedSolutionsByStudent(@PathVariable("student_id") int student_id) {
        List<SolutionResponseDto> response = solutionService.getUncheckedSolutionsByStudent(student_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseDto>> getUncheckedSolutionsByStudentAndGroup(@PathVariable("student_id") int student_id, @PathVariable("group_id") int group_id) {
        List<SolutionResponseDto> response = solutionService.getUncheckedSolutionsByStudentAndGroup(student_id, group_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseDto>> getUncheckedSolutionsByAssignment(@PathVariable("assignment_id") int assignment_id) {
        List<SolutionResponseDto> response = solutionService.getUncheckedSolutionsByAssignment(assignment_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseDto>> getUncheckedSolutionsByTeacher(@PathVariable("teacher_id") int teacher_id) {
        List<SolutionResponseDto> response = solutionService.getUncheckedSolutionsByTeacher(teacher_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseDto>> getCheckedSolutionsByGroup(@PathVariable("group_id") int group_id) {
        List<SolutionResponseDto> response = solutionService.getCheckedSolutionsByGroup(group_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseDto>> getCheckedSolutionsByStudent(@PathVariable("student_id") int student_id) {
        List<SolutionResponseDto> response = solutionService.getCheckedSolutionsByStudent(student_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseDto>> getCheckedSolutionsByStudentAndGroup(@PathVariable("student_id") int student_id, @PathVariable("group_id") int group_id) {
        List<SolutionResponseDto> response = solutionService.getCheckedSolutionsByStudentAndGroup(student_id, group_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseDto>> getCheckedSolutionsByAssignment(@PathVariable("assignment_id") int assignment_id) {
        List<SolutionResponseDto> response = solutionService.getCheckedSolutionsByAssignment(assignment_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseDto>> getCheckedSolutionsByTeacher(@PathVariable("teacher_id") int teacher_id) {
        List<SolutionResponseDto> response = solutionService.getCheckedSolutionsByTeacher(teacher_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    @GetMapping("/extended/solutions/byGroup/{group_id}")
    public ResponseEntity<List<SolutionResponseExtendedDto>> getSolutionsByGroupIdExtended(@PathVariable("group_id") int id) {
        if(solutionService.getSolutionsByGroupIdExtended(id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponseExtendedDto> responseList = solutionService.getSolutionsByGroupIdExtended(id);
            responseList.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    @GetMapping("/extended/solutions/byAssignment/{assignment_id}")
    public ResponseEntity<List<SolutionResponseExtendedDto>> getSolutionsByAssignmentIdExtended(@PathVariable("assignment_id") int id) {
        if(solutionService.getSolutionsByAssignmentIdExtended(id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponseExtendedDto> responseList = solutionService.getSolutionsByAssignmentIdExtended(id);
            responseList.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    @GetMapping("/extended/solutions/lateByGroup/{group_id}")
    public ResponseEntity<List<SolutionResponseExtendedDto>> getLateSolutionsByGroupIdExtended(@PathVariable("group_id") int group_id) {
        if(solutionService.getLateSolutionsByGroupExtended(group_id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponseExtendedDto> responseList = solutionService.getLateSolutionsByGroupExtended(group_id);
            responseList.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    @GetMapping("/extended/solutions/lateByGroupAndStudent/{group_id}/{student_id}")
    public ResponseEntity<List<SolutionResponseExtendedDto>> getLateSolutionsByGroupIdAndStudentIdExtended(@PathVariable("group_id") int group_id, @PathVariable("student_id") int student_id) {
        if(solutionService.getLateSolutionsByUserAndGroupExtended(student_id, group_id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponseExtendedDto> responseList = solutionService.getLateSolutionsByUserAndGroupExtended(student_id, group_id);
            responseList.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    @GetMapping("/extended/solutions/lateByAssignment/{assignment_id}")
    public ResponseEntity<List<SolutionResponseExtendedDto>> getLateSolutionsByAssignmentIdExtended(@PathVariable("assignment_id") int assignment_id) {
        if(solutionService.getLateSolutionsByAssignmentExtended(assignment_id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponseExtendedDto> responseList = solutionService.getLateSolutionsByAssignmentExtended(assignment_id);
            responseList.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
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
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    @GetMapping("/extended/solutions/lateByStudent/{student_id}")
    public ResponseEntity<List<SolutionResponseExtendedDto>> getLateSolutionsByStudentIdExtended(@PathVariable("student_id") int student_id) {
        if(solutionService.getLateSolutionsByStudentExtended(student_id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            List<SolutionResponseExtendedDto> responseList = solutionService.getLateSolutionsByStudentExtended(student_id);
            responseList.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/uncheckedByGroup/{group_id}")
    @Operation(
            summary = "Returns list of all unchecked solutions in the group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any unchecked solutions in this group.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseExtendedDto>> getUncheckedSolutionsByGroupExtended(@PathVariable("group_id") int group_id) {
        List<SolutionResponseExtendedDto> response = solutionService.getUncheckedSolutionsByGroupExtended(group_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/uncheckedByStudent/{student_id}")
    @Operation(
            summary = "Returns list of all unchecked solutions by a given student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any unchecked solutions for the student.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseExtendedDto>> getUncheckedSolutionsByStudentExtended(@PathVariable("student_id") int student_id) {
        List<SolutionResponseExtendedDto> response = solutionService.getUncheckedSolutionsByStudentExtended(student_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/uncheckedByStudentAndGroup/{student_id}/{group_id}")
    @Operation(
            summary = "Returns list of all unchecked solutions by a given student in a group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any unchecked solutions for the student in the group.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseExtendedDto>> getUncheckedSolutionsByStudentAndGroupExtended(@PathVariable("student_id") int student_id, @PathVariable("group_id") int group_id) {
        List<SolutionResponseExtendedDto> response = solutionService.getUncheckedSolutionsByStudentAndGroupExtended(student_id, group_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/uncheckedByAssignment/{assignment_id}")
    @Operation(
            summary = "Returns list of all unchecked solutions for a given assignment.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any unchecked solutions for the assignment.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseExtendedDto>> getUncheckedSolutionsByAssignmentExtended(@PathVariable("assignment_id") int assignment_id) {
        List<SolutionResponseExtendedDto> response = solutionService.getUncheckedSolutionsByAssignmentExtended(assignment_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/uncheckedByTeacher/{teacher_id}")
    @Operation(
            summary = "Returns list of all unchecked solutions by a given teacher.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any unchecked solutions by this teacher.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseExtendedDto>> getUncheckedSolutionsByTeacherExtended(@PathVariable("teacher_id") int teacher_id) {
        List<SolutionResponseExtendedDto> response = solutionService.getUncheckedSolutionsByTeacherExtended(teacher_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/checkedByGroup/{group_id}")
    @Operation(
            summary = "Returns list of all checked solutions in the group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any checked solutions in this group.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseExtendedDto>> getCheckedSolutionsByGroupExtended(@PathVariable("group_id") int group_id) {
        List<SolutionResponseExtendedDto> response = solutionService.getCheckedSolutionsByGroupExtended(group_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/checkedByStudent/{student_id}")
    @Operation(
            summary = "Returns list of all checked solutions by a given student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any checked solutions for the student.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseExtendedDto>> getCheckedSolutionsByStudentExtended(@PathVariable("student_id") int student_id) {
        List<SolutionResponseExtendedDto> response = solutionService.getCheckedSolutionsByStudentExtended(student_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/checkedByStudentAndGroup/{student_id}/{group_id}")
    @Operation(
            summary = "Returns list of all checked solutions by a given student in a group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any checked solutions for the student in the group.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseExtendedDto>> getCheckedSolutionsByStudentAndGroupExtended(@PathVariable("student_id") int student_id, @PathVariable("group_id") int group_id) {
        List<SolutionResponseExtendedDto> response = solutionService.getCheckedSolutionsByStudentAndGroupExtended(student_id, group_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/checkedByAssignment/{assignment_id}")
    @Operation(
            summary = "Returns list of all checked solutions for a given assignment.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any checked solutions for the assignment.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseExtendedDto>> getCheckedSolutionsByAssignmentExtended(@PathVariable("assignment_id") int assignment_id) {
        List<SolutionResponseExtendedDto> response = solutionService.getCheckedSolutionsByAssignmentExtended(assignment_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/checkedByTeacher/{teacher_id}")
    @Operation(
            summary = "Returns list of all checked solutions by a given teacher.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find any checked solutions by this teacher.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = SolutionResponseExtendedDto.class))
                            )
                    )
            }
    )
    ResponseEntity<List<SolutionResponseExtendedDto>> getCheckedSolutionsByTeacherExtended(@PathVariable("teacher_id") int teacher_id) {
        List<SolutionResponseExtendedDto> response = solutionService.getCheckedSolutionsByTeacherExtended(teacher_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solution/checkForEvaluation/{solution_id}")
    @Operation(
            summary = "Checks if solution with given id has any evaluation attached to it. Returns true if so, false otherwise. False may also indicate that there is no solution in the DB with given id.",
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
    public ResponseEntity<Boolean> checkForEvaluationToSolution(@PathVariable("solution_id") int id) {
        return new ResponseEntity<>(solutionService.checkForEvaluationToSolution(id), HttpStatus.OK);
    }

    @GetMapping("/solution/getCheckedSolutionByUserAssignmentGroup/{user_id}/{assignment_id}/{group_id}")
    @Operation(
            summary = "Returns checked solution for a given user, assignment and group.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SolutionResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Could not find assignment/user/group.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SolutionResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find solution for given ids.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SolutionResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<SolutionResponseDto> getCheckedSolutionByUserAssignmentGroup(@PathVariable("user_id") int userId, @PathVariable("assignment_id") int assignmentId, @PathVariable("group_id") int groupId) {
        SolutionResponseDto solutionResponseDto = solutionService.getCheckedSolutionByUserAssignmentGroup(userId, groupId, assignmentId);
        if (solutionResponseDto.isForbidden()) {
            return new ResponseEntity<>(solutionResponseDto, HttpStatus.BAD_REQUEST);
        } else if (solutionResponseDto.getId() != 0) {
            return new ResponseEntity<>(solutionResponseDto, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(solutionResponseDto, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/solution/getUncheckedSolutionByUserAssignmentGroup/{user_id}/{assignment_id}/{group_id}")
    @Operation(
            summary = "Returns unchecked solution for a given user, assignment and group.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SolutionResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Could not find assignment/user/group.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SolutionResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find solution for given ids.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SolutionResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<SolutionResponseDto> getUncheckedSolutionByUserAssignmentGroup(@PathVariable("user_id") int userId, @PathVariable("assignment_id") int assignmentId, @PathVariable("group_id") int groupId) {
        SolutionResponseDto solutionResponseDto = solutionService.getUncheckedSolutionByUserAssignmentGroup(userId, groupId, assignmentId);
        if (solutionResponseDto.isForbidden()) {
            return new ResponseEntity<>(solutionResponseDto, HttpStatus.BAD_REQUEST);
        } else if (solutionResponseDto.getId() != 0) {
            return new ResponseEntity<>(solutionResponseDto, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(solutionResponseDto, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/solution/checkForFile/{solution_id}")
    @Operation(
            summary = "Checks if solution with given id has any file attached to it. Returns true if so, false otherwise. False may also indicate that there is no solution in the DB with given id.",
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
    public ResponseEntity<Boolean> checkForFileToSolution(@PathVariable("solution_id") int solution_id) {
        return new ResponseEntity<>(solutionService.checkForFileToSolution(solution_id), HttpStatus.OK);
    }

    @GetMapping("/solutions/calendarListByTeacher/{teacher_id}")
    @Operation(
            summary = "Returns list of all teacher's solutions to calendar.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No solutions for the teacher.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<SolutionResponseCalendarDto>> getSolutionsByTeacherCalendar(@PathVariable("teacher_id") int teacher_id) {
        return new ResponseEntity<>(solutionService.getSolutionsByTeacherCalender(teacher_id), HttpStatus.OK);
    }
}
