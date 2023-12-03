package pl.HomeworkJustClick.Backend.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationDeleteScheduler {
    private final NotificationRepository repository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Warsaw")
    public void deleteReadNotifications() {
        var notifications = repository.findAllByRead(true);
        repository.deleteAll(notifications);
    }
}
