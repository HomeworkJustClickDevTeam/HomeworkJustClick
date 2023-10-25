package pl.HomeworkJustClick.HomeworkJustClick;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "File", description = "Files related calls.")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/file")
    @Operation(
            summary = "Adds file and returns it's id, filename and format.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "File added",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FileResponseDto.class))

                    )
            }
    )
    public ResponseEntity<FileResponseDto> add(@RequestParam("file") MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        String format = name.split("\\.")[1];
        FileResponseDto response = fileService.addFile(name, format, file);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/twoFiles")
    @Operation(
            summary = "Adds two files and returns its' ids, filenames and formats.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Files added",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FileResponseDto.class))

                    )
            }
    )
    public ResponseEntity<List<FileResponseDto>> addTwoFiles(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2) throws IOException {
        List<MultipartFile> fileList = List.of(file1, file2);
        List<FileResponseDto> response = fileService.addFileList(fileList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/threeFiles")
    @Operation(
            summary = "Adds three files and returns its' ids, filenames and formats.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Files added",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FileResponseDto.class))

                    )
            }
    )
    public ResponseEntity<List<FileResponseDto>> addThreeFiles(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3) throws IOException {
        List<MultipartFile> fileList = List.of(file1, file2, file3);
        List<FileResponseDto> response = fileService.addFileList(fileList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/fourFiles")
    @Operation(
            summary = "Adds four files and returns its' ids, filenames and formats.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Files added",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FileResponseDto.class))

                    )
            }
    )
    public ResponseEntity<List<FileResponseDto>> addFourFiles(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3, @RequestParam("file4") MultipartFile file4) throws IOException {
        List<MultipartFile> fileList = List.of(file1, file2, file3, file4);
        List<FileResponseDto> response = fileService.addFileList(fileList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/fiveFiles")
    @Operation(
            summary = "Adds five files and returns its' ids, filenames and formats.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Files added",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FileResponseDto.class))

                    )
            }
    )
    public ResponseEntity<List<FileResponseDto>> addFiveFiles(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3, @RequestParam("file4") MultipartFile file4, @RequestParam("file5") MultipartFile file5) throws IOException {
        List<MultipartFile> fileList = List.of(file1, file2, file3, file4, file5);
        List<FileResponseDto> response = fileService.addFileList(fileList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/fileList")
    @Operation(
            summary = "Adds list of files and returns its' ids, filenames and formats.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Files added",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FileResponseDto.class))

                    )
            },
            deprecated = true
    )
    public ResponseEntity<List<FileResponseDto>> addList(@RequestBody List<MultipartFile> fileList) throws IOException {
        List<FileResponseDto> responseList = fileService.addFileList(fileList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/file/{id}")
    @ResponseBody
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
    public ResponseEntity<File> getFile(@PathVariable("id") String id) {
        Optional<File> file = fileService.getFile(id);
        return file.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/file/{id}")
    @Operation(
            summary = "Deletes file with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "File deleted.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing file with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        if(fileService.deleteFile(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}