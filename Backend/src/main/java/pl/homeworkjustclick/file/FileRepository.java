package pl.homeworkjustclick.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {

    @Query(value = "select * from _file where assignment_id = :assignmentId", nativeQuery = true)
    List<File> getFilesByAssignmentId(@Param("assignmentId") int assignmentId);

    @Query(value = "select * from _file where solution_id = :solutionId", nativeQuery = true)
    List<File> getFilesBySolutionId(@Param("solutionId") int solutionId);
}
