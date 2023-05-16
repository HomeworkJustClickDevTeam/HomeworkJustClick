package pl.HomeworkJustClick.HomeworkJustClick;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/file")
    public ResponseEntity<FileResponse> add(@RequestParam("file")MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        String format = name.split("\\.")[1];
        FileResponse response = fileService.addFile(name, format, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/file/{id}")
    @ResponseBody
    public ResponseEntity<File> getFile(@PathVariable("id") String id) {
        Optional<File> file = fileService.getFile(id);
        return file.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/file/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        if(fileService.deleteFile(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
