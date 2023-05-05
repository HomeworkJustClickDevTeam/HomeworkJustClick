package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.HomeworkJustClick.Backend.Entities.Solution;

public interface SolutionRepository extends JpaRepository<Solution, Integer> {

}
