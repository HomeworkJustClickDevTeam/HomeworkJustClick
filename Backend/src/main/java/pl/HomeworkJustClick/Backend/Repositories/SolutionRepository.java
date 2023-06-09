package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.HomeworkJustClick.Backend.Entities.Solution;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Integer> {

    @Query(value = "select * from _solution where group_id = :group_id", nativeQuery = true)
    List<Solution> getSolutionsByGroupId(int group_id);

    @Query(value = "select * from _solution where assignment_id = :assignment_id", nativeQuery = true)
    List<Solution> getSolutionsByAssignmentId(int assignment_id);

    @Query(value = "select COUNT(*) from _solution where assignment_id = :assignment_id", nativeQuery = true)
    int countSolutionsByAssignmentId(int assignment_id);

    @Query(value = "select * from _solution where group_id = :group_id and user_id = :user_id", nativeQuery = true)
    List<Solution> getSolutionsByUserAndGroup(int user_id, int group_id);

    @Query(value = "select * from _solution where user_id = :user_id", nativeQuery = true)
    List<Solution> getSolutionsByUser(int user_id);

    @Query(value = "select * from _solution where user_id = :user_id and assignment_id = :assignment_id", nativeQuery = true)
    Optional<Solution> getSolutionByUserAndAssignment(int user_id, int assignment_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is null and s.group_id = :group_id", nativeQuery = true)
    List<Solution> getUncheckedSolutionsByGroup(int group_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is null and s.user_id = :student_id", nativeQuery = true)
    List<Solution> getUncheckedSolutionsByStudent(int student_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is null and s.user_id = :student_id and s.group_id = :group_id", nativeQuery = true)
    List<Solution> getUncheckedSolutionsByStudentAndGroup(int student_id, int group_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is null and s.assignment_id = :assignment_id", nativeQuery = true)
    List<Solution> getUncheckedSolutionsByAssignment(int assignment_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id join _group_teacher gt on s.group_id = gt.group_id where e.id is null and gt.user_id = :teacher_id", nativeQuery = true)
    List<Solution> getUncheckedSolutionsByTeacher(int teacher_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is not null and s.group_id = :group_id", nativeQuery = true)
    List<Solution> getCheckedSolutionsByGroup(int group_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is not null and s.user_id = :student_id", nativeQuery = true)
    List<Solution> getCheckedSolutionsByStudent(int student_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is not null and s.user_id = :student_id and s.group_id = :group_id", nativeQuery = true)
    List<Solution> getCheckedSolutionsByStudentAndGroup(int student_id, int group_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is not null and s.assignment_id = :assignment_id", nativeQuery = true)
    List<Solution> getCheckedSolutionsByAssignment(int assignment_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id join _group_teacher gt on s.group_id = gt.group_id where e.id is not null and gt.user_id = :teacher_id", nativeQuery = true)
    List<Solution> getCheckedSolutionsByTeacher(int teacher_id);
}
