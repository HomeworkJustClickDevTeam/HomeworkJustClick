package pl.homeworkjustclick.notification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;
import pl.homeworkjustclick.user.UserService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final UserService userService;

    public Notification findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification with id = " + id + " not found"));
    }

    public List<NotificationResponseDto> getNotificationsByUserId(Integer userId) {
        var user = userService.findById(userId);
        return repository.findAllByUserId(user.getId())
                .stream()
                .map(mapper::map)
                .sorted(Comparator.nullsLast(
                        (n1, n2) -> n2.getDate().compareTo(n1.getDate())))
                .toList();
    }

    public Integer countNotificationsByUserId(Integer userId) {
        return repository.countAllByUserId(userId);
    }

    @Transactional
    public List<NotificationResponseDto> updateReadInNotifications(List<Integer> notificationsIds) {
        var notifications = new ArrayList<Notification>();
        notificationsIds.forEach(id -> {
            var notification = findById(id);
            notification.setRead(true);
            notifications.add(notification);
        });
        return repository.saveAll(notifications).stream().map(mapper::map).toList();
    }

    @Transactional
    public void deleteNotification(Integer id) {
        var notification = findById(id);
        repository.delete(notification);
    }
}
