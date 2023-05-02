package pl.HomeworkJustClick.HomeworkJustClick;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/file")
    public ResponseEntity<FileResponse> add(@RequestParam("name") String name, @RequestParam("file")MultipartFile file) throws IOException {
        String format = name.split("\\.")[1];
        FileResponse response = fileService.addFile(name, format, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/file/{id}")
    @ResponseBody
    public ResponseEntity<File> getFile(@PathVariable String id) {
        File file = fileService.getFile(id);
        return ResponseEntity.ok().body(file);
    }
}
