package pl.HomeworkJustClick.Backend.comment;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.HomeworkJustClick.Backend.commentevaluation.CommentEvaluation;
import pl.HomeworkJustClick.Backend.commentfileimg.CommentFileImg;
import pl.HomeworkJustClick.Backend.commentfiletext.CommentFileText;
import pl.HomeworkJustClick.Backend.user.User;

import java.time.OffsetDateTime;
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
    private Integer id;

    @Column(name="title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "color")
    private String color;

    @Column(name = "lastUsedDate")
    private OffsetDateTime lastUsedDate;

    @Column(name = "counter")
    private Integer counter;

    @OneToMany(
            mappedBy = "comment",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<CommentEvaluation> commentEvaluations = new ArrayList<>();

    @OneToMany(
            mappedBy = "comment",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<CommentFileImg> commentFileImgs = new ArrayList<>();

    @OneToMany(
            mappedBy = "comment",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<CommentFileText> commentFileTexts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "comment_user_id_fk"))
    @JsonIgnore
    private User user;

    public Comment(String title, String description, User user, String color, OffsetDateTime lastUsedDate) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.color = color;
        this.lastUsedDate = lastUsedDate;
    }
}
