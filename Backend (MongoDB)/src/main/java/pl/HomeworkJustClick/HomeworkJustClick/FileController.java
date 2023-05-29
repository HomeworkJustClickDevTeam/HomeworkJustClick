package pl.HomeworkJustClick.HomeworkJustClick;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
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

    @PostMapping("/twoFiles")
    public ResponseEntity<List<FileResponse>> addTwoFiles(@RequestParam("file1")MultipartFile file1, @RequestParam("file2")MultipartFile file2) throws IOException {
        List<MultipartFile> fileList = List.of(file1, file2);
        List<FileResponse> response = fileService.addFileList(fileList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/threeFiles")
    public ResponseEntity<List<FileResponse>> addThreeFiles(@RequestParam("file1")MultipartFile file1, @RequestParam("file2")MultipartFile file2, @RequestParam("file3")MultipartFile file3) throws IOException {
        List<MultipartFile> fileList = List.of(file1, file2, file3);
        List<FileResponse> response = fileService.addFileList(fileList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/fourFiles")
    public ResponseEntity<List<FileResponse>> addFourFiles(@RequestParam("file1")MultipartFile file1, @RequestParam("file2")MultipartFile file2, @RequestParam("file3")MultipartFile file3, @RequestParam("file4")MultipartFile file4) throws IOException {
        List<MultipartFile> fileList = List.of(file1, file2, file3, file4);
        List<FileResponse> response = fileService.addFileList(fileList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/fiveFiles")
    public ResponseEntity<List<FileResponse>> addFiveFiles(@RequestParam("file1")MultipartFile file1, @RequestParam("file2")MultipartFile file2, @RequestParam("file3")MultipartFile file3, @RequestParam("file4")MultipartFile file4, @RequestParam("file5")MultipartFile file5) throws IOException {
        List<MultipartFile> fileList = List.of(file1, file2, file3, file4, file5);
        List<FileResponse> response = fileService.addFileList(fileList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/fileList")
    public ResponseEntity<List<FileResponse>> addList(@RequestBody List<MultipartFile> fileList) throws IOException {
        List<FileResponse> responseList = fileService.addFileList(fileList);
        return ResponseEntity.ok(responseList);
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
