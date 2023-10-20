package pl.HomeworkJustClick.Backend.groupteacher;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.HomeworkJustClick.Backend.group.Group;
import pl.HomeworkJustClick.Backend.group.GroupRepository;
import pl.HomeworkJustClick.Backend.groupstudent.GroupStudentRepository;
import pl.HomeworkJustClick.Backend.infrastructure.enums.Role;
import pl.HomeworkJustClick.Backend.user.User;
import pl.HomeworkJustClick.Backend.user.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class GroupTeacherRepositoryTest {

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
    void itShouldReturn1ForTeacherInGroup() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);

        //when
        int result = groupTeacherRepository.checkForTeacherInGroup(user.getId(), group.getId());

        //then
        assertEquals(1, result);
    }

    @Test
    void itShouldReturn0ForTeacherInGroup() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);

        //when
        int result = groupTeacherRepository.checkForTeacherInGroup(user.getId()+1, group.getId());

        //then
        assertEquals(0, result);
    }

    @Test
    void itShouldReturn0ForTeacherInGroup_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);

        //when
        int result = groupTeacherRepository.checkForTeacherInGroup(user.getId(), group.getId()+1);

        //then
        assertEquals(0, result);
    }

    @Test
    void itShouldReturn0ForTeacherInGroup_3() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);

        //when
        int result = groupTeacherRepository.checkForTeacherInGroup(user.getId()+1, group.getId()+1);

        //then
        assertEquals(0, result);
    }

    @Test
    void itShouldReturnGroupTeacherForGroupAndTeacher() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);

        //when
        GroupTeacher actual = groupTeacherRepository.getGroupTeacherObjectByTeacherAndGroup(user.getId(), group.getId());

        //then
        assertEquals(groupTeacher, actual);
    }

    @Test
    void itShouldNotReturnGroupTeacherForGroupAndTeacher() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);

        //when
        GroupTeacher actual = groupTeacherRepository.getGroupTeacherObjectByTeacherAndGroup(user.getId()+1, group.getId());

        //then
        assertNull(actual);
    }

    @Test
    void itShouldNotReturnGroupTeacherForGroupAndTeacher_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);

        //when
        GroupTeacher actual = groupTeacherRepository.getGroupTeacherObjectByTeacherAndGroup(user.getId()+1, group.getId()+1);

        //then
        assertNull(actual);
    }

    @Test
    void itShouldNotReturnGroupTeacherForGroupAndTeacher_3() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);

        //when
        GroupTeacher actual = groupTeacherRepository.getGroupTeacherObjectByTeacherAndGroup(user.getId(), group.getId()+1);

        //then
        assertNull(actual);
    }

    @Test
    void itShouldCountTeachersInGroup() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);

        //when
        int result = groupTeacherRepository.countTeachersInGroup(group.getId());

        //then
        assertEquals(1, result);
    }

    @Test
    void itShouldCountTeachersInGroup_2() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
//        groupTeacherRepository.save(groupTeacher);

        //when
        int result = groupTeacherRepository.countTeachersInGroup(group.getId());

        //then
        assertEquals(0, result);
    }

    @Test
    void itShouldCountTeachersInGroup_3() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        User user2 = new User("jan_kowalski2@gmail.com", "123", true, Role.USER, 123456, "Jan2", "Kowalski2", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        GroupTeacher groupTeacher2 = new GroupTeacher(group, user2, "");
        userRepository.save(user);
        userRepository.save(user2);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        groupTeacherRepository.save(groupTeacher2);

        //when
        int result = groupTeacherRepository.countTeachersInGroup(group.getId());

        //then
        assertEquals(2, result);
    }
}
