package service;

import constant.TaskStatus;
import constant.TypeTasksManager;
import entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private TasksManager inMemoryTaskManager;

//    @BeforeEach
//    public void setUp() {
//        inMemoryTaskManager = Manager.getTaskManager(TypeTasksManager.IN_MEMORY_TASKS_MANAGER);
//        inMemoryTaskManager.createNewEpic("Epic 1", "Desc 1");
//        inMemoryTaskManager.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L);
//        inMemoryTaskManager.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L);
//        inMemoryTaskManager.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L);
//        inMemoryTaskManager.createNewEpic("Epic 2", "Desc 2");
//        inMemoryTaskManager.createNewSubtask("Subtask 2.1", "Desc sub 2", 5L);
//        inMemoryTaskManager.createNewSubtask("Subtask 2.2", "Desc sub 2", 5L);
//    }
//
//    //TODO разобраться с тестами
//    @Test
//    @DisplayName("Test get all subtasks, expected ok")
//    public void testGetAllSubtasks() {
//        int actual = inMemoryTaskManager.getAllSubtasks().size();
//        int expected = 5;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test get all epics, expected ok")
//    public void testGetAllEpics() {
//        int actual = inMemoryTaskManager.getAllEpics().size();
//        int expected = 2;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test return epic by subtasks id, expected ok")
//    public void testGetEpicBySubtaskId() {
//        long actual = inMemoryTaskManager.getEpicBySubtaskIdOrNull(3L).getId();
//        long expected = 1L;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test change subtasks status to InProgress, expected ok")
//    public void testChangeSubtaskStatusInProgress() {
//        inMemoryTaskManager.changeSubtaskStatusInProgress(2L);
//        TaskStatus actual = inMemoryTaskManager.getSubtaskByIdOrNull(2L).getStatus();
//        TaskStatus expected = TaskStatus.IN_PROGRESS;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test change subtasks status epic to InProgress, expected ok")
//    public void testChangeSubtaskStatusEpicInProgress() {
//        inMemoryTaskManager.changeSubtaskStatusInProgress(3L);
//        TaskStatus actual = inMemoryTaskManager.getEpicById(1L).getStatus();
//        TaskStatus expected = TaskStatus.IN_PROGRESS;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test change subtasks status to Done, expected ok")
//    public void testChangeSubtaskStatusDone() {
//        inMemoryTaskManager.changeSubtaskStatusDone(4L);
//        TaskStatus actual = inMemoryTaskManager.getSubtaskByIdOrNull(4L).getStatus();
//        TaskStatus expected = TaskStatus.DONE;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test change subtasks status Done, epic to InProgress, expected ok")
//    public void testChangeSubtaskStatusEpicNotDone() {
//        inMemoryTaskManager.changeSubtaskStatusDone(2L);
//        TaskStatus actual = inMemoryTaskManager.getEpicById(1L).getStatus();
//        TaskStatus expected = TaskStatus.IN_PROGRESS;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test change subtask status Done, epic to InProgress, expected ok")
//    public void testChangeSubtaskStatusEpicDone() {
//        inMemoryTaskManager.changeSubtaskStatusDone(6L);
//        inMemoryTaskManager.changeSubtaskStatusDone(7L);
//        inMemoryTaskManager.createNewSubtask("Subtask 2.3", "Desc sub 2", 5L);
//        TaskStatus actual = inMemoryTaskManager.getEpicById(5L).getStatus();
//        TaskStatus expected = TaskStatus.IN_PROGRESS;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test change subtasks status Done, epic to Done, expected ok")
//    public void testChangeEpicStatusIfCreateNewSubtask() {
//        inMemoryTaskManager.changeSubtaskStatusDone(6L);
//        inMemoryTaskManager.changeSubtaskStatusDone(7L);
//        TaskStatus actual = inMemoryTaskManager.getEpicById(5L).getStatus();
//        TaskStatus expected = TaskStatus.DONE;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test update epics name, expected ok")
//    public void testUpdateEpicName() {
//        inMemoryTaskManager.updateEpicName(1L, "New Name");
//        String actual = inMemoryTaskManager.getEpicById(1L).getName();
//        String expected = "New Name";
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test update epics description, expected ok")
//    public void testUpdateEpicDescription() {
//        inMemoryTaskManager.updateEpicDescription(5L, "New Description");
//        String actual = inMemoryTaskManager.getEpicById(5L).getDescription();
//        String expected = "New Description";
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test update subtask name, expected ok")
//    public void testUpdateSubtaskName() {
//        inMemoryTaskManager.updateSubtaskName(3L, "New Name");
//        String actual = inMemoryTaskManager.getSubtaskByIdOrNull(3L).getName();
//        String expected = "New Name";
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test update subtask description, expected ok")
//    public void testUpdateSubtaskDescription() {
//        inMemoryTaskManager.updateSubtaskDescription(4L, "New Description");
//        String actual = inMemoryTaskManager.getSubtaskByIdOrNull(4L).getDescription();
//        String expected = "New Description";
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test change epic status to Done after remove his subtask with status InProgress or New, expected ok")
//    public void testChangeEpicStatusIfRemoveNotFinishSubtask() {
//        inMemoryTaskManager.changeSubtaskStatusDone(6L);
//        inMemoryTaskManager.removeSubtasksById(7L);
//        TaskStatus actual = inMemoryTaskManager.getEpicById(5L).getStatus();
//        TaskStatus expected = TaskStatus.DONE;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test remove all subtasks by epics id, expected ok")
//    public void testRemoveSubtasksByEpicId() {
//        inMemoryTaskManager.removeSubtasksByEpicId(1L);
//        int actual = inMemoryTaskManager.getAllSubtasks().size();
//        int expected = 2;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test remove epic, expected ok")
//    public void testRemoveEpicById() {
//        inMemoryTaskManager.removeEpicById(1L);
//        int actual = inMemoryTaskManager.getAllEpics().size();
//        int expected = 1;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test cascade remove subtasks, expected ok")
//    public void testСascadeRemoveSubtask() {
//        inMemoryTaskManager.removeEpicById(1L);
//        int actual = inMemoryTaskManager.getAllSubtasks().size();
//        int expected = 2;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test remove subtask, expected ok")
//    public void testRemoveSubtaskById() {
//        inMemoryTaskManager.removeSubtasksById(3L);
//        int actual = inMemoryTaskManager.getAllSubtasks().size();
//        int expected = 4;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test remove all subtask, expected ok")
//    public void testRemoveAllSubtasks() {
//        inMemoryTaskManager.removeAllSubtasks();
//        int actual = inMemoryTaskManager.getAllSubtasks().size();
//        int expected = 0;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test remove all epics, expected ok")
//    public void testRemoveAllEpics() {
//        inMemoryTaskManager.removeAllEpics();
//        int actual = inMemoryTaskManager.getAllEpics().size();
//        int expected = 0;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test creating history of inMemoryTaskManager, expected ok")
//    public void testTasksQueue() {
//        inMemoryTaskManager.getEpicById(1L);
//        inMemoryTaskManager.getEpicById(5L);
//        inMemoryTaskManager.getSubtaskByIdOrNull(3L);
//        inMemoryTaskManager.getEpicById(5L);
//        Task actual = inMemoryTaskManager.getHistory().get(inMemoryTaskManager.getHistory().size() - 1);
//        Task expected = inMemoryTaskManager.getEpicById(1L);
//        assertNotEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test of getting last task in history of inMemoryTaskManager, expected ok")
//    public void testLastInHistoryTasksQueue() {
//        inMemoryTaskManager.getEpicById(1L);
//        inMemoryTaskManager.getEpicById(5L);
//        inMemoryTaskManager.getEpicById(5L);
//        Task actual = inMemoryTaskManager.getHistory().get(0);
//        Task expected = inMemoryTaskManager.getEpicById(5L);
//        assertNotEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test remove repeat tasks in history of inMemoryTaskManager, when in history only one task, expected ok")
//    public void testSizeHistoryToOneTask() {
//        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
//        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
//        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
//        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
//        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
//        int actual = inMemoryTaskManager.getHistory().size();
//        int expected = 1;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test remove repeat tasks in history of inMemoryTaskManager, when in history many tasks, expected ok")
//    public void testSizeHistoryToSameTasks() {
//        inMemoryTaskManager.getEpicById(1L);
//        inMemoryTaskManager.getEpicById(5L);
//        inMemoryTaskManager.getEpicById(1L);
//        inMemoryTaskManager.getEpicById(1L);
//        inMemoryTaskManager.getEpicById(5L);
//        inMemoryTaskManager.getEpicById(5L);
//        inMemoryTaskManager.getEpicById(1L);
//        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
//        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
//        int actual = inMemoryTaskManager.getHistory().size();
//        int expected = 3;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test remove subtasks in history of inMemoryTaskManager after remove this subtask , expected ok")
//    public void testRemoveHistoryAfterRemoveSubtask() {
//        inMemoryTaskManager.getEpicById(1L);
//        inMemoryTaskManager.getEpicById(5L);
//        inMemoryTaskManager.getEpicById(1L);
//        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
//        inMemoryTaskManager.getEpicById(1L);
//        inMemoryTaskManager.getEpicById(5L);
//        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
//        inMemoryTaskManager.getEpicById(5L);
//        inMemoryTaskManager.getEpicById(1L);
//        inMemoryTaskManager.removeSubtasksById(2L);
//        int actual = inMemoryTaskManager.getHistory().size();
//        int expected = 2;
//        assertEquals(actual, expected);
//    }
//
//    @Test
//    @DisplayName("Test remove epic in history of inMemoryTaskManager after remove this epic , expected ok")
//    public void testRemoveHistoryAfterRemoveEpic() {
//        inMemoryTaskManager.getEpicById(1L);
//        inMemoryTaskManager.getEpicById(5L);
//        inMemoryTaskManager.getEpicById(1L);
//        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
//        inMemoryTaskManager.getEpicById(1L);
//        inMemoryTaskManager.getEpicById(5L);
//        inMemoryTaskManager.getSubtaskByIdOrNull(2L);
//        inMemoryTaskManager.getEpicById(5L);
//        inMemoryTaskManager.getEpicById(1L);
//        inMemoryTaskManager.removeEpicById(1L);
//        int actual = inMemoryTaskManager.getHistory().size();
//        int expected = 1;
//        assertEquals(actual, expected);
//    }
}
