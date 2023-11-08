package pl.HomeworkJustClick.Backend.evaluationpanel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.HomeworkJustClick.Backend.evaluationpanelbutton.EvaluationPanelButton;
import pl.HomeworkJustClick.Backend.user.User;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_evaluation_panel")
@Getter
@Setter
public class EvaluationPanel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "width")
    private Integer width;
    @Column(name = "counter")
    private Integer counter;
    @Column(name = "last_used_date")
    private OffsetDateTime lastUsedDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "evaluation_panel_user_id_fk"))
    @JsonIgnore
    private User user;
    @OneToMany(
            mappedBy = "evaluationPanel",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<EvaluationPanelButton> evaluationPanelButtons = new ArrayList<>();
}
