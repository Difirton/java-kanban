import constant.TaskStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import service.TasksManagerService;

public class TasksManagerServiceTest {
    TasksManagerService tasksManagerService;

    @Before
    public void setUp() throws Exception {
        tasksManagerService = new TasksManagerService();
        tasksManagerService.createNewEpic("Epic 1", "Desc 1");
        tasksManagerService.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L);
        tasksManagerService.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L);
        tasksManagerService.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L);
        tasksManagerService.createNewEpic("Epic 2", "Desc 2");
        tasksManagerService.createNewSubtask("Subtask 2.1", "Desc sub 2", 2L);
        tasksManagerService.createNewSubtask("Subtask 2.2", "Desc sub 2", 2L);
    }

    @Test
    public void testGetAllSubtasks() {
        int actual = tasksManagerService.getAllSubtasks().size();
        int expected = 5;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetAllEpics() {
        int actual = tasksManagerService.getAllEpics().size();
        int expected = 2;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetEpicBySubtaskId() {
        long actual = tasksManagerService.getEpicBySubtaskIdOrNull(3L).getId();
        long expected = 1L;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeSubtaskStatusInProgress() {
        tasksManagerService.changeSubtaskStatusInProgress(1L);
        TaskStatus actual = tasksManagerService.getSubtaskByIdOrNull(1L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeSubtaskStatusEpicInProgress() {
        tasksManagerService.changeSubtaskStatusInProgress(1L);
        TaskStatus actual = tasksManagerService.getEpicById(1L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeSubtaskStatusDone() {
        tasksManagerService.changeSubtaskStatusDone(1L);
        TaskStatus actual = tasksManagerService.getSubtaskByIdOrNull(1L).getStatus();
        TaskStatus expected = TaskStatus.DONE;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeSubtaskStatusEpicNotDone() {
        tasksManagerService.changeSubtaskStatusDone(1L);
        TaskStatus actual = tasksManagerService.getEpicById(1L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeSubtaskStatusEpicDone() {
        tasksManagerService.changeSubtaskStatusDone(4L);
        tasksManagerService.changeSubtaskStatusDone(5L);
        tasksManagerService.createNewSubtask("Subtask 2.3", "Desc sub 2", 2L);
        TaskStatus actual = tasksManagerService.getEpicById(2L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeEpicStatusIfCreateNewSubtask() {
        tasksManagerService.changeSubtaskStatusDone(4L);
        tasksManagerService.changeSubtaskStatusDone(5L);
        TaskStatus actual = tasksManagerService.getEpicById(2L).getStatus();
        TaskStatus expected = TaskStatus.DONE;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testUpdateEpicName() {
        tasksManagerService.updateEpicName(1L, "New Name");
        String actual = tasksManagerService.getEpicById(1L).getName();
        String expected = "New Name";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testUpdateEpicDescription() {
        tasksManagerService.updateEpicDescription(2L, "New Description");
        String actual = tasksManagerService.getEpicById(2L).getDescription();
        String expected = "New Description";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testUpdateSubtaskName() {
        tasksManagerService.updateSubtaskName(3L, "New Name");
        String actual = tasksManagerService.getSubtaskByIdOrNull(3L).getName();
        String expected = "New Name";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testUpdateSubtaskDescription() {
        tasksManagerService.updateSubtaskDescription(4L, "New Description");
        String actual = tasksManagerService.getSubtaskByIdOrNull(4L).getDescription();
        String expected = "New Description";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testRemoveSubtasksByEpicId() {
        tasksManagerService.removeSubtasksByEpicId(1L);
        int actual = tasksManagerService.getAllSubtasks().size();
        int expected = 2;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testRemoveEpic() {
        tasksManagerService.removeEpic(1L);
        int actual = tasksManagerService.getAllEpics().size();
        int expected = 1;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testСascadeRemoveSubtask() {
        tasksManagerService.removeEpic(1L);
        int actual = tasksManagerService.getAllSubtasks().size();
        int expected = 2;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testRemoveSubtaskById() {
        tasksManagerService.removeSubtasksById(1L);
        int actual = tasksManagerService.getAllSubtasks().size();
        int expected = 4;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testRemoveAllSubtasks() {
        tasksManagerService.removeAllSubtasks();
        int actual = tasksManagerService.getAllSubtasks().size();
        int expected = 0;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testRemoveAllEpics() {
        tasksManagerService.removeAllEpics();
        int actual = tasksManagerService.getAllEpics().size();
        int expected = 0;
        Assert.assertEquals(actual, expected);
    }
}
