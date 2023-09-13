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
import pl.HomeworkJustClick.Backend.groupteacher.GroupTeacherRepository;
import pl.HomeworkJustClick.Backend.infrastructure.enums.Role;
import pl.HomeworkJustClick.Backend.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class GroupStudentRepositoryTest {

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
    void itShouldReturn1ForStudentInGroup() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupStudent groupStudent = new GroupStudent(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupStudentRepository.save(groupStudent);

        //when
        int result = groupStudentRepository.checkForStudentInGroup(user.getId(), group.getId());

        //then
        assertEquals(1, result);
    }

    @Test
    void itShouldReturn0ForStudentInGroup() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupStudent groupStudent = new GroupStudent(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupStudentRepository.save(groupStudent);

        //when
        int result = groupStudentRepository.checkForStudentInGroup(user.getId()+1, group.getId());

        //then
        assertEquals(0, result);
    }

    @Test
    void itShouldReturn0ForStudentInGroup_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupStudent groupStudent = new GroupStudent(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupStudentRepository.save(groupStudent);

        //when
        int result = groupStudentRepository.checkForStudentInGroup(user.getId()+1, group.getId()+1);

        //then
        assertEquals(0, result);
    }

    @Test
    void itShouldReturn0ForStudentInGroup_3() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupStudent groupStudent = new GroupStudent(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupStudentRepository.save(groupStudent);

        //when
        int result = groupStudentRepository.checkForStudentInGroup(user.getId(), group.getId()+1);

        //then
        assertEquals(0, result);
    }

    @Test
    void itShouldReturnGroupStudentForGroupAndStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupStudent groupStudent = new GroupStudent(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupStudentRepository.save(groupStudent);

        //when
        GroupStudent actual = groupStudentRepository.getGroupStudentObjectByStudentAndGroup(user.getId(), group.getId());

        //then
        assertEquals(groupStudent, actual);
    }

    @Test
    void itShouldNotReturnGroupStudentForGroupAndStudent() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupStudent groupStudent = new GroupStudent(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupStudentRepository.save(groupStudent);

        //when
        GroupStudent actual = groupStudentRepository.getGroupStudentObjectByStudentAndGroup(user.getId()+1, group.getId());

        //then
        assertNull(actual);
    }

    @Test
    void itShouldNotReturnGroupStudentForGroupAndStudent_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupStudent groupStudent = new GroupStudent(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupStudentRepository.save(groupStudent);

        //when
        GroupStudent actual = groupStudentRepository.getGroupStudentObjectByStudentAndGroup(user.getId()+1, group.getId()+1);

        //then
        assertNull(actual);
    }

    @Test
    void itShouldNotReturnGroupStudentForGroupAndStudent_3() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7);
        Group group = new Group("Grupa", "opis",5,false);
        GroupStudent groupStudent = new GroupStudent(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupStudentRepository.save(groupStudent);

        //when
        GroupStudent actual = groupStudentRepository.getGroupStudentObjectByStudentAndGroup(user.getId(), group.getId()+1);

        //then
        assertNull(actual);
    }

}
