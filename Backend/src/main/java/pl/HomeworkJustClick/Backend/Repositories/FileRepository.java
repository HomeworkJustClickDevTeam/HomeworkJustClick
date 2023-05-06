package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.HomeworkJustClick.Backend.Entities.File;

public interface FileRepository extends JpaRepository<File, Integer> {
}
