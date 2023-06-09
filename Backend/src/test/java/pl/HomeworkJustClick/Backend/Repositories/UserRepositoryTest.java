package pl.HomeworkJustClick.Backend.Repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Enums.Role;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void itShouldGetUserByEmail() {
        //given
        String email = "jan_kowalski@gmail.com";
        String password = "123";
        boolean isVerified = true;
        Role role = Role.USER;
        int index = 123456;
        String firstname = "Jan";
        String lastname = "Kowalski";
        int color = 7;
        User user = new User(email, password, isVerified, role, index, firstname, lastname, color);
        userRepository.save(user);

        //when
        Optional<User> expectedUser = userRepository.findByEmail(email);

        //then
        assertTrue(expectedUser.isPresent());
        assertEquals(expectedUser.get(), user);
    }

    @Test
    void itShouldNotGetUserByEmail() {
        //given
        String email = "jan_kowalski@gmail.com";
        String password = "123";
        boolean isVerified = true;
        Role role = Role.USER;
        int index = 123456;
        String firstname = "Jan";
        String lastname = "Kowalski";
        int color = 7;
        User user = new User(email, password, isVerified, role, index, firstname, lastname, color);
        userRepository.save(user);

        //when
        Optional<User> expectedUser = userRepository.findByEmail("aaa@wp.pl");

        //then
        assertTrue(expectedUser.isEmpty());
    }
}
