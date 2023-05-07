package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.HomeworkJustClick.Backend.Entities.File;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Integer> {

    @Query(value="select * from _file where assignment_id = :id", nativeQuery = true)
    List<File> getFilesByAssignmentId(int id);

    @Query(value="select * from _file where solution_id = :id", nativeQuery = true)
    List<File> getFilesBySolutionId(int id);
}
