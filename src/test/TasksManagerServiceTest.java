import junit.framework.TestCase;
import services.TasksManagerService;

public class TasksManagerServiceTest extends TestCase {
    TasksManagerService tasksManagerService;

    @Override
    protected void setUp() throws Exception {
        tasksManagerService = new TasksManagerService();
        tasksManagerService.createNewEpic("Epic 1", "Desc 1");
        tasksManagerService.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L);
        tasksManagerService.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L);
        tasksManagerService.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L);
        tasksManagerService.createNewEpic("Epic 2", "Desc 2");
        tasksManagerService.createNewSubtask("Subtask 2.1", "Desc sub 1", 2L);
        tasksManagerService.createNewSubtask("Subtask 2.2", "Desc sub 1", 2L);
    }
    public void testGetAllSubtasks() {
        int actual = tasksManagerService.getAllSubtasks().size();
        int expected = 5;
        assertEquals(actual, expected);
    }

    public void testGetAllEpics() {
        int actual = tasksManagerService.getAllEpics().size();
        int expected = 2;
        assertEquals(actual, expected);
    }

    public void testGetEpicBySubtaskId() {
        long actual = tasksManagerService.getEpicBySubtaskId(3L).getId();
        long expected = 1L;
        assertEquals(actual, expected);
    }

    @Override
    protected void tearDown() throws Exception {

    }
}
