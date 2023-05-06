package pl.HomeworkJustClick.Backend.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.File;
import pl.HomeworkJustClick.Backend.Responses.FileResponse;
import pl.HomeworkJustClick.Backend.Services.FileService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    FileService fileService;

    @GetMapping("/files")
    public List<File> getAll() {
        return fileService.getAll();
    }

    @GetMapping("/file/{id}")
    public File getById(@PathVariable("id") int id) {
        return fileService.getById(id);
    }

    @PostMapping("/file/withAssignment/{id}")
    public ResponseEntity<FileResponse> addWithAssignment(@RequestBody File file, @PathVariable("id") int id) {
        FileResponse response = fileService.addWithAssignment(file, id);
        if(response==null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/file/withSolution/{id}")
    public ResponseEntity<FileResponse> addWithSolution(@RequestBody File file, @PathVariable("id") int id) {
        FileResponse response = fileService.addWithSolution(file, id);
        if(response==null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/file/{id}")
    public ResponseEntity<Void> delete (@PathVariable("id") int id) {
        if(fileService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
