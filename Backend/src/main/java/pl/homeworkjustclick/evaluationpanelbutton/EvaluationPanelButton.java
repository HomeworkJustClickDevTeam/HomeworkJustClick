package pl.homeworkjustclick.evaluationpanelbutton;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.homeworkjustclick.evaluationbutton.EvaluationButton;
import pl.homeworkjustclick.evaluationpanel.EvaluationPanel;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_evaluation_panel_button")
@Getter
@Setter
public class EvaluationPanelButton implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_panel_id", nullable = false, foreignKey = @ForeignKey(name = "evaluation_panel_button_evaluation_panel_id_fk"))
    @JsonIgnore
    private EvaluationPanel evaluationPanel;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_button_id", nullable = false, foreignKey = @ForeignKey(name = "evaluation_panel_button_evaluation_button_id_fk"))
    @JsonIgnore
    private EvaluationButton evaluationButton;
}
