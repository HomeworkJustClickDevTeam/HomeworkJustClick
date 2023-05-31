package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.HomeworkJustClick.Backend.Entities.Evaluation;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {

    @Query(value = "select e.* from _evaluation e join _solution s on e.solution_id = s.id where s.assignment_id = :assignment_id", nativeQuery = true)
    public List<Evaluation> getEvaluationsByAssignment(int assignment_id);

    @Query(value = "select COUNT(e.*) from _evaluation e join _solution s on e.solution_id = s.id where s.assignment_id = :assignment_id", nativeQuery = true)
    public int countEvaluationsByAssignment(int assignment_id);

    @Query(value = "select e.* from _evaluation e join _group_student gs on gs.group_id = e.group_id where gs.user_id = :student_id", nativeQuery = true)
    public List<Evaluation> getAllEvaluationsByStudent(int student_id);

    @Query(value = "select e.* from _evaluation e join _group_student gs on gs.group_id = e.group_id where gs.user_id = :student_id and e.group_id = :group_id", nativeQuery = true)
    public List<Evaluation> getAllEvaluationsByStudentInGroup(int student_id, int group_id);

    @Query(value = "select e.* from _evaluation e join _solution s on e.solution_id = s.id where s.id = :solution_id", nativeQuery = true)
    public Evaluation getEvaluationBySolution(int solution_id);
}