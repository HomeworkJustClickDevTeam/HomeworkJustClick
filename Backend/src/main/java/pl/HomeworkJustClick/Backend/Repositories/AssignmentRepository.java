package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.HomeworkJustClick.Backend.Entities.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

}
