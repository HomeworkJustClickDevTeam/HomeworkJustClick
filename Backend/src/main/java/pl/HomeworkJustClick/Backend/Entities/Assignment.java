package pl.HomeworkJustClick.Backend.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
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
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private Group group;

    @Column(name = "task_content")
    private String task_content;

    @Column(name = "creation_datetime", updatable = false)
    @CreationTimestamp
    private OffsetDateTime creation_datetime;

    @Column(name = "last_modified_datetime")
    @UpdateTimestamp
    private OffsetDateTime last_modified_datetime;

    @Column(name = "completion_datetime", nullable = true)
    private OffsetDateTime completion_datetime;

    public Assignment(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public OffsetDateTime getCreation_datetime() {
        return creation_datetime;
    }

    public OffsetDateTime getLast_modified_datetime() {
        return last_modified_datetime;
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

    public String getTask_content() {
        return task_content;
    }

    public void setTask_content(String task_content) {
        this.task_content = task_content;
    }

    public OffsetDateTime getCompletion_datetime() {
        return completion_datetime;
    }

    public void setCompletion_datetime(OffsetDateTime completion_datetime) {
        this.completion_datetime = completion_datetime;
    }
}
