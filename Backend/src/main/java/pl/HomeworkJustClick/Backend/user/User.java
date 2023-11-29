package pl.HomeworkJustClick.Backend.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.HomeworkJustClick.Backend.assignment.Assignment;
import pl.HomeworkJustClick.Backend.evaluation.Evaluation;
import pl.HomeworkJustClick.Backend.evaluationpanel.EvaluationPanel;
import pl.HomeworkJustClick.Backend.groupstudent.GroupStudent;
import pl.HomeworkJustClick.Backend.groupteacher.GroupTeacher;
import pl.HomeworkJustClick.Backend.infrastructure.enums.Role;
import pl.HomeworkJustClick.Backend.solution.Solution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    @Schema(example = "0")
    private int id;

    @Column(name = "email")
    @Schema(example = "example@gmail.com")
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
    @Schema(example = "Example")
    private String firstname;

    @Column(name="lastname")
    @Schema(example = "Exampler")
    private String lastname;

    @Column(name="color")
    @Schema(example = "0")
    @Max(19)
    @Min(0)
    private int color;

    @Column(name = "salt")
    private String salt;

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

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<EvaluationPanel> evaluationPanels = new ArrayList<>();

    @Override
    @Schema(example = "exampleman1231")
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

    public User(String email, String password, boolean isVerified, Role role, int index, String firstname, String surname, int color, String salt) {
        this.email = email;
        this.password = password;
        this.isVerified = isVerified;
        this.role = role;
        this.index = index;
        this.firstname = firstname;
        this.lastname = surname;
        this.color = color;
        this.salt = salt;
    }
}
