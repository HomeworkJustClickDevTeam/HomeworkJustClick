package pl.homeworkjustclick.evaluationpanel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import pl.homeworkjustclick.BaseTestEntity;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = {
        "classpath:db/cleanup_all.sql",
        "classpath:db/init_user.sql",
        "classpath:db/init_group.sql",
        "classpath:db/init_group_student.sql",
        "classpath:db/init_group_teacher.sql",
        "classpath:db/init_assignment.sql",
        "classpath:db/init_evaluation_panel.sql"
})
public class EvaluationPanelUtilsServiceTest extends BaseTestEntity {
    @Autowired
    EvaluationPanelRepository evaluationPanelRepository;
    @Autowired
    EvaluationPanelUtilsService evaluationPanelUtilsService;

    @Test
    void shouldUpdateEvaluationPanel() {
        var evaluationPanel = evaluationPanelRepository.findAll().get(0);
        evaluationPanelUtilsService.updateEvaluationPanel(evaluationPanel.getId());
        var updatedEvaluationPanel = evaluationPanelRepository.findById(evaluationPanel.getId()).get();
        assertTrue(evaluationPanel.getLastUsedDate().isBefore(updatedEvaluationPanel.getLastUsedDate()));
        assertEquals(evaluationPanel.getCounter() + 1, (int) updatedEvaluationPanel.getCounter());
    }

    @Test
    void shouldThrowException() {
        assertThrows(EntityNotFoundException.class, () -> evaluationPanelUtilsService.updateEvaluationPanel(999));
    }
}
