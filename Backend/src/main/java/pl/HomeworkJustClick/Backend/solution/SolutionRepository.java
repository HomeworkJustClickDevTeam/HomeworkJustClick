package pl.HomeworkJustClick.Backend.solution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Integer> {

    @Query(value = "select * from _solution where group_id = :group_id", nativeQuery = true)
    List<Solution> getSolutionsByGroupId(@Param("group_id") int group_id);

    @Query(value = "select * from _solution where assignment_id = :assignment_id", nativeQuery = true)
    List<Solution> getSolutionsByAssignmentId(@Param("assignment_id") int assignment_id);

    @Query(value = "select COUNT(*) from _solution where assignment_id = :assignment_id", nativeQuery = true)
    int countSolutionsByAssignmentId(@Param("assignment_id") int assignment_id);

    @Query(value = "select * from _solution where group_id = :group_id and user_id = :user_id", nativeQuery = true)
    List<Solution> getSolutionsByUserAndGroup(@Param("user_id") int user_id, @Param("group_id") int group_id);

    @Query(value = "select * from _solution where user_id = :user_id", nativeQuery = true)
    List<Solution> getSolutionsByUser(@Param("user_id") int user_id);

    @Query(value = "select * from _solution where user_id = :user_id and assignment_id = :assignment_id", nativeQuery = true)
    Optional<Solution> getSolutionByUserAndAssignment(@Param("user_id") int user_id, @Param("assignment_id") int assignment_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is null and s.group_id = :group_id", nativeQuery = true)
    List<Solution> getUncheckedSolutionsByGroup(@Param("group_id") int group_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is null and s.user_id = :student_id", nativeQuery = true)
    List<Solution> getUncheckedSolutionsByStudent(@Param("student_id") int student_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is null and s.user_id = :student_id and s.group_id = :group_id", nativeQuery = true)
    List<Solution> getUncheckedSolutionsByStudentAndGroup(@Param("student_id") int student_id, @Param("group_id") int group_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is null and s.assignment_id = :assignment_id", nativeQuery = true)
    List<Solution> getUncheckedSolutionsByAssignment(@Param("assignment_id") int assignment_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id join _group_teacher gt on s.group_id = gt.group_id where e.id is null and gt.user_id = :teacher_id", nativeQuery = true)
    List<Solution> getUncheckedSolutionsByTeacher(@Param("teacher_id") int teacher_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is not null and s.group_id = :group_id", nativeQuery = true)
    List<Solution> getCheckedSolutionsByGroup(@Param("group_id") int group_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is not null and s.user_id = :student_id", nativeQuery = true)
    List<Solution> getCheckedSolutionsByStudent(@Param("student_id") int student_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is not null and s.user_id = :student_id and s.group_id = :group_id", nativeQuery = true)
    List<Solution> getCheckedSolutionsByStudentAndGroup(@Param("student_id") int student_id, @Param("group_id") int group_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id where e.id is not null and s.assignment_id = :assignment_id", nativeQuery = true)
    List<Solution> getCheckedSolutionsByAssignment(@Param("assignment_id") int assignment_id);

    @Query(value = "select s.* from _solution s left outer join _evaluation e on s.id = e.solution_id join _group_teacher gt on s.group_id = gt.group_id where e.id is not null and gt.user_id = :teacher_id", nativeQuery = true)
    List<Solution> getCheckedSolutionsByTeacher(@Param("teacher_id") int teacher_id);

    @Query(value = "select COUNT(*) from _solution s join _evaluation e on s.id = e.solution_id where s.id = :solution_id", nativeQuery = true)
    int checkForEvaluationToSolution(@Param("solution_id") int solution_id);

    @Query(value = "SELECT s.* FROM _solution s LEFT JOIN _evaluation e on s.id = e.solution_id WHERE s.assignment_id = :assignment_id AND s.user_id = :user_id AND s.group_id = :group_id AND e.id IS NOT NULL", nativeQuery = true)
    Optional<Solution> getCheckedSolutionByUserAssignmentGroup(@Param("assignment_id") int assignment_id, @Param("user_id") int user_id, @Param("group_id") int group_id);

    @Query(value = "SELECT s.* FROM _solution s LEFT JOIN _evaluation e on s.id = e.solution_id WHERE s.assignment_id = :assignment_id AND s.user_id = :user_id AND s.group_id = :group_id AND e.id IS NULL", nativeQuery = true)
    Optional<Solution> getUncheckedSolutionByUserAssignmentGroup(@Param("assignment_id") int assignment_id, @Param("user_id") int user_id, @Param("group_id") int group_id);

}
