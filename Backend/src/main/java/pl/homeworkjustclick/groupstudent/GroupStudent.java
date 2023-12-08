package pl.homeworkjustclick.groupstudent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.homeworkjustclick.group.Group;
import pl.homeworkjustclick.user.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_group_student")
@Getter
@Setter
public class GroupStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    @Schema(example = "0")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false, foreignKey = @ForeignKey(name = "group_student_group_id_fk"))
    @JsonIgnore
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "group_student_user_id_fk"))
    @JsonIgnore
    private User user;

    @Column
    @Schema(example = "Example desc")
    @Size(max = 255)
    private String description;

    public GroupStudent(Group group, User user, String description) {
        this.group = group;
        this.user = user;
        this.description = description;
    }
}
