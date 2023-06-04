package pl.HomeworkJustClick.Backend.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.GroupStudent;
import pl.HomeworkJustClick.Backend.Entities.GroupTeacher;
import pl.HomeworkJustClick.Backend.Repositories.GroupRepository;
import pl.HomeworkJustClick.Backend.Repositories.GroupStudentRepository;
import pl.HomeworkJustClick.Backend.Repositories.GroupTeacherRepository;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupStudentServiceImplement implements GroupStudentService{

    private final GroupStudentRepository groupStudentRepository;

    private final GroupRepository groupRepository;

    private final GroupTeacherRepository groupTeacherRepository;

    private final UserRepository userRepository;

    @Override
    public List<GroupStudent> getAll() {
        return groupStudentRepository.findAll();
    }

    @Override
    public Optional<GroupStudent> getById(int id) {
        return groupStudentRepository.findById(id);
    }

    @Override
    public Boolean add(GroupStudent groupStudent) {
        groupStudentRepository.save(groupStudent);
        return true;
    }

    @Override
    public Boolean delete(int id) {
        if(groupStudentRepository.existsById(id)) {
            groupStudentRepository.deleteById(id);
            return true;
        } else {
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
            return false;
        }
    }

    @Override
    public Boolean addStudentToGroup (int group_id, int student_id) {
        int groupTeacherCheck = groupTeacherRepository.getGroupTeacherByTeacherAndGroup(student_id, group_id);
        int groupStudentCheck = groupStudentRepository.getGroupStudentByStudentAndGroup(student_id, group_id);
        if (groupTeacherCheck == 0 && groupStudentCheck == 0 && groupRepository.findById(group_id).isPresent() && userRepository.findById(student_id).isPresent()) {
            GroupStudent groupStudent = new GroupStudent(groupRepository.findById(group_id).get(), userRepository.findById(student_id).get(), "");
            groupStudentRepository.save(groupStudent);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean deleteStudentFromGroup (int group_id, int student_id) {
        if (groupStudentRepository.getGroupStudentByStudentAndGroup(student_id, group_id) != 0){
            GroupStudent groupStudent = groupStudentRepository.getGroupStudentObjectByStudentAndGroup(student_id, group_id);
            groupStudentRepository.deleteById(groupStudent.getId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean checkForStudentInGroup(int student_id, int group_id) {
        int groupStudentCheck = groupStudentRepository.getGroupStudentByStudentAndGroup(student_id, group_id);
        return groupStudentCheck != 0;
    }
}
