package service;

import constant.TaskStatus;
import entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

abstract class TasksManagerTest<T extends TasksManager> {
    private T taskManager;

    abstract T createTaskManager() throws NoSuchFieldException, IllegalAccessException, IOException;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException, IOException {
        taskManager = createTaskManager();
        taskManager.createNewEpic("Epic 1", "Desc 1");
        taskManager.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L, "2020-01-01 00:00", 40);
        taskManager.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L, "2020-01-01 01:00", 40);
        taskManager.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L, "2020-01-01 02:00", 40);
        taskManager.createNewEpic("Epic 2", "Desc 2");
        taskManager.createNewSubtask("Subtask 2.1", "Desc sub 2", 5L, "2020-01-01 03:00", 40);
        taskManager.createNewSubtask("Subtask 2.2", "Desc sub 2", 5L, "2020-01-01 04:00", 40);
    }

    @Test
    @DisplayName("Test get all subtasks, expected ok")
    public void testGetAllSubtasks() {
        int actual = taskManager.getAllSubtasks().size();
        int expected = 5;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test get all epics, expected ok")
    public void testGetAllEpics() {
        int actual = taskManager.getAllEpics().size();
        int expected = 2;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test return epic by subtasks id, expected ok")
    public void testGetEpicBySubtaskId() {
        long actual = taskManager.getEpicBySubtaskId(3L).getId();
        long expected = 1L;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test change subtasks status to InProgress, expected ok")
    public void testChangeSubtaskStatusInProgress() {
        taskManager.changeSubtaskStatusInProgress(2L);
        TaskStatus actual = taskManager.getEpicBySubtaskId(2L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test change subtasks status epic to InProgress, expected ok")
    public void testChangeSubtaskStatusEpicInProgress() {
        taskManager.changeSubtaskStatusInProgress(3L);
        TaskStatus actual = taskManager.getEpicById(1L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test change subtasks status to Done, expected ok")
    public void testChangeSubtaskStatusDone() {
        taskManager.changeSubtaskStatusDone(4L);
        TaskStatus actual = taskManager.getSubtaskById(4L).getStatus();
        TaskStatus expected = TaskStatus.DONE;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test change subtasks status Done, epic to InProgress, expected ok")
    public void testChangeSubtaskStatusEpicNotDone() {
        taskManager.changeSubtaskStatusDone(2L);
        TaskStatus actual = taskManager.getEpicById(1L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test change subtask status Done, epic to InProgress, expected ok")
    public void testChangeSubtaskStatusEpicDone() {
        taskManager.changeSubtaskStatusDone(6L);
        taskManager.changeSubtaskStatusDone(7L);
        taskManager.createNewSubtask("Subtask 2.3", "Desc sub 2", 5L);
        TaskStatus actual = taskManager.getEpicById(5L).getStatus();
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test change subtasks status Done, epic to Done, expected ok")
    public void testChangeEpicStatusIfCreateNewSubtask() {
        taskManager.changeSubtaskStatusDone(6L);
        taskManager.changeSubtaskStatusDone(7L);
        TaskStatus actual = taskManager.getEpicById(5L).getStatus();
        TaskStatus expected = TaskStatus.DONE;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test update epics name, expected ok")
    public void testUpdateEpicName() {
        taskManager.updateTaskName(1L, "New Name");
        String actual = taskManager.getEpicById(1L).getName();
        String expected = "New Name";
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test update epics description, expected ok")
    public void testUpdateEpicDescription() {
        taskManager.updateTaskDescription(5L, "New Description");
        String actual = taskManager.getEpicById(5L).getDescription();
        String expected = "New Description";
        assertEquals(actual, expected);
    }


    @Test
    @DisplayName("Test change epic status to Done after remove his subtask with status InProgress or New, expected ok")
    public void testChangeEpicStatusIfRemoveNotFinishSubtask() {
        taskManager.changeSubtaskStatusDone(6L);
        taskManager.removeSubtaskById(7L);
        TaskStatus actual = taskManager.getEpicById(5L).getStatus();
        TaskStatus expected = TaskStatus.DONE;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test remove all subtasks by epics id, expected ok")
    public void testRemoveSubtasksByEpicId() {
        taskManager.removeSubtasksByEpicId(1L);
        int actual = taskManager.getAllSubtasks().size();
        int expected = 2;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test remove epic, expected ok")
    public void testRemoveEpicById() {
        taskManager.removeEpicById(1L);
        int actual = taskManager.getAllEpics().size();
        int expected = 1;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test cascade remove subtasks, expected ok")
    public void testСascadeRemoveSubtask() {
        taskManager.removeEpicById(1L);
        int actual = taskManager.getAllSubtasks().size();
        int expected = 2;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test remove subtask, expected ok")
    public void testRemoveSubtaskById() {
        taskManager.removeSubtaskById(3L);
        int actual = taskManager.getAllSubtasks().size();
        int expected = 4;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test remove all subtask, expected ok")
    public void testRemoveAllSubtasks() {
        taskManager.removeAllSubtasks();
        int actual = taskManager.getAllSubtasks().size();
        int expected = 0;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test remove all epics, expected ok")
    public void testRemoveAllEpics() {
        taskManager.removeAllEpics();
        int actual = taskManager.getAllEpics().size();
        int expected = 0;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test creating history of inMemoryTaskManager, expected ok")
    public void testTasksQueue() {
        taskManager.getEpicById(1L);
        taskManager.getEpicById(5L);
        taskManager.getSubtaskById(3L);
        taskManager.getEpicById(5L);
        Task actual = taskManager.getHistory().get(taskManager.getHistory().size() - 1);
        Task expected = taskManager.getEpicById(1L);
        assertNotEquals(actual, expected);
    }

    @Test
    @DisplayName("Test of getting last task in history of inMemoryTaskManager, expected ok")
    public void testLastInHistoryTasksQueue() {
        taskManager.getEpicById(1L);
        taskManager.getEpicById(5L);
        taskManager.getEpicById(5L);
        Task actual = taskManager.getHistory().get(0);
        Task expected = taskManager.getEpicById(5L);
        assertNotEquals(actual, expected);
    }

    @Test
    @DisplayName("Test remove repeat tasks in history of inMemoryTaskManager, when in history only one task, expected ok")
    public void testSizeHistoryToOneTask() {
        taskManager.getSubtaskById(2L);
        taskManager.getSubtaskById(2L);
        taskManager.getSubtaskById(2L);
        taskManager.getSubtaskById(2L);
        taskManager.getSubtaskById(2L);
        int actual = taskManager.getHistory().size();
        int expected = 1;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test remove repeat tasks in history of inMemoryTaskManager, when in history many tasks, expected ok")
    public void testSizeHistoryToSameTasks() {
        taskManager.getEpicById(1L);
        taskManager.getEpicById(5L);
        taskManager.getEpicById(1L);
        taskManager.getEpicById(1L);
        taskManager.getEpicById(5L);
        taskManager.getEpicById(5L);
        taskManager.getEpicById(1L);
        taskManager.getSubtaskById(2L);
        taskManager.getSubtaskById(2L);
        int actual = taskManager.getHistory().size();
        int expected = 3;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test remove subtasks in history of inMemoryTaskManager after remove this subtask , expected ok")
    public void testRemoveHistoryAfterRemoveSubtask() {
        taskManager.getEpicById(1L);
        taskManager.getEpicById(5L);
        taskManager.getEpicById(1L);
        taskManager.getSubtaskById(2L);
        taskManager.getEpicById(1L);
        taskManager.getEpicById(5L);
        taskManager.getSubtaskById(2L);
        taskManager.getEpicById(5L);
        taskManager.getEpicById(1L);
        taskManager.removeSubtaskById(2L);
        int actual = taskManager.getHistory().size();
        int expected = 2;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test remove epic in history of inMemoryTaskManager after remove this epic , expected ok")
    public void testRemoveHistoryAfterRemoveEpic() {
        taskManager.getEpicById(1L);
        taskManager.getEpicById(5L);
        taskManager.getEpicById(1L);
        taskManager.getSubtaskById(2L);
        taskManager.getEpicById(1L);
        taskManager.getEpicById(5L);
        taskManager.getSubtaskById(2L);
        taskManager.getEpicById(5L);
        taskManager.getEpicById(1L);
        taskManager.removeEpicById(1L);
        int actual = taskManager.getHistory().size();
        int expected = 1;
        assertEquals(actual, expected);
    }

}