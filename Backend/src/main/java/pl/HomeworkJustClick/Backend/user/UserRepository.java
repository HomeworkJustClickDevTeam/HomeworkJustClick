package pl.HomeworkJustClick.Backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query(value = "select u.* from _user u join _group_teacher gt on u.id = gt.user_id where gt.group_id = :group_id", nativeQuery = true)
    List<User> getTeachersByGroupId(@Param("group_id") int group_id);

    @Query(value = "select u.* from _user u join _group_student gs on u.id = gs.user_id where gs.group_id = :group_id", nativeQuery = true)
    List<User> getStudentsByGroupId(@Param("group_id") int group_id);

}
