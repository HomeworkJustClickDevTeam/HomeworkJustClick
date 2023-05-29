package pl.HomeworkJustClick.Backend.Controllers;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.File;
import pl.HomeworkJustClick.Backend.Responses.FileResponse;
import pl.HomeworkJustClick.Backend.Services.FileService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "File")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/files")
    public List<File> getAll() {
        return fileService.getAll();
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<File> getById(@PathVariable("id") int id) {
        Optional<File> file = fileService.getById(id);
        return file.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/file/withAssignment/{id}")
    public ResponseEntity<FileResponse> addWithAssignment(@RequestBody File file, @PathVariable("id") int id) {
        FileResponse response = fileService.addWithAssignment(file, id);
        if(response==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/file/listWithAssignment/{id}")
    public ResponseEntity<Void> addListWithAssignment(@RequestBody List<File> fileList, @PathVariable("id") int id) {
        boolean response = fileService.addListWithAssignment(fileList, id);
        if(response){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/file/withSolution/{id}")
    public ResponseEntity<FileResponse> addWithSolution(@RequestBody File file, @PathVariable("id") int id) {
        FileResponse response = fileService.addWithSolution(file, id);
        if(response==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/file/listWithSolution/{id}")
    public ResponseEntity<Void> addListWithSolution(@RequestBody List<File> fileList, @PathVariable("id") int id) {
        boolean response = fileService.addListWithSolution(fileList, id);
        if(response){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/file/{id}")
    public ResponseEntity<Void> delete (@PathVariable("id") int id) {
        if(fileService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/files/byAssignment/{id}")
    public List<File> getFilesByAssignment(@PathVariable("id") int id) {
        return fileService.getFilesByAssignment(id);
    }

    @GetMapping("/files/bySolution/{id}")
    public List<File> getFilesBySolution(@PathVariable("id") int id) {
        return fileService.getFilesBySolution(id);
    }
}
