package pl.homeworkjustclick.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findAllByUserId(Integer userId);

    Integer countAllByUserId(Integer userId);

    List<Notification> findAllByRead(Boolean read);
}
