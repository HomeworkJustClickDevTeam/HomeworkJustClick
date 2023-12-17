package pl.homeworkjustclick.solution;

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
import pl.homeworkjustclick.assignment.AssignmentResponseDto;

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

    @GetMapping("/solution/{solutionId}")
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
    public ResponseEntity<SolutionResponseDto> getById(@PathVariable("solutionId") int id) {
        SolutionResponseDto solution = solutionService.getById(id);
        if (solution != null) {
            return new ResponseEntity<>(solution, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/extended/solution/{solutionId}")
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
    public ResponseEntity<SolutionResponseExtendedDto> getByIdExtended(@PathVariable("solutionId") int id) {
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

    @DeleteMapping("/solution/{solutionId}")
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

    public void delete(@PathVariable("solutionId") int id) {
        solutionService.delete(id);
    }

    @PostMapping("/solution/withUserAndAssignment/{userId}/{assignmentId}")
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
            @RequestBody Solution solution, @PathVariable("userId") int userId, @PathVariable("assignmentId") int assignmentId) {
        SolutionResponseDto response = solutionService.addWithUserAndAssignment(solution, userId, assignmentId);
        if (response.getId() != 0) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response.isForbidden()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/solution/user/{userId}/{solutionId}")
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
    public ResponseEntity<Void> updateUser(@PathVariable("userId") int userId, @PathVariable("solutionId") int id) {
        if (solutionService.changeUserById(id, userId).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/solution/assignment/{assignmentId}/{solutionId}")
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
    public ResponseEntity<Void> updateAssignment(@PathVariable("assignmentId") int assignmentId, @PathVariable("solutionId") int id) {
        if (solutionService.changeAssignmentById(id, assignmentId).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
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
    @GetMapping("/solutions/byGroup/{groupId}")
    public ResponseEntity<List<SolutionResponseDto>> getSolutionsByGroupId(@PathVariable("groupId") int id) {
        if (solutionService.getSolutionsByGroupId(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
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
    @GetMapping("/solutions/byAssignment/{assignmentId}")
    public ResponseEntity<List<SolutionResponseDto>> getSolutionsByAssignmentId(@PathVariable("assignmentId") int id) {
        if (solutionService.getSolutionsByAssignmentId(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
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
    @GetMapping("/solutions/lateByGroup/{groupId}")
    public ResponseEntity<List<SolutionResponseDto>> getLateSolutionsByGroupId(@PathVariable("groupId") int groupId) {
        if (solutionService.getLateSolutionsByGroup(groupId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<SolutionResponseDto> responseList = solutionService.getLateSolutionsByGroup(groupId);
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
    @GetMapping("/solutions/lateByGroupAndStudent/{groupId}/{studentId}")
    public ResponseEntity<List<SolutionResponseDto>> getLateSolutionsByGroupIdAndStudentId(@PathVariable("groupId") int groupId, @PathVariable("studentId") int studentId) {
        if (solutionService.getLateSolutionsByUserAndGroup(studentId, groupId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<SolutionResponseDto> responseList = solutionService.getLateSolutionsByUserAndGroup(studentId, groupId);
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
    @GetMapping("/solutions/lateByAssignment/{assignmentId}")
    public ResponseEntity<List<SolutionResponseDto>> getLateSolutionsByAssignmentId(@PathVariable("assignmentId") int assignmentId) {
        if (solutionService.getLateSolutionsByAssignment(assignmentId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<SolutionResponseDto> responseList = solutionService.getLateSolutionsByAssignment(assignmentId);
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
    @GetMapping("/solutions/lateByStudent/{studentId}")
    public ResponseEntity<List<SolutionResponseDto>> getLateSolutionsByStudentId(@PathVariable("studentId") int studentId) {
        if (solutionService.getLateSolutionsByStudent(studentId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<SolutionResponseDto> responseList = solutionService.getLateSolutionsByStudent(studentId);
            responseList.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/uncheckedByGroup/{groupId}")
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
    ResponseEntity<List<SolutionResponseDto>> getUncheckedSolutionsByGroup(@PathVariable("groupId") int groupId) {
        List<SolutionResponseDto> response = solutionService.getUncheckedSolutionsByGroup(groupId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/uncheckedByStudent/{studentId}")
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
    ResponseEntity<List<SolutionResponseDto>> getUncheckedSolutionsByStudent(@PathVariable("studentId") int studentId) {
        List<SolutionResponseDto> response = solutionService.getUncheckedSolutionsByStudent(studentId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/uncheckedByStudentAndGroup/{studentId}/{groupId}")
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
    ResponseEntity<List<SolutionResponseDto>> getUncheckedSolutionsByStudentAndGroup(@PathVariable("studentId") int studentId, @PathVariable("groupId") int groupId) {
        List<SolutionResponseDto> response = solutionService.getUncheckedSolutionsByStudentAndGroup(studentId, groupId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/uncheckedByAssignment/{assignmentId}")
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
    ResponseEntity<List<SolutionResponseDto>> getUncheckedSolutionsByAssignment(@PathVariable("assignmentId") int assignmentId) {
        List<SolutionResponseDto> response = solutionService.getUncheckedSolutionsByAssignment(assignmentId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/uncheckedByTeacher/{teacherId}")
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
    ResponseEntity<List<SolutionResponseDto>> getUncheckedSolutionsByTeacher(@PathVariable("teacherId") int teacherId) {
        List<SolutionResponseDto> response = solutionService.getUncheckedSolutionsByTeacher(teacherId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/checkedByGroup/{groupId}")
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
    ResponseEntity<List<SolutionResponseDto>> getCheckedSolutionsByGroup(@PathVariable("groupId") int groupId) {
        List<SolutionResponseDto> response = solutionService.getCheckedSolutionsByGroup(groupId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/checkedByStudent/{studentId}")
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
    ResponseEntity<List<SolutionResponseDto>> getCheckedSolutionsByStudent(@PathVariable("studentId") int studentId) {
        List<SolutionResponseDto> response = solutionService.getCheckedSolutionsByStudent(studentId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/checkedByStudentAndGroup/{studentId}/{groupId}")
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
    ResponseEntity<List<SolutionResponseDto>> getCheckedSolutionsByStudentAndGroup(@PathVariable("studentId") int studentId, @PathVariable("groupId") int groupId) {
        List<SolutionResponseDto> response = solutionService.getCheckedSolutionsByStudentAndGroup(studentId, groupId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/checkedByAssignment/{assignmentId}")
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
    ResponseEntity<List<SolutionResponseDto>> getCheckedSolutionsByAssignment(@PathVariable("assignmentId") int assignmentId) {
        List<SolutionResponseDto> response = solutionService.getCheckedSolutionsByAssignment(assignmentId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solutions/checkedByTeacher/{teacherId}")
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
    ResponseEntity<List<SolutionResponseDto>> getCheckedSolutionsByTeacher(@PathVariable("teacherId") int teacherId) {
        List<SolutionResponseDto> response = solutionService.getCheckedSolutionsByTeacher(teacherId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
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
    @GetMapping("/extended/solutions/byGroup/{groupId}")
    public ResponseEntity<List<SolutionResponseExtendedDto>> getSolutionsByGroupIdExtended(@PathVariable("groupId") int id) {
        if (solutionService.getSolutionsByGroupIdExtended(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
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
    @GetMapping("/extended/solutions/byAssignment/{assignmentId}")
    public ResponseEntity<List<SolutionResponseExtendedDto>> getSolutionsByAssignmentIdExtended(@PathVariable("assignmentId") int id) {
        if (solutionService.getSolutionsByAssignmentIdExtended(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
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
    @GetMapping("/extended/solutions/lateByGroup/{groupId}")
    public ResponseEntity<List<SolutionResponseExtendedDto>> getLateSolutionsByGroupIdExtended(@PathVariable("groupId") int groupId) {
        if (solutionService.getLateSolutionsByGroupExtended(groupId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<SolutionResponseExtendedDto> responseList = solutionService.getLateSolutionsByGroupExtended(groupId);
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
    @GetMapping("/extended/solutions/lateByGroupAndStudent/{groupId}/{studentId}")
    public ResponseEntity<List<SolutionResponseExtendedDto>> getLateSolutionsByGroupIdAndStudentIdExtended(@PathVariable("groupId") int groupId, @PathVariable("studentId") int studentId) {
        if (solutionService.getLateSolutionsByUserAndGroupExtended(studentId, groupId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<SolutionResponseExtendedDto> responseList = solutionService.getLateSolutionsByUserAndGroupExtended(studentId, groupId);
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
    @GetMapping("/extended/solutions/lateByAssignment/{assignmentId}")
    public ResponseEntity<List<SolutionResponseExtendedDto>> getLateSolutionsByAssignmentIdExtended(@PathVariable("assignmentId") int assignmentId) {
        if (solutionService.getLateSolutionsByAssignmentExtended(assignmentId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<SolutionResponseExtendedDto> responseList = solutionService.getLateSolutionsByAssignmentExtended(assignmentId);
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
    @GetMapping("/extended/solutions/lateByStudent/{studentId}")
    public ResponseEntity<List<SolutionResponseExtendedDto>> getLateSolutionsByStudentIdExtended(@PathVariable("studentId") int studentId) {
        if (solutionService.getLateSolutionsByStudentExtended(studentId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<SolutionResponseExtendedDto> responseList = solutionService.getLateSolutionsByStudentExtended(studentId);
            responseList.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/uncheckedByGroup/{groupId}")
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
    ResponseEntity<List<SolutionResponseExtendedDto>> getUncheckedSolutionsByGroupExtended(@PathVariable("groupId") int groupId) {
        List<SolutionResponseExtendedDto> response = solutionService.getUncheckedSolutionsByGroupExtended(groupId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/uncheckedByStudent/{studentId}")
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
    ResponseEntity<List<SolutionResponseExtendedDto>> getUncheckedSolutionsByStudentExtended(@PathVariable("studentId") int studentId) {
        List<SolutionResponseExtendedDto> response = solutionService.getUncheckedSolutionsByStudentExtended(studentId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/uncheckedByStudentAndGroup/{studentId}/{groupId}")
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
    ResponseEntity<List<SolutionResponseExtendedDto>> getUncheckedSolutionsByStudentAndGroupExtended(@PathVariable("studentId") int studentId, @PathVariable("groupId") int groupId) {
        List<SolutionResponseExtendedDto> response = solutionService.getUncheckedSolutionsByStudentAndGroupExtended(studentId, groupId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/uncheckedByAssignment/{assignmentId}")
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
    ResponseEntity<List<SolutionResponseExtendedDto>> getUncheckedSolutionsByAssignmentExtended(@PathVariable("assignmentId") int assignmentId) {
        List<SolutionResponseExtendedDto> response = solutionService.getUncheckedSolutionsByAssignmentExtended(assignmentId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/uncheckedByTeacher/{teacherId}")
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
    ResponseEntity<List<SolutionResponseExtendedDto>> getUncheckedSolutionsByTeacherExtended(@PathVariable("teacherId") int teacherId) {
        List<SolutionResponseExtendedDto> response = solutionService.getUncheckedSolutionsByTeacherExtended(teacherId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/checkedByGroup/{groupId}")
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
    ResponseEntity<List<SolutionResponseExtendedDto>> getCheckedSolutionsByGroupExtended(@PathVariable("groupId") int groupId) {
        List<SolutionResponseExtendedDto> response = solutionService.getCheckedSolutionsByGroupExtended(groupId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/checkedByStudent/{studentId}")
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
    ResponseEntity<List<SolutionResponseExtendedDto>> getCheckedSolutionsByStudentExtended(@PathVariable("studentId") int studentId) {
        List<SolutionResponseExtendedDto> response = solutionService.getCheckedSolutionsByStudentExtended(studentId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/checkedByStudentAndGroup/{studentId}/{groupId}")
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
    ResponseEntity<List<SolutionResponseExtendedDto>> getCheckedSolutionsByStudentAndGroupExtended(@PathVariable("studentId") int studentId, @PathVariable("groupId") int groupId) {
        List<SolutionResponseExtendedDto> response = solutionService.getCheckedSolutionsByStudentAndGroupExtended(studentId, groupId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/checkedByAssignment/{assignmentId}")
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
    ResponseEntity<List<SolutionResponseExtendedDto>> getCheckedSolutionsByAssignmentExtended(@PathVariable("assignmentId") int assignmentId) {
        List<SolutionResponseExtendedDto> response = solutionService.getCheckedSolutionsByAssignmentExtended(assignmentId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/solutions/checkedByTeacher/{teacherId}")
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
    ResponseEntity<List<SolutionResponseExtendedDto>> getCheckedSolutionsByTeacherExtended(@PathVariable("teacherId") int teacherId) {
        List<SolutionResponseExtendedDto> response = solutionService.getCheckedSolutionsByTeacherExtended(teacherId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response.sort(Comparator.comparing(SolutionResponseExtendedDto::getCreationDateTime));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/solution/checkForEvaluation/{solutionId}")
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
    public ResponseEntity<Boolean> checkForEvaluationToSolution(@PathVariable("solutionId") int id) {
        return new ResponseEntity<>(solutionService.checkForEvaluationToSolution(id), HttpStatus.OK);
    }

    @GetMapping("/solution/getCheckedSolutionByUserAssignmentGroup/{userId}/{assignmentId}/{groupId}")
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
    public ResponseEntity<SolutionResponseDto> getCheckedSolutionByUserAssignmentGroup(@PathVariable("userId") int userId, @PathVariable("assignmentId") int assignmentId, @PathVariable("groupId") int groupId) {
        SolutionResponseDto solutionResponseDto = solutionService.getCheckedSolutionByUserAssignmentGroup(userId, groupId, assignmentId);
        if (solutionResponseDto.isForbidden()) {
            return new ResponseEntity<>(solutionResponseDto, HttpStatus.BAD_REQUEST);
        } else if (solutionResponseDto.getId() != 0) {
            return new ResponseEntity<>(solutionResponseDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(solutionResponseDto, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/solution/getUncheckedSolutionByUserAssignmentGroup/{userId}/{assignmentId}/{groupId}")
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
    public ResponseEntity<SolutionResponseDto> getUncheckedSolutionByUserAssignmentGroup(@PathVariable("userId") int userId, @PathVariable("assignmentId") int assignmentId, @PathVariable("groupId") int groupId) {
        SolutionResponseDto solutionResponseDto = solutionService.getUncheckedSolutionByUserAssignmentGroup(userId, groupId, assignmentId);
        if (solutionResponseDto.isForbidden()) {
            return new ResponseEntity<>(solutionResponseDto, HttpStatus.BAD_REQUEST);
        } else if (solutionResponseDto.getId() != 0) {
            return new ResponseEntity<>(solutionResponseDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(solutionResponseDto, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/solution/checkForFile/{solutionId}")
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
    public ResponseEntity<Boolean> checkForFileToSolution(@PathVariable("solutionId") int solutionId) {
        return new ResponseEntity<>(solutionService.checkForFileToSolution(solutionId), HttpStatus.OK);
    }

    @GetMapping("/solutions/calendarListByTeacher/{teacherId}")
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
    public ResponseEntity<List<SolutionResponseCalendarDto>> getSolutionsByTeacherCalendar(@PathVariable("teacherId") int teacherId) {
        return new ResponseEntity<>(solutionService.getSolutionsByTeacherCalender(teacherId), HttpStatus.OK);
    }
}
