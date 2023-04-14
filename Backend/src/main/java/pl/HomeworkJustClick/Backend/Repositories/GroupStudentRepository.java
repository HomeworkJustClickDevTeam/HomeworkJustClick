package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.HomeworkJustClick.Backend.Entities.GroupStudent;

public interface GroupStudentRepository extends JpaRepository<GroupStudent,Integer> {
}
