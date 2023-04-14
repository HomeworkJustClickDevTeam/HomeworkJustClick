package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.HomeworkJustClick.Backend.Entities.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {
}
