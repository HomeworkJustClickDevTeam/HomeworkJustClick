package pl.HomeworkJustClick.Backend.commentfileimg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import pl.HomeworkJustClick.Backend.comment.Comment;
import pl.HomeworkJustClick.Backend.file.File;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_comment_file_img")
@Getter
@Setter
public class CommentFileImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private Integer id;
    @Column(name = "left_top_x")
    private Integer leftTopX;
    @Column(name = "left_top_y")
    private Integer leftTopY;
    @Column(name = "width")
    private Integer width;
    @Column(name = "height")
    private Integer height;
    @Column(name = "img_width")
    private Integer imgWidth;
    @Column(name = "img_height")
    private Integer imgHeight;
    @Column(name = "color")
    private String color;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false, foreignKey = @ForeignKey(name = "comment_file_img_comment_id_fk"))
    @JsonIgnore
    private Comment comment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false, foreignKey = @ForeignKey(name = "comment_file_img_file_id_fk"))
    @JsonIgnore
    private File file;
}
