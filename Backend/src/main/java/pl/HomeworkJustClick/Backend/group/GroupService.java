package pl.HomeworkJustClick.Backend.group;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final EntityManager entityManager;

    private final GroupRepository groupRepository;

    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    public Optional<Group> getById(int id) {
        return groupRepository.findById(id);
    }

    @Transactional
    public GroupResponseDto add(Group group) {
        entityManager.persist(group);
        int id = group.getId();
        group.setColor(id%20);
        entityManager.persist(group);
        return GroupResponseDto.builder().id(group.getId()).name(group.getName()).description(group.getDescription()).color(group.getColor()).build();
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

    public List<Group> getGroupsByTeacher(int teacher_id) {
        return groupRepository.getGroupsByTeacherId(teacher_id);
    }

    public List<Group> getGroupsByStudent(int student_id) {
        return groupRepository.getGroupsByStudentId(student_id);
    }

}
