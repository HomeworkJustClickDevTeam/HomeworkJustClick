package pl.HomeworkJustClick.Backend.notification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.infrastructure.exception.EntityNotFoundException;
import pl.HomeworkJustClick.Backend.user.UserService;

import java.util.ArrayList;
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

    public Slice<NotificationResponseDto> getNotificationsByUserId(Integer userId, Pageable pageable) {
        var user = userService.findById(userId);
        return repository.findAllByUserId(user.getId(), pageable)
                .map(mapper::map);
    }

    public Integer countNotificationsByUserId(Integer userId) {
        return repository.countAllByUserId(userId);
    }

    @Transactional
    public List<NotificationResponseDto> updateReadInNotifications(List<Integer> notificationsIds) {
        var response = new ArrayList<NotificationResponseDto>();
        notificationsIds.forEach(id -> {
            var notification = findById(id);
            notification.setRead(true);
            response.add(mapper.map(repository.save(notification)));
        });
        return response;
    }

    @Transactional
    public void deleteNotification(Integer id) {
        var notification = findById(id);
        repository.delete(notification);
    }
}
