package pl.homeworkjustclick.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.assignment.Assignment;
import pl.homeworkjustclick.group.Group;
import pl.homeworkjustclick.user.User;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class NotificationCreateService {
    private final NotificationRepository repository;

    public void createEvaluationNotification(User user, Assignment assignment, Group group) {
        var notification = Notification.builder()
                .description("Oceniono zadanie \"" + assignment.getTitle() + "\" w grupie \"" + group.getName() + "\".")
                .read(false)
                .date(OffsetDateTime.now())
                .user(user)
                .assignment(assignment)
                .build();
        repository.save(notification);
    }

    public void createAssignmentNotification(User user, Assignment assignment, Group group) {
        var notification = Notification.builder()
                .description("Dodano nowe zadanie \"" + assignment.getTitle() + "\" w grupie \"" + group.getName() + "\". Termin wykonania: " + assignment.getCompletionDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ".")
                .read(false)
                .date(OffsetDateTime.now())
                .user(user)
                .assignment(assignment)
                .build();
        repository.save(notification);
    }

    public void createEvaluationReportNotification(User teacher, Assignment assignment, User student) {
        var notification = Notification.builder()
                .description("Uczeń " + student.getFirstname() + " " + student.getLastname() + " zgłosił ocenę do zadania \"" + assignment.getTitle() + "\" jako niepoprawną.")
                .read(false)
                .date(OffsetDateTime.now())
                .user(teacher)
                .assignment(assignment)
                .build();
        repository.save(notification);
    }
}
