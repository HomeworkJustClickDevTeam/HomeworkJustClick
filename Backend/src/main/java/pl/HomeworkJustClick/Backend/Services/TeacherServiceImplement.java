package pl.HomeworkJustClick.Backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Teacher;
import pl.HomeworkJustClick.Backend.Repositories.TeacherRepository;

import java.util.List;

@Service
public class TeacherServiceImplement implements TeacherService{

    @Autowired
    TeacherRepository teacherRepository;

    @Override
    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher getById(int id) {
        if (teacherRepository.findById(id).isPresent()) {
            return teacherRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public Boolean add(Teacher teacher) {
        teacherRepository.save(teacher);
        return true;
    }

    @Override
    public Boolean update(int id, Teacher updatedTeacher) {
        if (teacherRepository.findById(id).isPresent()) {
            Teacher teacher = teacherRepository.getReferenceById(id);
            if (updatedTeacher.getFirstname() != null) {
                teacher.setFirstname(updatedTeacher.getFirstname());
            }
            if(updatedTeacher.getLastname() != null) {
                teacher.setLastname(updatedTeacher.getLastname());
            }
            if(updatedTeacher.getTitle() != null) {
                teacher.setTitle(updatedTeacher.getTitle());
            }
            try {
                teacherRepository.save(teacher);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Boolean delete(int id) {
        try {
            teacherRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Boolean changeTitleById(int id, String title) {
        if (teacherRepository.findById(id).isPresent()) {
            Teacher teacher = teacherRepository.findById(id).get();
            teacher.setTitle(title);
            teacherRepository.save(teacher);
            return true;
        } else {
            return false;
        }
    }

}
