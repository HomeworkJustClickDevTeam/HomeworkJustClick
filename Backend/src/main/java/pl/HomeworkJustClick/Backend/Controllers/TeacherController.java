package pl.HomeworkJustClick.Backend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Teacher;
import pl.HomeworkJustClick.Backend.Services.TeacherService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @GetMapping("/teachers")
    public List<Teacher> getAll() {
        return teacherService.getAll();
    }

    @GetMapping("teacher/{id}")
    public Teacher getById(@PathVariable("id") int id) {
        return teacherService.getById(id);
    }

    @PostMapping("/teacher")
    public ResponseEntity<Void> add(@RequestBody Teacher teacher) {
        if(teacherService.add(teacher)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/teacher/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") int id, @RequestBody Teacher updatedTeacher){
        if(teacherService.update(id, updatedTeacher)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/teacher/{id}")
    public ResponseEntity<Void> delete (@PathVariable("id") int id) {
        if(teacherService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/teacher/title/{id}")
    public ResponseEntity<Void> updateTitle(@PathVariable("id") int id, @RequestBody String title){
        if(teacherService.changeTitleById(id, title)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
