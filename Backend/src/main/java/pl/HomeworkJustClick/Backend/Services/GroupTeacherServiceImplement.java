package pl.HomeworkJustClick.Backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.GroupTeacher;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Repositories.GroupRepository;
import pl.HomeworkJustClick.Backend.Repositories.GroupTeacherRepository;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupTeacherServiceImplement implements GroupTeacherService{

    @Autowired
    GroupTeacherRepository groupTeacherRepository;

    @Autowired
    GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<GroupTeacher> getAll() {
        return groupTeacherRepository.findAll();
    }

    @Override
    public GroupTeacher getById(int id) {
        if (groupTeacherRepository.findById(id).isPresent()) {
            return groupTeacherRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public Boolean add(GroupTeacher groupTeacher) {
        groupTeacherRepository.save(groupTeacher);
        return true;
    }

    @Override
    public Boolean delete(int id) {
        try {
            groupTeacherRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
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
            return null;
        }
    }

    @Override
    public Boolean addTeacherToGroup (int group_id, int teacher_id) {
        int groupTeacherCheck = groupTeacherRepository.getGroupTeacherByTeacherAndGroup(teacher_id, group_id);
        if (groupTeacherCheck == 0 && groupRepository.findById(group_id).isPresent() && userRepository.findById(teacher_id).isPresent()) {
            GroupTeacher groupTeacher = new GroupTeacher(groupRepository.findById(group_id).get(), userRepository.findById(teacher_id).get(), "");
            groupTeacherRepository.save(groupTeacher);
            return true;
        } else {
            return false;
        }
    }

}
