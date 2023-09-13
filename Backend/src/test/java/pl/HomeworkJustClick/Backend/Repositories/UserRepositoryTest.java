package pl.HomeworkJustClick.Backend.Repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.HomeworkJustClick.Backend.group.Group;
import pl.HomeworkJustClick.Backend.group.GroupRepository;
import pl.HomeworkJustClick.Backend.groupstudent.GroupStudent;
import pl.HomeworkJustClick.Backend.groupstudent.GroupStudentRepository;
import pl.HomeworkJustClick.Backend.groupteacher.GroupTeacher;
import pl.HomeworkJustClick.Backend.groupteacher.GroupTeacherRepository;
import pl.HomeworkJustClick.Backend.infrastructure.enums.Role;
import pl.HomeworkJustClick.Backend.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupTeacherRepository groupTeacherRepository;

    @Autowired
    private GroupStudentRepository groupStudentRepository;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

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
        Optional<User> actualUser = userRepository.findByEmail(email);

        //then
        assertTrue(actualUser.isPresent());
        assertEquals(actualUser.get(), user);
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
        Optional<User> actualUser = userRepository.findByEmail("aaa@wp.pl");

        //then
        assertTrue(actualUser.isEmpty());
    }

    @Test
    void itShouldGetTeachersByGroupId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        List<User> expectedList = new ArrayList<>();
        expectedList.add(user);

        //when
        List<User> actualList = userRepository.getTeachersByGroupId(group.getId());

        //then
        assertEquals(expectedList, actualList);
    }

    @Test
    void itShouldNotGetTeachersByGroupId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        List<User> expectedList = new ArrayList<>();
        expectedList.add(user);

        //when
        List<User> actualList = userRepository.getTeachersByGroupId(group.getId()+1);

        //then
        assertNotEquals(expectedList, actualList);
    }

    @Test
    void itShouldGetStudentsByGroupId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupStudent groupStudent = new GroupStudent(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupStudentRepository.save(groupStudent);
        List<User> expectedList = new ArrayList<>();
        expectedList.add(user);

        //when
        List<User> actualList = userRepository.getStudentsByGroupId(group.getId());

        //then
        assertEquals(expectedList, actualList);
    }

    @Test
    void itShouldNotGetStudentsByGroupId() {
        //given
        User user = new User( "jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupStudent groupStudent = new GroupStudent(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupStudentRepository.save(groupStudent);
        List<User> expectedList = new ArrayList<>();
        expectedList.add(user);

        //when
        List<User> actualList = userRepository.getStudentsByGroupId(group.getId()+1);

        //then
        assertNotEquals(expectedList, actualList);
    }
}
