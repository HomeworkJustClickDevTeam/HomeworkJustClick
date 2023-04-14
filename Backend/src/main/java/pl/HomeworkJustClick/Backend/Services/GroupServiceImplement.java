package pl.HomeworkJustClick.Backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.GroupTeacher;
import pl.HomeworkJustClick.Backend.Repositories.GroupRepository;
import pl.HomeworkJustClick.Backend.Repositories.GroupTeacherRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImplement implements GroupService {
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    GroupTeacherRepository groupTeacherRepository;

    @Override
    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group getById(int id) {
        if (groupRepository.findById(id).isPresent()) {
            return groupRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public Boolean add(Group group) {
        groupRepository.save(group);
        return true;
    }

    @Override
    public Boolean delete(int id) {
        try {
            groupRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Boolean changeNameById(int id, String name) {
        if (groupRepository.findById(id).isPresent()) {
            Group group = groupRepository.findById(id).get();
            group.setName(name);
            groupRepository.save(group);
            return true;
        } else {
            return null;
        }
    }

    @Override
    public Boolean addWithTeacher(Group group, GroupTeacher groupTeacher) {
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        return true;
    }

    @Override
    public List<Group> getGroupsByTeacher(int teacher_id) {
        return groupRepository.getGroupTeachersByTeacher(teacher_id);
    }

    @Override
    public List<Group> getGroupsByStudent(int student_id) {
        return groupRepository.getGroupStudentsByStudent(student_id);
    }
}
