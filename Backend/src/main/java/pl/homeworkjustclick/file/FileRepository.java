package pl.homeworkjustclick.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {

    @Query(value="select * from _file where assignment_id = :assignment_id", nativeQuery = true)
    List<File> getFilesByAssignmentId(@Param("assignment_id") int assignment_id);

    @Query(value="select * from _file where solution_id = :solution_id", nativeQuery = true)
    List<File> getFilesBySolutionId(@Param("solution_id") int solution_id);
}
