package pl.homeworkjustclick.evaluationpanelassignment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.homeworkjustclick.assignment.Assignment;
import pl.homeworkjustclick.evaluationpanel.EvaluationPanel;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_evaluation_panel_assignment")
@Getter
@Setter
public class EvaluationPanelAssignment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_panel_id", nullable = false, foreignKey = @ForeignKey(name = "evaluation_panel_assignment_evaluation_panel_id_fk"))
    @JsonIgnore
    private EvaluationPanel evaluationPanel;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false, foreignKey = @ForeignKey(name = "evaluation_panel_assignment_assignment_id_fk"))
    @JsonIgnore
    private Assignment assignment;
}
