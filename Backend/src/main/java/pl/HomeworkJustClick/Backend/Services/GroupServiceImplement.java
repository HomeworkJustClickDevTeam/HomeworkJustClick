package pl.HomeworkJustClick.Backend.Services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.GroupTeacher;
import pl.HomeworkJustClick.Backend.Repositories.GroupRepository;
import pl.HomeworkJustClick.Backend.Repositories.GroupTeacherRepository;
import pl.HomeworkJustClick.Backend.Responses.GroupResponse;

import java.util.List;

@Service
public class GroupServiceImplement implements GroupService {

    EntityManager entityManager;

    public GroupServiceImplement(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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
    @Transactional
    public GroupResponse add(Group group) {
        entityManager.persist(group);
        int id = group.getId();
        group.setColor(id%20);
        entityManager.persist(group);
        return GroupResponse.builder().id(group.getId()).name(group.getName()).description(group.getDescription()).color(group.getColor()).build();
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
    public Boolean changeDescriptionById(int id, String description) {
        if (groupRepository.findById(id).isPresent()) {
            Group group = groupRepository.findById(id).get();
            group.setDescription(description);
            groupRepository.save(group);
            return true;
        } else {
            return null;
        }
    }

    @Override
    public Boolean changeColorById(int id, int color) {
        if (groupRepository.findById(id).isPresent()) {
            Group group = groupRepository.findById(id).get();
            group.setColor(color);
            groupRepository.save(group);
            return true;
        } else {
            return null;
        }
    }

    @Override
    public Boolean archiveGroup(int id) {
        if (groupRepository.findById(id).isPresent()) {
            Group group = groupRepository.findById(id).get();
            if(group.isArchived()) {
                return false;
            } else {
                group.setArchived(true);
            }
            groupRepository.save(group);
            return true;
        } else {
            return null;
        }
    }

    @Override
    public Boolean addWithTeacher(Group group, GroupTeacher groupTeacher) {
        group.setArchived(false);
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
