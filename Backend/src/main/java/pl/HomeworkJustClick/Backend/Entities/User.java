package pl.HomeworkJustClick.Backend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.HomeworkJustClick.Backend.Enums.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    @Schema(example = "0")
    private int id;

    @Column(name = "email")
    @Schema(example = "najman@gmail.com")
    private String email;

    @JsonIgnore
    @Column(name = "password")
    @Schema(example = "123123")
    private String password;

    @Column(name="isVerified")
    @Schema(example = "true", allowableValues = {"true", "false"})
    private boolean isVerified;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Schema(description = "The values are listed in the same order as in backend, so you can use names or numbers 0-2.", implementation = Role.class, example = "USER")
    private Role role;

    @Column(name="index")
    @Schema(example = "12345")
    private int index;

    @Column(name="firstname")
    @Schema(example = "Marcin")
    private String firstname;

    @Column(name="lastname")
    @Schema(example = "Najman")
    private String lastname;

    @Column(name="color")
    @Schema(example = "0")
    @Max(20)
    @Min(0)
    private int color;

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Assignment> assignments = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Solution> solutions = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Evaluation> evaluations = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<GroupStudent> groupStudents = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<GroupTeacher> groupTeachers = new ArrayList<>();

    @Override
    @Schema(example = "naj1231")
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public User(String email, String password, boolean isVerified, Role role, int index, String firstname, String surname, int color) {
        this.email = email;
        this.password = password;
        this.isVerified = isVerified;
        this.role = role;
        this.index = index;
        this.firstname = firstname;
        this.lastname = surname;
        this.color = color;
    }

    public User(String email, String password, boolean isVerified, Role role, int index, String firstname, String lastname, List<GroupStudent> groupStudents, List<GroupTeacher> groupTeachers, int color) {
        this.email = email;
        this.password = password;
        this.isVerified = isVerified;
        this.role = role;
        this.index = index;
        this.firstname = firstname;
        this.lastname = lastname;
        this.groupStudents = groupStudents;
        this.groupTeachers = groupTeachers;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String surname) {
        this.lastname = surname;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
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
