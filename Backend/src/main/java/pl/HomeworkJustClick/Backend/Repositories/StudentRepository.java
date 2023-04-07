package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.HomeworkJustClick.Backend.Entities.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query(value = "select s.id from _student s join _user u on u.id = s.user_id where s.user_id = :user_id", nativeQuery = true)
    int getStudentByUser(int user_id);
}
