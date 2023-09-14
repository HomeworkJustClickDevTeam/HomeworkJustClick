package pl.HomeworkJustClick.Backend.group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.HomeworkJustClick.Backend.assignment.Assignment;
import pl.HomeworkJustClick.Backend.groupstudent.GroupStudent;
import pl.HomeworkJustClick.Backend.groupteacher.GroupTeacher;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_group")
@Getter
@Setter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    @Schema(example = "0")
    private int id;

    @Schema(example = "Example Group")
    @Column(name="name")
    @Size(max = 255)
    private String name;

    @Schema(example = "Example desc")
    @Column(name="description")
    @Size(max = 255)
    private String description;

    @Schema(example = "0")
    @Max(19)
    @Min(0)
    @Column(name="color")
    private int color;

    @Column(name="isArchived")
    @Schema(example = "false")
    private boolean isArchived;

    @OneToMany(
            mappedBy = "group",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Assignment> assignments = new ArrayList<>();

    @OneToMany(
            mappedBy = "group",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<GroupStudent> groupStudents = new ArrayList<>();

    @OneToMany(
            mappedBy = "group",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<GroupTeacher> groupTeachers = new ArrayList<>();

    public Group(String name, String description, int color, boolean isArchived) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.isArchived = isArchived;
    }
}
