import constant.TaskStatus;
import entity.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import service.Manager;
import service.TasksManager;

public class InMemoryTaskManagerTest {
    TasksManager inMemoryTaskManager;

    @Before
    public void setUp() throws Exception {
        inMemoryTaskManager = Manager.getDefault();
        inMemoryTaskManager.createNewEpic("Epic 1", "Desc 1");
        inMemoryTaskManager.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L);
        inMemoryTaskManager.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L);
        inMemoryTaskManager.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L);
        inMemoryTaskManager.createNewEpic("Epic 2", "Desc 2");
        inMemoryTaskManager.createNewSubtask("Subtask 2.1", "Desc sub 2", 5L);
        inMemoryTaskManager.createNewSubtask("Subtask 2.2", "Desc sub 2", 5L);
    }

    @Test
    public void testGetAllSubtasks() {
        int actual = inMemoryTaskManager.getAllSubtasks().size();
        int expected = 5;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetAllEpics() {
        int actual = inMemoryTaskManager.getAllEpics().size();
        int expected = 2;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetEpicBySubtaskId() {
        long actual = inMemoryTaskManager.getEpicBySubtaskIdOrNull(3L).getId();
        long expected = 1L;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeSubtaskStatusInProgress() {
        inMemoryTaskManager.changeSubtaskStatusInProgress(2L);
        TaskStatus actual = inMemoryTaskManager.getSubtaskByIdOrNull(2L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeSubtaskStatusEpicInProgress() {
        inMemoryTaskManager.changeSubtaskStatusInProgress(3L);
        TaskStatus actual = inMemoryTaskManager.getEpicById(1L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeSubtaskStatusDone() {
        inMemoryTaskManager.changeSubtaskStatusDone(4L);
        TaskStatus actual = inMemoryTaskManager.getSubtaskByIdOrNull(4L).getStatus();
        TaskStatus expected = TaskStatus.DONE;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeSubtaskStatusEpicNotDone() {
        inMemoryTaskManager.changeSubtaskStatusDone(2L);
        TaskStatus actual = inMemoryTaskManager.getEpicById(1L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeSubtaskStatusEpicDone() {
        inMemoryTaskManager.changeSubtaskStatusDone(6L);
        inMemoryTaskManager.changeSubtaskStatusDone(7L);
        inMemoryTaskManager.createNewSubtask("Subtask 2.3", "Desc sub 2", 5L);
        TaskStatus actual = inMemoryTaskManager.getEpicById(5L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeEpicStatusIfCreateNewSubtask() {
        inMemoryTaskManager.changeSubtaskStatusDone(6L);
        inMemoryTaskManager.changeSubtaskStatusDone(7L);
        TaskStatus actual = inMemoryTaskManager.getEpicById(5L).getStatus();
        TaskStatus expected = TaskStatus.DONE;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testUpdateEpicName() {
        inMemoryTaskManager.updateEpicName(1L, "New Name");
        String actual = inMemoryTaskManager.getEpicById(1L).getName();
        String expected = "New Name";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testUpdateEpicDescription() {
        inMemoryTaskManager.updateEpicDescription(5L, "New Description");
        String actual = inMemoryTaskManager.getEpicById(5L).getDescription();
        String expected = "New Description";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testUpdateSubtaskName() {
        inMemoryTaskManager.updateSubtaskName(3L, "New Name");
        String actual = inMemoryTaskManager.getSubtaskByIdOrNull(3L).getName();
        String expected = "New Name";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testUpdateSubtaskDescription() {
        inMemoryTaskManager.updateSubtaskDescription(4L, "New Description");
        String actual = inMemoryTaskManager.getSubtaskByIdOrNull(4L).getDescription();
        String expected = "New Description";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeEpicStatusIfRemoveNotFinishSubtask() {
        inMemoryTaskManager.changeSubtaskStatusDone(6L);
        inMemoryTaskManager.removeSubtasksById(7L);
        TaskStatus actual = inMemoryTaskManager.getEpicById(5L).getStatus();
        TaskStatus expected = TaskStatus.DONE;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testRemoveSubtasksByEpicId() {
        inMemoryTaskManager.removeSubtasksByEpicId(1L);
        int actual = inMemoryTaskManager.getAllSubtasks().size();
        int expected = 2;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testRemoveEpicById() {
        inMemoryTaskManager.removeEpicById(1L);
        int actual = inMemoryTaskManager.getAllEpics().size();
        int expected = 1;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testСascadeRemoveSubtask() {
        inMemoryTaskManager.removeEpicById(1L);
        int actual = inMemoryTaskManager.getAllSubtasks().size();
        int expected = 2;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testRemoveSubtaskById() {
        inMemoryTaskManager.removeSubtasksById(3L);
        int actual = inMemoryTaskManager.getAllSubtasks().size();
        int expected = 4;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testRemoveAllSubtasks() {
        inMemoryTaskManager.removeAllSubtasks();
        int actual = inMemoryTaskManager.getAllSubtasks().size();
        int expected = 0;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testRemoveAllEpics() {
        inMemoryTaskManager.removeAllEpics();
        int actual = inMemoryTaskManager.getAllEpics().size();
        int expected = 0;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testTasksQueue() {
        inMemoryTaskManager.getEpicById(1L);
        inMemoryTaskManager.getEpicById(5L);
        inMemoryTaskManager.getSubtaskByIdOrNull(3L);
        inMemoryTaskManager.getEpicById(5L);
        Task actual = inMemoryTaskManager.getHistory().get(inMemoryTaskManager.getHistory().size() - 1);
        Task expected = inMemoryTaskManager.getEpicById(1L);
        Assert.assertNotEquals(actual, expected);
    }

    @Test
    public void testLastInHistoryTasksQueue() {
        inMemoryTaskManager.getEpicById(1L);
        inMemoryTaskManager.getEpicById(5L);
        inMemoryTaskManager.getEpicById(5L);
        Task actual = inMemoryTaskManager.getHistory().get(0);
        Task expected = inMemoryTaskManager.getEpicById(5L);
        Assert.assertNotEquals(actual, expected);
    }

    @Test
    public void testSizeHistoryToOneTask() {
        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
        int actual = inMemoryTaskManager.getHistory().size();
        int expected = 1;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testSizeHistoryToSameTasks() {
        inMemoryTaskManager.getEpicById(1L);
        inMemoryTaskManager.getEpicById(5L);
        inMemoryTaskManager.getEpicById(1L);
        inMemoryTaskManager.getEpicById(1L);
        inMemoryTaskManager.getEpicById(5L);
        inMemoryTaskManager.getEpicById(5L);
        inMemoryTaskManager.getEpicById(1L);
        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
        int actual = inMemoryTaskManager.getHistory().size();
        int expected = 3;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testRemoveHistoryAfterRemoveSubtask() {
        inMemoryTaskManager.getEpicById(1L);
        inMemoryTaskManager.getEpicById(5L);
        inMemoryTaskManager.getEpicById(1L);
        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
        inMemoryTaskManager.getEpicById(1L);
        inMemoryTaskManager.getEpicById(5L);
        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
        inMemoryTaskManager.getEpicById(5L);
        inMemoryTaskManager.getEpicById(1L);
        inMemoryTaskManager.removeSubtasksById(2L);
        int actual = inMemoryTaskManager.getHistory().size();
        int expected = 2;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testRemoveHistoryAfterRemoveEpic() {
        inMemoryTaskManager.getEpicById(1L);
        inMemoryTaskManager.getEpicById(5L);
        inMemoryTaskManager.getEpicById(1L);
        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
        inMemoryTaskManager.getEpicById(1L);
        inMemoryTaskManager.getEpicById(5L);
        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
        inMemoryTaskManager.getEpicById(5L);
        inMemoryTaskManager.getEpicById(1L);
        inMemoryTaskManager.removeEpicById(1L);
        int actual = inMemoryTaskManager.getHistory().size();
        int expected = 1;
        Assert.assertEquals(actual, expected);
    }
}
