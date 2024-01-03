package pl.homeworkjustclick.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class NotificationDeleteScheduler {
    private final NotificationRepository repository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Warsaw")
    public void deleteReadNotifications() {
        var notifications = repository.findAllByRead(true)
                .stream().filter(n -> n.getDate().isBefore(OffsetDateTime.now().minusDays(3)))
                .toList();
        repository.deleteAll(notifications);
    }
}
