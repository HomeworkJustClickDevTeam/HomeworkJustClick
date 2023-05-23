package pl.HomeworkJustClick.Backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.GroupTeacher;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Repositories.GroupTeacherRepository;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplement implements UserService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupTeacherRepository groupTeacherRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Boolean add(User user) {
        userRepository.save(user);
        return true;
    }

    @Override
    public Boolean update(int id, User updatedUser) {
        if (userRepository.findById(id).isPresent()) {
            User user = userRepository.getReferenceById(id);
            if (!updatedUser.getFirstname().isEmpty() && !updatedUser.getFirstname().equals(user.getFirstname())) {
                user.setFirstname(updatedUser.getFirstname());
            }
            if (!updatedUser.getLastname().isEmpty() && !updatedUser.getLastname().equals(user.getLastname())) {
                user.setLastname(updatedUser.getLastname());
            }
            if (updatedUser.getIndex() != user.getIndex()) {
                user.setIndex(updatedUser.getIndex());
            }
            if (updatedUser.getColor() != user.getColor()) {
                user.setColor(updatedUser.getColor());
            }
            try {
                userRepository.save(user);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else {
            return null;
        }
    }

    @Override
    public Boolean delete(int id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else  {
            return false;
        }
    }

    @Override
    public Boolean changeIndexById(int id, int index) {
        if (userRepository.findById(id).isPresent()) {
            User user = userRepository.findById(id).get();
            user.setIndex(index);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean changeColorById(int id, int color) {
        if (userRepository.findById(id).isPresent()) {
            User user = userRepository.findById(id).get();
            user.setColor(color);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<User> getTeachersByGroup(int group_id) {
        return userRepository.getGroupTeachersByGroup(group_id);
    }

    @Override
    public List<User> getStudentsByGroup(int group_id) {
        return userRepository.getGroupStudentsByGroup(group_id);
    }
}
