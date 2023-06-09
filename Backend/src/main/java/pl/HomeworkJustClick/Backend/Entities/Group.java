package pl.HomeworkJustClick.Backend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    @Schema(example = "0")
    private int id;

    @Schema(example = "Example Group")
    @Column(name="name")
    private String name;

    @Schema(example = "Example desc")
    @Column(name="description")
    private String description;

    @Schema(example = "0")
    @Max(19)
    @Min(0)
    @Column(name="color")
    private int color;

    @Column(name="isArchived")
    @Schema(example = "false")
    private boolean isArchived;

    @OneToMany(
            mappedBy = "group",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Assignment> assignments = new ArrayList<>();

    @OneToMany(
            mappedBy = "group",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<GroupStudent> groupStudents = new ArrayList<>();

    @OneToMany(
            mappedBy = "group",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<GroupTeacher> groupTeachers = new ArrayList<>();

    public Group(String name, String description, int color, boolean isArchived) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.isArchived = isArchived;
    }

    public Group(int id, String name, String description, int color, boolean isArchived) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.isArchived = isArchived;
    }

    public Group(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public List<GroupStudent> getGroupStudents() {
        return groupStudents;
    }

    public void setGroupStudents(List<GroupStudent> groupStudents) {
        this.groupStudents = groupStudents;
    }

    public List<GroupTeacher> getGroupTeachers() {
        return groupTeachers;
    }

    public void setGroupTeachers(List<GroupTeacher> groupTeachers) {
        this.groupTeachers = groupTeachers;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }
}
