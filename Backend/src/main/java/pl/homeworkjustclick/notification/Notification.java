package pl.homeworkjustclick.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.homeworkjustclick.assignment.Assignment;
import pl.homeworkjustclick.user.User;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_notification")
@Getter
@Setter
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "description")
    @Size(max = 500)
    private String description;
    @Column(name = "read")
    private Boolean read = false;
    @Column(name = "date")
    private OffsetDateTime date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false, foreignKey = @ForeignKey(name = "notification_assignment_id_fk"))
    @JsonIgnore
    private Assignment assignment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "notification_user_id_fk"))
    @JsonIgnore
    private User user;
}
