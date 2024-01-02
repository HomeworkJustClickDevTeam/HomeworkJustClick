package pl.homeworkjustclick.groupteacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.homeworkjustclick.group.GroupRepository;
import pl.homeworkjustclick.group.GroupService;
import pl.homeworkjustclick.groupstudent.GroupStudentRepository;
import pl.homeworkjustclick.user.UserRepository;
import pl.homeworkjustclick.user.UserService;

@Service
@RequiredArgsConstructor
public class GroupTeacherService {
    private final GroupTeacherRepository groupTeacherRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupStudentRepository groupStudentRepository;
    private final UserService userService;
    private final GroupService groupService;

    @Transactional
    public Boolean add(GroupTeacher groupTeacher) {
        groupTeacherRepository.save(groupTeacher);
        return true;
    }

    @Transactional
    public Boolean addTeacherToGroup(int groupId, int teacherId) {
        int groupTeacherCheck = groupTeacherRepository.checkForTeacherInGroup(teacherId, groupId);
        int groupStudentCheck = groupStudentRepository.checkForStudentInGroup(teacherId, groupId);
        if (groupTeacherCheck != 0 || groupStudentCheck != 0) {
            return null;
        } else if (groupRepository.findById(groupId).isPresent() && userRepository.findById(teacherId).isPresent()) {
            GroupTeacher groupTeacher = new GroupTeacher(groupService.findById(groupId), userService.findById(teacherId), "");
            groupTeacherRepository.save(groupTeacher);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Boolean deleteTeacherFromGroup(int groupId, int teacherId) {
        if (groupTeacherRepository.countTeachersInGroup(groupId) > 1) {
            GroupTeacher groupTeacher = groupTeacherRepository.getGroupTeacherObjectByTeacherAndGroup(teacherId, groupId);
            groupTeacherRepository.deleteById(groupTeacher.getId());
            return true;
        } else {
            return false;
        }
    }

    public int countTeachersInGroup(int groupId) {
        return groupTeacherRepository.countTeachersInGroup(groupId);
    }

    public Boolean checkForTeacherInGroup(int teacherId, int groupId) {
        int groupTeacherCheck = groupTeacherRepository.checkForTeacherInGroup(teacherId, groupId);
        return groupTeacherCheck != 0;
    }

}
