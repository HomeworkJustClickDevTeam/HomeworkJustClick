package pl.HomeworkJustClick.Backend.groupstudent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import pl.HomeworkJustClick.Backend.group.Group;
import pl.HomeworkJustClick.Backend.user.User;

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
    @JoinColumn(name = "group_id", nullable = false, foreignKey = @ForeignKey(name = "Fk_group"))
    @JsonIgnore
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "Fk_student"))
    @JsonIgnore
    private User user;

    @Column
    @Schema(example = "Example desc")
    private String description;

    public GroupStudent(Group group, User user, String description) {
        this.group = group;
        this.user = user;
        this.description = description;
    }
}
