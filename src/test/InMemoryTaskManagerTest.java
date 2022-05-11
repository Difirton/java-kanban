import constant.TaskManagerType;
import constant.TaskStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import service.InMemoryTaskManager;
import utill.Manager;
import utill.TasksManager;

public class InMemoryTaskManagerTest {
    TasksManager inMemoryTaskManager;

    @Before
    public void setUp() throws Exception {
        inMemoryTaskManager = Manager.getDefault(TaskManagerType.MANAGER_OF_EPIC_AND_SUBTASK);
        inMemoryTaskManager.createNewEpic("Epic 1", "Desc 1");
        inMemoryTaskManager.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L);
        inMemoryTaskManager.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L);
        inMemoryTaskManager.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L);
        inMemoryTaskManager.createNewEpic("Epic 2", "Desc 2");
        inMemoryTaskManager.createNewSubtask("Subtask 2.1", "Desc sub 2", 2L);
        inMemoryTaskManager.createNewSubtask("Subtask 2.2", "Desc sub 2", 2L);
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
        inMemoryTaskManager.changeSubtaskStatusInProgress(1L);
        TaskStatus actual = inMemoryTaskManager.getSubtaskByIdOrNull(1L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeSubtaskStatusEpicInProgress() {
        inMemoryTaskManager.changeSubtaskStatusInProgress(1L);
        TaskStatus actual = inMemoryTaskManager.getEpicById(1L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeSubtaskStatusDone() {
        inMemoryTaskManager.changeSubtaskStatusDone(1L);
        TaskStatus actual = inMemoryTaskManager.getSubtaskByIdOrNull(1L).getStatus();
        TaskStatus expected = TaskStatus.DONE;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeSubtaskStatusEpicNotDone() {
        inMemoryTaskManager.changeSubtaskStatusDone(1L);
        TaskStatus actual = inMemoryTaskManager.getEpicById(1L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeSubtaskStatusEpicDone() {
        inMemoryTaskManager.changeSubtaskStatusDone(4L);
        inMemoryTaskManager.changeSubtaskStatusDone(5L);
        inMemoryTaskManager.createNewSubtask("Subtask 2.3", "Desc sub 2", 2L);
        TaskStatus actual = inMemoryTaskManager.getEpicById(2L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testChangeEpicStatusIfCreateNewSubtask() {
        inMemoryTaskManager.changeSubtaskStatusDone(4L);
        inMemoryTaskManager.changeSubtaskStatusDone(5L);
        TaskStatus actual = inMemoryTaskManager.getEpicById(2L).getStatus();
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
        inMemoryTaskManager.updateEpicDescription(2L, "New Description");
        String actual = inMemoryTaskManager.getEpicById(2L).getDescription();
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
        inMemoryTaskManager.changeSubtaskStatusDone(4L);
        inMemoryTaskManager.removeSubtasksById(5L);
        TaskStatus actual = inMemoryTaskManager.getEpicById(2L).getStatus();
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
        inMemoryTaskManager.removeSubtasksById(1L);
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
}
