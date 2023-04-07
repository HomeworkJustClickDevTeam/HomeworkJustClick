package pl.HomeworkJustClick.Backend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Student;
import pl.HomeworkJustClick.Backend.Services.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping("/students")
    public List<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping("student/{id}")
    public Student getById(@PathVariable("id") int id) {
        return studentService.getById(id);
    }

    @PostMapping("/student")
    public ResponseEntity<Void> add(@RequestBody Student student) {
        if(studentService.add(student)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/student/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") int id, @RequestBody Student updatedStudent){
        if(studentService.update(id, updatedStudent)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/student/{id}")
    public ResponseEntity<Void> delete (@PathVariable("id") int id) {
        if(studentService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/student/index/{id}")
    public ResponseEntity<Void> updateIndex(@PathVariable("id") int id, @RequestBody int index){
        if(studentService.changeIndexById(id, index)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
