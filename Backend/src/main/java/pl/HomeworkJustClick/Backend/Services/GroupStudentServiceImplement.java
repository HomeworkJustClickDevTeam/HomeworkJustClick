package pl.HomeworkJustClick.Backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.GroupStudent;
import pl.HomeworkJustClick.Backend.Entities.GroupTeacher;
import pl.HomeworkJustClick.Backend.Repositories.GroupRepository;
import pl.HomeworkJustClick.Backend.Repositories.GroupStudentRepository;

import java.util.List;

@Service
public class GroupStudentServiceImplement implements GroupStudentService{

    @Autowired
    GroupStudentRepository groupStudentRepository;

    @Autowired
    GroupRepository groupRepository;

    @Override
    public List<GroupStudent> getAll() {
        return groupStudentRepository.findAll();
    }

    @Override
    public GroupStudent getById(int id) {
        if (groupStudentRepository.findById(id).isPresent()) {
            return groupStudentRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public Boolean add(GroupStudent groupStudent) {
        groupStudentRepository.save(groupStudent);
        return true;
    }

    @Override
    public Boolean delete(int id) {
        try {
            groupStudentRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Boolean changeDescriptionById (int id, String description) {
        if (groupStudentRepository.findById(id).isPresent()) {
            GroupStudent groupStudent = groupStudentRepository.findById(id).get();
            groupStudent.setDescription(description);
            groupStudentRepository.save(groupStudent);
            return true;
        } else {
            return null;
        }
    }
}
