package pl.HomeworkJustClick.Backend.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/student")
    public ResponseEntity<String> testStudent() {
        return ResponseEntity.ok("Hello Student!");
    }

    @GetMapping("/teacher")
    public ResponseEntity<String> testTeacher() {
        return ResponseEntity.ok("Hello Teacher!");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> testAdmin() {
        return ResponseEntity.ok("Hello Admin!");
    }

    @GetMapping("/")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello World!");
    }
}
