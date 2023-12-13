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
    public Boolean addTeacherToGroup (int group_id, int teacher_id) {
        int groupTeacherCheck = groupTeacherRepository.checkForTeacherInGroup(teacher_id, group_id);
        int groupStudentCheck = groupStudentRepository.checkForStudentInGroup(teacher_id, group_id);
        if (groupTeacherCheck != 0 || groupStudentCheck != 0){
            return null;
        }
        else if (groupRepository.findById(group_id).isPresent() && userRepository.findById(teacher_id).isPresent()) {
            GroupTeacher groupTeacher = new GroupTeacher(groupRepository.findById(group_id).get(), userRepository.findById(teacher_id).get(), "");
            groupTeacherRepository.save(groupTeacher);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Boolean deleteTeacherFromGroup (int group_id, int teacher_id) {
        if(groupTeacherRepository.countTeachersInGroup(group_id) > 1) {
            GroupTeacher groupTeacher = groupTeacherRepository.getGroupTeacherObjectByTeacherAndGroup(teacher_id, group_id);
            groupTeacherRepository.deleteById(groupTeacher.getId());
            return true;
        } else {
            return false;
        }
    }

    public int countTeachersInGroup (int group_id) {
        return groupTeacherRepository.countTeachersInGroup(group_id);
    }

    public Boolean checkForTeacherInGroup(int teacher_id, int group_id) {
        int groupTeacherCheck = groupTeacherRepository.checkForTeacherInGroup(teacher_id, group_id);
        return groupTeacherCheck != 0;
    }

}
