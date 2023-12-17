package pl.homeworkjustclick.groupteacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.homeworkjustclick.group.GroupRepository;
import pl.homeworkjustclick.groupstudent.GroupStudentRepository;
import pl.homeworkjustclick.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupTeacherService {

    private final GroupTeacherRepository groupTeacherRepository;

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    private final GroupStudentRepository groupStudentRepository;

    public List<GroupTeacher> getAll() {
        return groupTeacherRepository.findAll();
    }

    public Optional<GroupTeacher> getById(int id) {
        return groupTeacherRepository.findById(id);
    }

    @Transactional
    public Boolean add(GroupTeacher groupTeacher) {
        groupTeacherRepository.save(groupTeacher);
        return true;
    }

    @Transactional
    public Boolean delete(int id) {
        if(groupTeacherRepository.existsById(id)) {
            groupTeacherRepository.deleteById(id);
            return true;
        } else  {
            return false;
        }
    }

    @Transactional
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

    @Transactional
    public Boolean addTeacherToGroup(int groupId, int teacherId) {
        int groupTeacherCheck = groupTeacherRepository.checkForTeacherInGroup(teacherId, groupId);
        int groupStudentCheck = groupStudentRepository.checkForStudentInGroup(teacherId, groupId);
        if (groupTeacherCheck != 0 || groupStudentCheck != 0) {
            return null;
        } else if (groupRepository.findById(groupId).isPresent() && userRepository.findById(teacherId).isPresent()) {
            GroupTeacher groupTeacher = new GroupTeacher(groupRepository.findById(groupId).get(), userRepository.findById(teacherId).get(), "");
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
