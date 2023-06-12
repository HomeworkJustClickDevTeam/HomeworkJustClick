package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.HomeworkJustClick.Backend.Entities.Assignment;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    @Query(value = "select * from _assignment where group_id = :group_id", nativeQuery = true)
    List<Assignment> getAssignmentsByGroupId(int group_id);

    @Query(value = "select a.* from _assignment a join _group_student gs on a.group_id = gs.group_id where a.group_id = :group_id and gs.user_id = :user_id", nativeQuery = true)
    List<Assignment> getAllAssignmentsByGroupIdAndUserId(int group_id, int user_id);

    @Query(value = "select a.* from _assignment a join _group_student gs on a.group_id = gs.group_id left outer join _solution s on a.id = s.assignment_id where a.group_id = :group_id and gs.user_id = :user_id and s.id is null", nativeQuery = true)
    List<Assignment> getUndoneAssignmentsByGroupIdAndUserId(int group_id, int user_id);

    @Query(value = "select a.* from _assignment a join _group_student gs on a.group_id = gs.group_id left outer join _solution s on a.id = s.assignment_id where gs.user_id = :user_id and s.id is null", nativeQuery = true)
    List<Assignment> getUndoneAssignmentsByStudent(int user_id);

    @Query(value = "select a.* from _assignment a join _group_student gs on a.group_id = gs.group_id left outer join _solution s on a.id = s.assignment_id where a.group_id = :group_id and gs.user_id = :user_id and s.id is not null", nativeQuery = true)
    List<Assignment> getDoneAssignmentsByGroupIdAndUserId(int group_id, int user_id);

    @Query(value = "select a.* from _assignment a join _group_student gs on a.group_id = gs.group_id left outer join _solution s on a.id = s.assignment_id where gs.user_id = :user_id and s.id is not null", nativeQuery = true)
    List<Assignment> getDoneAssignmentsByStudent(int user_id);

    @Query(value = "select a.* from _assignment a join _group_student gs on a.group_id = gs.group_id left outer join _solution s on a.id = s.assignment_id where a.group_id = :group_id and gs.user_id = :user_id and s.id is null and a.completion_datetime < now();", nativeQuery = true)
    List<Assignment> getExpiredUndoneAssignmentsByGroupIdAndUserId(int group_id, int user_id);

    @Query(value = "select a.* from _assignment a join _group_student gs on a.group_id = gs.group_id left outer join _solution s on a.id = s.assignment_id where gs.user_id = :student_id and s.id is null and a.completion_datetime < now();", nativeQuery = true)
    List<Assignment> getExpiredUndoneAssignmentsByStudent(int student_id);

    @Query(value = "select a.* from _assignment a join _group_student gs on a.group_id = gs.group_id left outer join _solution s on a.id = s.assignment_id where a.group_id = :group_id and gs.user_id = :user_id and s.id is null and a.completion_datetime > now();", nativeQuery = true)
    List<Assignment> getNonExpiredUndoneAssignmentsByGroupIdAndUserId(int group_id, int user_id);

    @Query(value = "select a.* from _assignment a join _group_student gs on a.group_id = gs.group_id left outer join _solution s on a.id = s.assignment_id where gs.user_id = :student_id and s.id is null and a.completion_datetime > now();", nativeQuery = true)
    List<Assignment> getNonExpiredUndoneAssignmentsByStudent(int student_id);

    @Query(value = "select a.* from _assignment a join _group_student gs on a.group_id = gs.group_id where gs.user_id = :user_id", nativeQuery = true)
    List<Assignment> getAllAssignmentsByStudent(int user_id);

    @Query(value = "select COUNT(*) from _assignment a join _solution s on a.id = s.assignment_id where a.id = :assignment_id", nativeQuery = true)
    int checkForSolutionToAssignment(int assignment_id);
}
