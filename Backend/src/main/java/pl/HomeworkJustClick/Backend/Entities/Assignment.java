package pl.HomeworkJustClick.Backend.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.OffsetDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_assignments")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;

    @Column(name = "taskDescription")
    private String taskDescription;

    @Column(name = "creationDatetime", updatable = false, nullable = false)
    @CreationTimestamp
    private OffsetDateTime creationDatetime;

    @Column(name = "lastModifiedDatetime")
    @UpdateTimestamp
    private OffsetDateTime lastModifiedDatetime;

    @Column(name = "completionDatetime")
    private OffsetDateTime completionDatetime;

    @Column(name = "result")
    private double result;

    @Column(name = "title")
    private String title;

    public Assignment(String title) {
        this.title = title;
    }

    public double getResult() {
        return result;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public OffsetDateTime getCreationDatetime() {
        return creationDatetime;
    }

    public OffsetDateTime getLastModifiedDatetime() {
        return lastModifiedDatetime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskContent) {
        this.taskDescription = taskContent;
    }

    public OffsetDateTime getCompletionDatetime() {
        return completionDatetime;
    }

    public void setCompletionDatetime(OffsetDateTime completionDatetime) {
        this.completionDatetime = completionDatetime;
    }
}
