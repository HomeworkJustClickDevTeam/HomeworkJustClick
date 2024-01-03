package pl.homeworkjustclick.notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;
import pl.homeworkjustclick.user.UserRepository;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@Sql(scripts = {
        "classpath:db/cleanup_all.sql",
        "classpath:db/init_user.sql",
        "classpath:db/init_group.sql",
        "classpath:db/init_group_student.sql",
        "classpath:db/init_group_teacher.sql",
        "classpath:db/init_assignment.sql",
        "classpath:db/init_solution.sql",
        "classpath:db/init_evaluation.sql",
        "classpath:db/init_notification.sql"
})
class NotificationControllerTest extends BaseTestEntity {
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    UserRepository userRepository;

    private static Stream<Arguments> prepareUsers() {
        return Stream.of(
                Arguments.of("jan_kowalski@gmail.com", 3),
                Arguments.of("anna_malinowska@gmail.com", 2),
                Arguments.of("zofia_danielska@gmail.com", 1)
        );
    }

    @ParameterizedTest
    @MethodSource("prepareUsers")
    void shouldGetNotificationsByUserId(String userMail, int expected) throws Exception {
        var userId = userRepository.findByEmail(userMail).get().getId();
        mockMvc.perform(get("/api/notification/byUser/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expected))
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("prepareUsers")
    void shouldGetNotificationsCounterByUserId(String userMail, int expected) throws Exception {
        var userId = userRepository.findByEmail(userMail).get().getId();
        mockMvc.perform(get("/api/notification/countByUser/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(expected))
                .andReturn();
    }

    @Test
    void shouldChangeReadInNotifications() throws Exception {
        var path = new StringBuilder("/api/notification?notificationsIds=");
        var notifications = notificationRepository.findAll();
        for (var notification : notifications) {
            path.append(notification.getId());
            path.append(",");
        }
        path.deleteCharAt(path.length() - 1);
        mockMvc.perform(post(String.valueOf(path)))
                .andExpect(status().isOk())
                .andReturn();
        notificationRepository.findAll().forEach(notification -> assertTrue(notification.getRead()));
    }

    @Test
    void shouldThrowExceptionWhenChangingReadInNotExistingNotification() throws Exception {
        var path = new StringBuilder("/api/notification?notificationsIds=");
        var notifications = notificationRepository.findAll();
        for (var notification : notifications) {
            path.append(notification.getId());
            path.append(",");
        }
        path.append("9999");
        mockMvc.perform(post(String.valueOf(path)))
                .andExpect(status().isNotFound())
                .andReturn();
        notificationRepository.findAll().forEach(notification -> assertFalse(notification.getRead()));
    }

    @Test
    void shouldDeleteNotification() throws Exception {
        var notificationId = notificationRepository.findAll().get(0).getId();
        mockMvc.perform(delete("/api/notification/" + notificationId))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(notificationRepository.findById(notificationId).isEmpty());
    }

    @Test
    void shouldNotDeleteNotExistingNotification() throws Exception {
        var expectedSize = notificationRepository.findAll().size();
        mockMvc.perform(delete("/api/notification/9999"))
                .andExpect(status().isNotFound())
                .andReturn();
        assertEquals(expectedSize, notificationRepository.findAll().size());
    }
}
