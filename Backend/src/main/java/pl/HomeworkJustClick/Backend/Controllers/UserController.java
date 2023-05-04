package pl.HomeworkJustClick.Backend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Services.GroupTeacherService;
import pl.HomeworkJustClick.Backend.Services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    GroupTeacherService groupTeacherService;

    @GetMapping("/users")
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/user/{id}")
    public User getById(@PathVariable("id") int id) {
        return userService.getById(id);
    }

    @PostMapping("/user")
    public ResponseEntity<Void> add(@RequestBody User user) {
        if(userService.add(user)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") int id, @RequestBody User updatedUser){
        if(userService.update(id, updatedUser)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> delete (@PathVariable("id") int id) {
        if(userService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/user/index/{id}")
    public ResponseEntity<Void> updateIndex(@PathVariable("id") int id, @RequestBody int index){
        if(userService.changeIndexById(id, index)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/user/color/{id}")
    public ResponseEntity<Void> updateColor(@PathVariable("id") int id, @RequestBody int color){
        if(color >= 0 && color <20 && userService.changeColorById(id, color)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/user/getTeachersByGroup/{group_id}")
    public List<User> getTeachersByGroup(@PathVariable("group_id") int group_id) {
        return userService.getTeachersByGroup(group_id);
    }

    @GetMapping("/user/getStudentsByGroup/{group_id}")
    public List<User> getStudentsByGroup(@PathVariable("group_id") int group_id) {
        return userService.getStudentsByGroup(group_id);
    }


}
