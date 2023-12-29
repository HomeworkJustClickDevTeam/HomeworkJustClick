package pl.homeworkjustclick.file;


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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "File", description = "File related calls.")
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
public class FileController {

    private final FileService fileService;

    @GetMapping("/files")
    @Operation(
            summary = "Returns list of all files in DB.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = File.class))
                            )
                    )
            }
    )
    public List<File> getAll() {
        return fileService.getAll();
    }

    @GetMapping("/file/{fileId}")
    @Operation(
            summary = "Returns file by it's id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No file with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = File.class))

                    )
            }
    )
    public ResponseEntity<File> getById(@PathVariable("fileId") int id) {
        Optional<File> file = fileService.getById(id);
        return file.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/file/withAssignment/{assignmentId}")
    @Operation(
            summary = "Creates file with assignment attached to it.",
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
                                    schema = @Schema(implementation = FileResponseDto.class))

                    )
            }
    )
    public ResponseEntity<FileResponseDto> addWithAssignment(@RequestBody File file, @PathVariable("assignmentId") int id) {
        FileResponseDto response = fileService.addWithAssignment(file, id);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/file/withSolution/{solutionId}")
    @Operation(
            summary = "Creates file with solution attached to it.",
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
                                    schema = @Schema(implementation = FileResponseDto.class))

                    )
            }
    )
    public ResponseEntity<FileResponseDto> addWithSolution(@RequestBody File file, @PathVariable("solutionId") int id) {
        FileResponseDto response = fileService.addWithSolution(file, id);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/file/{fileId}")
    @Operation(
            summary = "Deletes file with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing file with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> delete(@PathVariable("fileId") int id) {
        if (fileService.delete(id).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/files/byAssignment/{assignmentId}")
    @Operation(
            summary = "Returns list of files with given assignment.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No files with this assignment.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = File.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<File>> getFilesByAssignment(@PathVariable("assignmentId") int id) {
        List<File> response = fileService.getFilesByAssignment(id);
        if (response.isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/files/bySolution/{solutionId}")
    @Operation(
            summary = "Returns list of files with given solution.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No files with this solution.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = File.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<File>> getFilesBySolution(@PathVariable("solutionId") int id) {
        List<File> response = fileService.getFilesBySolution(id);
        if (response.isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
