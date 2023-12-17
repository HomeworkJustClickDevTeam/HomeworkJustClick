package pl.homeworkjustclick.group;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {


    private final GroupRepository groupRepository;

    public Group findById(Integer id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group with id = " + id + " not found"));
    }

    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    public Optional<Group> getById(int id) {
        return groupRepository.findById(id);
    }

    @Transactional
    public GroupResponseDto add(Group group) {
        var savedGroup = groupRepository.save(group);
        int id = savedGroup.getId();
        savedGroup.setColor(id % 20);
        groupRepository.save(savedGroup);
        return GroupResponseDto.builder().id(savedGroup.getId()).name(savedGroup.getName()).description(savedGroup.getDescription()).color(savedGroup.getColor()).build();
    }

    @Transactional
    public Boolean delete(int id) {
        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Boolean changeNameById(int id, String name) {
        if (groupRepository.findById(id).isPresent()) {
            Group group = groupRepository.findById(id).get();
            group.setName(name);
            groupRepository.save(group);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Boolean changeDescriptionById(int id, String description) {
        if (groupRepository.findById(id).isPresent()) {
            Group group = groupRepository.findById(id).get();
            group.setDescription(description);
            groupRepository.save(group);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Boolean changeColorById(int id, int color) {
        if (groupRepository.findById(id).isPresent()) {
            Group group = groupRepository.findById(id).get();
            group.setColor(color);
            groupRepository.save(group);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public int archiveGroup(int id) {
        if (groupRepository.findById(id).isPresent()) {
            Group group = groupRepository.findById(id).get();
            if(group.isArchived()) {
                return 1;
            } else {
                group.setArchived(true);
            }
            groupRepository.save(group);
            return 0;
        } else {
            return 2;
        }
    }

    @Transactional
    public int unarchiveGroup(int id) {
        if (groupRepository.findById(id).isPresent()) {
            Group group = groupRepository.findById(id).get();
            if(!group.isArchived()) {
                return 1;
            } else {
                group.setArchived(false);
            }
            groupRepository.save(group);
            return 0;
        } else {
            return 2;
        }
    }

    public List<Group> getGroupsByTeacher(int teacherId) {
        return groupRepository.getGroupsByTeacherId(teacherId);
    }

    public List<Group> getGroupsByStudent(int studentId) {
        return groupRepository.getGroupsByStudentId(studentId);
    }

}
