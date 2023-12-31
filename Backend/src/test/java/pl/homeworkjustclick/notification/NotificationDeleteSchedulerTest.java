package pl.homeworkjustclick.notification;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
class NotificationDeleteSchedulerTest extends BaseTestEntity {
    @Autowired
    NotificationDeleteScheduler notificationDeleteScheduler;
    @Autowired
    NotificationRepository notificationRepository;

    @Test
    void shouldNotDeleteAnyNotifications() {
        var expectedSize = notificationRepository.findAll().size();
        notificationDeleteScheduler.deleteReadNotifications();
        assertEquals(expectedSize, notificationRepository.findAll().size());
    }

    @Test
    void shouldDeleteAllReadNotificationsOlderThan3Days() {
        var notifications = notificationRepository.findAll();
        var expectedSize = notifications.size() - 2;
        for (int i = 0; i < 6; i++) {
            var notification = notifications.get(i);
            notification.setRead(true);
            notificationRepository.save(notification);
        }
        for (int i = 0; i < 2; i++) {
            var notification = notifications.get(i);
            notification.setDate(OffsetDateTime.now().minusDays(3).minusMinutes(1));
            notificationRepository.save(notification);
        }
        notificationDeleteScheduler.deleteReadNotifications();
        assertEquals(expectedSize, notificationRepository.findAll().size());
    }
}
