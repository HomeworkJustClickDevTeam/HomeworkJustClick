package pl.HomeworkJustClick.Backend.comment;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.HomeworkJustClick.Backend.commentevaluation.CommentEvaluation;
import pl.HomeworkJustClick.Backend.user.User;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_comment")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    @Schema(example = "0")
    private Integer id;

    @Column(name="title")
    @Schema(example = "Example title")
    @Size(max = 255)
    private String title;

    @Column(name = "description")
    @Schema(example = "Example desc")
    @Size(max = 255)
    private String description;

    @OneToMany(
            mappedBy = "comment",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<CommentEvaluation> commentEvaluations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "Fk_user"))
    @JsonIgnore
    private User user;

    public Comment(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.user = user;
    }
}
