package pl.homeworkjustclick.notification;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;

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
public class NotificationDeleteSchedulerTest extends BaseTestEntity {
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
    void shouldDeleteAllReadNotifications() {
        var notifications = notificationRepository.findAll();
        var expectedSize = notifications.size() - 2;
        for (int i = 0; i < 2; i++) {
            var notification = notifications.get(i);
            notification.setRead(true);
            notificationRepository.save(notification);
        }
        notificationDeleteScheduler.deleteReadNotifications();
        assertEquals(expectedSize, notificationRepository.findAll().size());
    }
}
