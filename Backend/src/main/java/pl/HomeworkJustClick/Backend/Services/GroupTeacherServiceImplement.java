package pl.HomeworkJustClick.Backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.GroupTeacher;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Repositories.GroupRepository;
import pl.HomeworkJustClick.Backend.Repositories.GroupStudentRepository;
import pl.HomeworkJustClick.Backend.Repositories.GroupTeacherRepository;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupTeacherServiceImplement implements GroupTeacherService{

    @Autowired
    GroupTeacherRepository groupTeacherRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupStudentRepository groupStudentRepository;

    @Override
    public List<GroupTeacher> getAll() {
        return groupTeacherRepository.findAll();
    }

    @Override
    public Optional<GroupTeacher> getById(int id) {
        return groupTeacherRepository.findById(id);
    }

    @Override
    public Boolean add(GroupTeacher groupTeacher) {
        groupTeacherRepository.save(groupTeacher);
        return true;
    }

    @Override
    public Boolean delete(int id) {
        if(groupTeacherRepository.existsById(id)) {
            groupTeacherRepository.deleteById(id);
            return true;
        } else  {
            return false;
        }
    }

    @Override
    public Boolean changeDescriptionById (int id, String description) {
        if (groupTeacherRepository.findById(id).isPresent()) {
            GroupTeacher groupTeacher = groupTeacherRepository.findById(id).get();
            groupTeacher.setDescription(description);
            groupTeacherRepository.save(groupTeacher);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean addTeacherToGroup (int group_id, int teacher_id) {
        int groupTeacherCheck = groupTeacherRepository.getGroupTeacherByTeacherAndGroup(teacher_id, group_id);
        int groupStudentCheck = groupStudentRepository.getGroupStudentByStudentAndGroup(teacher_id, group_id);
        if (groupTeacherCheck == 0 && groupStudentCheck == 0 && groupRepository.findById(group_id).isPresent() && userRepository.findById(teacher_id).isPresent()) {
            GroupTeacher groupTeacher = new GroupTeacher(groupRepository.findById(group_id).get(), userRepository.findById(teacher_id).get(), "");
            groupTeacherRepository.save(groupTeacher);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean deleteTeacherFromGroup (int group_id, int teacher_id) {
        if(groupTeacherRepository.countTeachersInGroup(group_id) > 1) {
            GroupTeacher groupTeacher = groupTeacherRepository.getGroupTeacherObjectByTeacherAndGroup(teacher_id, group_id);
            groupTeacherRepository.deleteById(groupTeacher.getId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int countTeachersInGroup (int group_id) {
        return groupTeacherRepository.countTeachersInGroup(group_id);
    }

    @Override
    public Boolean checkForTeacherInGroup(int teacher_id, int group_id) {
        int groupTeacherCheck = groupTeacherRepository.getGroupTeacherByTeacherAndGroup(teacher_id, group_id);
        return groupTeacherCheck != 0;
    }

}
