package pl.HomeworkJustClick.Backend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_assignment")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "Fk_user"))
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false, foreignKey = @ForeignKey(name = "Fk_group"))
    @JsonIgnore
    private Group group;

    @OneToMany(mappedBy = "assignment", orphanRemoval = true)
    @JsonIgnore
    private List<Solution> solutions = new ArrayList<>();

    @OneToMany(mappedBy = "assignment", orphanRemoval = true)
    @JsonIgnore
    private List<File> files = new ArrayList<>();

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

    @Column(name = "title")
    private String title;
    @Column(name = "visible", columnDefinition = "boolean default false", nullable = false)
    private Boolean visible = false;

    public int getMax_points() {
        return max_points;
    }

    public void setMax_points(int max_points) {
        this.max_points = max_points;
    }

    @Column(name = "max_points")
    private int max_points;

    public Assignment(String title) {
        this.title = title;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
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
