package pl.homeworkjustclick.groupstudent;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.group.GroupRepository;
import pl.homeworkjustclick.group.GroupService;
import pl.homeworkjustclick.groupteacher.GroupTeacherRepository;
import pl.homeworkjustclick.user.UserRepository;
import pl.homeworkjustclick.user.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupStudentService {
    private final GroupStudentRepository groupStudentRepository;
    private final GroupRepository groupRepository;
    private final GroupTeacherRepository groupTeacherRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final GroupService groupService;

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
    public Boolean addStudentToGroup(int groupId, int studentId) {
        int groupTeacherCheck = groupTeacherRepository.checkForTeacherInGroup(studentId, groupId);
        int groupStudentCheck = groupStudentRepository.checkForStudentInGroup(studentId, groupId);
        if (groupTeacherCheck != 0 || groupStudentCheck != 0) {
            return null;
        } else if (groupRepository.findById(groupId).isPresent() && userRepository.findById(studentId).isPresent()) {
            GroupStudent groupStudent = new GroupStudent(groupService.findById(groupId), userService.findById(studentId), "");
            groupStudentRepository.save(groupStudent);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Boolean deleteStudentFromGroup(int groupId, int studentId) {
        if (groupStudentRepository.checkForStudentInGroup(studentId, groupId) != 0) {
            GroupStudent groupStudent = groupStudentRepository.getGroupStudentObjectByStudentAndGroup(studentId, groupId);
            groupStudentRepository.deleteById(groupStudent.getId());
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkForStudentInGroup(int studentId, int groupId) {
        int groupStudentCheck = groupStudentRepository.checkForStudentInGroup(studentId, groupId);
        return groupStudentCheck != 0;
    }
}
