package pl.homeworkjustclick.group;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {
        "classpath:db/cleanup_all.sql",
        "classpath:db/init_user.sql",
        "classpath:db/init_group.sql",
        "classpath:db/init_group_student.sql",
        "classpath:db/init_group_teacher.sql"
})
@WithMockUser
class GroupControllerTest extends BaseTestEntity {

    @Test
    void shouldGetAllGroups() throws Exception {
        mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }
}
