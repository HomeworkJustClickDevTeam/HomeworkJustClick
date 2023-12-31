package pl.homeworkjustclick.group;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.homeworkjustclick.groupstudent.GroupStudent;
import pl.homeworkjustclick.groupstudent.GroupStudentRepository;
import pl.homeworkjustclick.groupteacher.GroupTeacher;
import pl.homeworkjustclick.groupteacher.GroupTeacherRepository;
import pl.homeworkjustclick.infrastructure.enums.Role;
import pl.homeworkjustclick.user.User;
import pl.homeworkjustclick.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
class GroupRepositoryTest {

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
    void itShouldGetTeachersByGroupId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        List<Group> expectedList = new ArrayList<>();
        expectedList.add(group);

        //when
        List<Group> actualList = groupRepository.getGroupsByTeacherId(user.getId());

        //then
        assertEquals(expectedList, actualList);
    }

    @Test
    void itShouldNotGetTeachersByGroupId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupTeacher groupTeacher = new GroupTeacher(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupTeacherRepository.save(groupTeacher);
        List<Group> expectedList = new ArrayList<>();
        expectedList.add(group);

        //when
        List<Group> actualList = groupRepository.getGroupsByTeacherId(user.getId()+1);

        //then
        assertNotEquals(expectedList, actualList);
    }

    @Test
    void itShouldGetStudentsByGroupId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupStudent groupStudent = new GroupStudent(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupStudentRepository.save(groupStudent);
        List<Group> expectedList = new ArrayList<>();
        expectedList.add(group);

        //when
        List<Group> actualList = groupRepository.getGroupsByStudentId(user.getId());

        //then
        assertEquals(expectedList, actualList);
    }

    @Test
    void itShouldNotGetStudentsByGroupId() {
        //given
        User user = new User("jan_kowalski@gmail.com", "123", true, Role.USER, 123456, "Jan", "Kowalski", 7, "x");
        Group group = new Group("Grupa", "opis",5,false);
        GroupStudent groupStudent = new GroupStudent(group, user, "");
        userRepository.save(user);
        groupRepository.save(group);
        groupStudentRepository.save(groupStudent);
        List<Group> expectedList = new ArrayList<>();
        expectedList.add(group);

        //when
        List<Group> actualList = groupRepository.getGroupsByStudentId(user.getId()+1);

        //then
        assertNotEquals(expectedList, actualList);
    }

}
