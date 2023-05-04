package pl.HomeworkJustClick.Backend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="color")
    int color;

    @Column(name="isArchived")
    boolean isArchived;

    @OneToMany(mappedBy = "group")
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
