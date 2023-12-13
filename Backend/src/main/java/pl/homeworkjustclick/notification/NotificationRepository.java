package pl.homeworkjustclick.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Page<Notification> findAllByUserId(Integer userId, Pageable pageable);

    Integer countAllByUserId(Integer userId);

    List<Notification> findAllByRead(Boolean read);
}
