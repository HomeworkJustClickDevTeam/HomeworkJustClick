package pl.HomeworkJustClick.Backend.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
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
    private String name;

    @Column(name="format")
    @Schema(example = ".txt")
    private String format;

    @Column(name="mongo_id")
    @Schema(example = "0")
    private String mongo_id;

    @ManyToOne
    @JoinColumn(name = "assignmentId")
    @JsonIgnore
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "solutionId")
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
