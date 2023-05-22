package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.HomeworkJustClick.Backend.Entities.Assignment;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    @Query(value = "select * from _assignment where group_id = :group_id", nativeQuery = true)
    List<Assignment> getAssignmentsByGroupId(int group_id);

}
