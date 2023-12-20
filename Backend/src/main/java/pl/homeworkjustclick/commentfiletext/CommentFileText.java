package pl.homeworkjustclick.commentfiletext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.homeworkjustclick.comment.Comment;
import pl.homeworkjustclick.file.File;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_comment_file_text")
@Getter
@Setter
public class CommentFileText implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private Integer id;
    @Column(name = "highlight_start")
    private Integer highlightStart;
    @Column(name = "highlight_end")
    private Integer highlightEnd;
    @Column(name = "color")
    private String color;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false, foreignKey = @ForeignKey(name = "comment_file_text_comment_id_fk"))
    @JsonIgnore
    private Comment comment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false, foreignKey = @ForeignKey(name = "comment_file_text_file_id_fk"))
    @JsonIgnore
    private File file;
}
