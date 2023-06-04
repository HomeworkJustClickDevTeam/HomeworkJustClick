package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.HomeworkJustClick.Backend.Entities.Solution;

import java.util.List;
import java.util.Optional;

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
}
