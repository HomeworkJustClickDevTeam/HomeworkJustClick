package pl.homeworkjustclick.groupstudent;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.group.GroupRepository;
import pl.homeworkjustclick.groupteacher.GroupTeacherRepository;
import pl.homeworkjustclick.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupStudentService {

    private final GroupStudentRepository groupStudentRepository;

    private final GroupRepository groupRepository;

    private final GroupTeacherRepository groupTeacherRepository;

    private final UserRepository userRepository;

    public List<GroupStudent> getAll() {
        return groupStudentRepository.findAll();
    }

    public Optional<GroupStudent> getById(int id) {
        return groupStudentRepository.findById(id);
    }

    @Transactional
    public Boolean add(GroupStudent groupStudent) {
        groupStudentRepository.save(groupStudent);
        return true;
    }

    @Transactional
    public Boolean delete(int id) {
        if(groupStudentRepository.existsById(id)) {
            groupStudentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Boolean addStudentToGroup (int group_id, int student_id) {
        int groupTeacherCheck = groupTeacherRepository.checkForTeacherInGroup(student_id, group_id);
        int groupStudentCheck = groupStudentRepository.checkForStudentInGroup(student_id, group_id);
        if(groupTeacherCheck != 0 || groupStudentCheck != 0){
            return null;
        }
        else if (groupRepository.findById(group_id).isPresent() && userRepository.findById(student_id).isPresent()) {
            GroupStudent groupStudent = new GroupStudent(groupRepository.findById(group_id).get(), userRepository.findById(student_id).get(), "");
            groupStudentRepository.save(groupStudent);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Boolean deleteStudentFromGroup (int group_id, int student_id) {
        if (groupStudentRepository.checkForStudentInGroup(student_id, group_id) != 0){
            GroupStudent groupStudent = groupStudentRepository.getGroupStudentObjectByStudentAndGroup(student_id, group_id);
            groupStudentRepository.deleteById(groupStudent.getId());
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkForStudentInGroup(int student_id, int group_id) {
        int groupStudentCheck = groupStudentRepository.checkForStudentInGroup(student_id, group_id);
        return groupStudentCheck != 0;
    }
}
