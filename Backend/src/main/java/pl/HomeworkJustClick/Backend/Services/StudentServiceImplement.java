package pl.HomeworkJustClick.Backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.Student;
import pl.HomeworkJustClick.Backend.Repositories.StudentRepository;

import java.util.List;

@Service
public class StudentServiceImplement implements StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Override
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student getById(int id) {
        if (studentRepository.findById(id).isPresent()) {
            return studentRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public Boolean add(Student student) {
        studentRepository.save(student);
        return true;
    }

    @Override
    public Boolean update(int id, Student updatedStudent) {
        if (studentRepository.findById(id).isPresent()) {
            Student student = studentRepository.getReferenceById(id);
            if (updatedStudent.getFirstname() != null) {
                student.setFirstname(updatedStudent.getFirstname());
            }
            if (updatedStudent.getLastname() != null) {
                student.setLastname(updatedStudent.getLastname());
            }
            if (updatedStudent.getIndex() != student.getIndex()) {
                student.setIndex(updatedStudent.getIndex());
            }
            try {
                studentRepository.save(student);
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
            studentRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Boolean changeIndexById(int id, int index) {
        if (studentRepository.findById(id).isPresent()) {
            Student student = studentRepository.findById(id).get();
            student.setIndex(index);
            studentRepository.save(student);
            return true;
        } else {
            return null;
        }
    }

}
