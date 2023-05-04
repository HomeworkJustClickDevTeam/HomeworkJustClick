package pl.HomeworkJustClick.Backend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.GroupTeacher;
import pl.HomeworkJustClick.Backend.Responses.GroupResponse;
import pl.HomeworkJustClick.Backend.Services.GroupService;
import pl.HomeworkJustClick.Backend.Services.GroupStudentService;
import pl.HomeworkJustClick.Backend.Services.GroupTeacherService;
import pl.HomeworkJustClick.Backend.Services.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    GroupTeacherService groupTeacherService;

    @Autowired
    GroupStudentService groupStudentService;

    @Autowired
    UserService userService;

    @GetMapping("/groups")
    public List<Group> getAll() {
        return groupService.getAll();
    }

    @GetMapping("/group/{id}")
    public Group getById(@PathVariable("id") int id) {
        return groupService.getById(id);
    }

    @PostMapping("/group")
    public ResponseEntity<GroupResponse> add(@RequestBody Group group) {
        GroupResponse response = groupService.add(group);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/group/{id}")
    public ResponseEntity<Void> delete (@PathVariable("id") int id) {
        if(groupService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/group/name/{id}")
    public ResponseEntity<Void> updateName(@PathVariable("id") int id, @RequestBody String name){
        if(groupService.changeNameById(id, name)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/group/description/{id}")
    public ResponseEntity<Void> updateDescription(@PathVariable("id") int id, @RequestBody String description){
        if(groupService.changeDescriptionById(id, description)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/group/color/{id}")
    public ResponseEntity<Void> updateColor(@PathVariable("id") int id, @RequestBody int color){
        if(color >= 0 && color <20 && groupService.changeColorById(id, color)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/group/archive/{id}")
    public ResponseEntity<Void> archiveGroup(@PathVariable("id") int id, @RequestBody int color){
        if(groupService.archiveGroup(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/group/withTeacher/{id}")
    public ResponseEntity<GroupResponse> addWithTeacher(@PathVariable("id") int id, @RequestBody Group group) {
        GroupResponse response = groupService.add(group);
        GroupTeacher groupTeacher = new GroupTeacher(group, userService.getById(id), "");
        if(groupTeacherService.add(groupTeacher)) {
            return ResponseEntity.ok(response);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/group/addTeacher/{teacher_id}/{group_id}")
    public ResponseEntity<Void> addTeacherToGroup(@PathVariable("teacher_id") int teacher_id, @PathVariable("group_id") int group_id) {
        if (groupTeacherService.addTeacherToGroup(group_id, teacher_id)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/groups/byTeacher/{id}")
    public List<Group> getGroupsByUserIdWhereUserIsTeacher(@PathVariable("id") int id){
        return groupService.getGroupsByTeacher(id);
    }

    @PostMapping("/group/addStudent/{student_id}/{group_id}")
    public ResponseEntity<Void> addStudentToGroup(@PathVariable("student_id") int student_id, @PathVariable("group_id") int group_id) {
        if (groupStudentService.addStudentToGroup(group_id, student_id)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/groups/byStudent/{id}")
    public List<Group> getGroupsByUserIdWhereUserIsStudent(@PathVariable("id") int id){
        return groupService.getGroupsByStudent(id);
    }

    @GetMapping("/groups/byUser/{id}")
    public List<Group> getGroupsByUserId(@PathVariable("id") int id) {
        List<Group> groupListTeacher = groupService.getGroupsByTeacher(id);
        List<Group> groupListStudent = groupService.getGroupsByStudent(id);
        List<Group> groupList = new ArrayList<>(groupListTeacher);
        groupList.addAll(groupListStudent);
        return groupList;
    }

    @DeleteMapping("/group/deleteStudent/{student_id}/{group_id}")
    public ResponseEntity<Void> deleteStudentFromGroup(@PathVariable("student_id") int student_id, @PathVariable("group_id") int group_id) {
        if(groupStudentService.deleteStudentFromGroup(group_id, student_id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/group/deleteTeacher/{teacher_id}/{group_id}")
    public ResponseEntity<Void> deleteTeacherFromGroup(@PathVariable("teacher_id") int teacher_id, @PathVariable("group_id") int group_id) {
        if(groupTeacherService.deleteTeacherFromGroup(group_id, teacher_id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else if(groupTeacherService.countTeachersInGroup(group_id) < 2) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
