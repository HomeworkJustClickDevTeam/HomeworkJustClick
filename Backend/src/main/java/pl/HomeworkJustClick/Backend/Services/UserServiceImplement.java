package pl.HomeworkJustClick.Backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;

import java.util.List;

@Service
public class UserServiceImplement implements UserService{
    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(int id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        } else {
            return null;
        }
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
            if (updatedUser.getFirstname() != null) {
                user.setFirstname(updatedUser.getFirstname());
            }
            if (updatedUser.getLastname() != null) {
                user.setLastname(updatedUser.getLastname());
            }
            if (updatedUser.getIndex() != user.getIndex()) {
                user.setIndex(updatedUser.getIndex());
            }

            try {
                userRepository.save(user);
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
            userRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
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
            return null;
        }
    }
}
