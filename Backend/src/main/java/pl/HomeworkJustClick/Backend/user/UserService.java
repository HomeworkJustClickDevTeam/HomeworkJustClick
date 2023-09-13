package pl.HomeworkJustClick.Backend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(int id) {
        return userRepository.findById(id);
    }

    public Boolean add(User user) {
        userRepository.save(user);
        return true;
    }

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

    public Boolean delete(int id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else  {
            return false;
        }
    }

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

    public List<User> getTeachersByGroup(int group_id) {
        return userRepository.getTeachersByGroupId(group_id);
    }

    public List<User> getStudentsByGroup(int group_id) {
        return userRepository.getStudentsByGroupId(group_id);
    }
}
