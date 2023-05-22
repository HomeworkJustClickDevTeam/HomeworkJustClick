package pl.HomeworkJustClick.Backend.Entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_evaluation")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @Column(name = "result")
    private Double result;

    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_id", nullable = false, foreignKey = @ForeignKey(name = "Fk_solution"))
    @JsonIgnore
    private Solution solution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false, foreignKey = @ForeignKey(name = "Fk_group"))
    @JsonIgnore
    private Group group;

    @Column(name = "creationDatetime", updatable = false, nullable = false)
    @CreationTimestamp
    private OffsetDateTime creationDatetime;

    @Column(name = "lastModifiedDatetime")
    @UpdateTimestamp
    private OffsetDateTime lastModifiedDatetime;

    @Column(name = "grade")
    private Double grade;

    @OneToMany(
            mappedBy = "evaluation",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<CommentEvaluation> commentEvaluations = new ArrayList<>();

    public Evaluation(User user, Solution solution){
        this.user = user;
        this.solution = solution;
    }

    public int getId() {
        return id;
    }

    public OffsetDateTime getCreationDatetime() {
        return creationDatetime;
    }

    public OffsetDateTime getLastModifiedDatetime() {
        return lastModifiedDatetime;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
