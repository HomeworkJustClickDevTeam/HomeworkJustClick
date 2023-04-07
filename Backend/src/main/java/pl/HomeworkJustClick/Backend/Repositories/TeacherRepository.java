package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.HomeworkJustClick.Backend.Entities.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query(value = "select t.id from _teacher t join _user u on u.id = t.user_id where t.user_id = :user_id", nativeQuery = true)
    int getTeacherByUser(int user_id);
}
