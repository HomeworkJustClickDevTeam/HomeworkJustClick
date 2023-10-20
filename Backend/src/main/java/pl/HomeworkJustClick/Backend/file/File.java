package pl.HomeworkJustClick.Backend.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.HomeworkJustClick.Backend.assignment.Assignment;
import pl.HomeworkJustClick.Backend.solution.Solution;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_file")
@Getter
@Setter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    @Schema(example = "0")
    private int id;

    @Column(name="name")
    @Schema(example = "Example file")
    @Size(max = 255)
    private String name;

    @Column(name="format")
    @Schema(example = ".txt")
    @Size(max = 255)
    private String format;

    @Column(name="mongo_id")
    @Schema(example = "0")
    @Size(max = 255)
    private String mongo_id;

    @ManyToOne
    @JoinColumn(name = "assignmentId", foreignKey = @ForeignKey(name = "file_assignment_id_fk"))
    @JsonIgnore
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "solutionId", foreignKey = @ForeignKey(name = "file_solution_id_fk"))
    @JsonIgnore
    private Solution solution;

    public File(String name, String format, String mongo_id, Assignment assignment, Solution solution) {
        this.name = name;
        this.format = format;
        this.mongo_id = mongo_id;
        this.assignment = assignment;
        this.solution = solution;
    }
}
