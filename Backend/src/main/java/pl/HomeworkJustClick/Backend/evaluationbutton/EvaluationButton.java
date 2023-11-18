package pl.HomeworkJustClick.Backend.evaluationbutton;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.HomeworkJustClick.Backend.evaluationpanelbutton.EvaluationPanelButton;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_evaluation_button")
@Getter
@Setter
public class EvaluationButton {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private Integer id;
    @Column(name = "points")
    private Double points;
    @OneToMany(
            mappedBy = "evaluationButton",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<EvaluationPanelButton> evaluationPanelButtons = new ArrayList<>();
}
