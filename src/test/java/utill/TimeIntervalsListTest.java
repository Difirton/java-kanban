package utill;

import constant.TypeTasksManager;
import entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Manager;
import service.TasksManager;

import static org.junit.jupiter.api.Assertions.*;

class TimeIntervalsListTest {
    TasksManager tasksManager;

    @BeforeEach
    public void setUp() {
        tasksManager = Manager.getTaskManager(TypeTasksManager.IN_MEMORY_TASKS_MANAGER);
        tasksManager.createNewEpic("Epic 1", "Description Epic 1");
        tasksManager.createNewEpic("Epic 2", "Description Epic 2");
        tasksManager.createNewSubtask("Subtask 3", "Subtask 3", 1L, "2005-12-10 12:20", 40 );
        tasksManager.createNewSubtask("Subtask 4", "Subtask 4", 1L, "2005-10-10 12:20", 60 );
        tasksManager.createNewSubtask("Subtask 5", "Subtask 5", 1L, "2005-08-16 12:20", 650);
        tasksManager.createNewSubtask("Subtask 6", "Subtask 6", 2L, "2005-08-16 12:30", 660);
        tasksManager.createNewEpic("Epic 7", "Description Epic 7");
        tasksManager.createNewSubtask("Subtask 8", "Subtask 8", 7L, "2010-12-10 19:20", 90);
        tasksManager.createNewSubtask("Subtask 9", "Subtask 9", 2L, "2008-12-10 22:20", 60);
        tasksManager.createNewSubtask("Subtask 10", "Subtask 10", 2L, "2004-10-10 22:20", 60);
        tasksManager.createNewSubtask("Subtask 11", "Subtask 11", 2L, "2004-11-10 22:20", 60);
        tasksManager.createNewSubtask("Subtask 12", "Subtask 12", 2L, "2016-11-10 22:20", 60);
        tasksManager.createNewSubtask("Subtask 13", "Subtask 13", 2L, "2012-11-10 22:20", 60);
    }

    @Test
    @DisplayName("Test interval duplication test, if two intervals intersect, expected ok")
    public void testNotAddNewSubtaskToMilleIfIntervalIsDuplication() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2012-11-10 22:30", 60);
        int expectedSize = 9;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test interval duplication test, if two intervals not intersect and previous is intersect, expected ok")
    public void testAddNewSubtaskToMilleIfIntervalIsNotDuplication() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2012-11-10 23:30", 60);
        int expectedSize = 10;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test intersect to the start collection, expected ok")
    public void testAddNewSubtaskToStartIfIntervalIsNotDuplication() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2004-10-10 20:20", 60);
        int expectedSize = 10;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test interval duplication test, if two intervals intersect in the start collection, expected ok")
    public void testNotAddNewSubtaskToStartIfIntervalIsDuplicationInsertElementForward() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2004-10-10 22:10", 60);
        int expectedSize = 9;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test interval duplication test, if two intervals intersect in the start collection, expected ok")
    public void testNotAddNewSubtaskToStartIfIntervalIsDuplicationInsertElementBehind() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2004-10-10 22:30", 60);
        int expectedSize = 9;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test intersect to the finish collection, expected ok")
    public void testAddNewSubtaskToFinishIfIntervalIsNotDuplication() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2016-12-10 22:30", 60);
        int expectedSize = 10;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test interval duplication test, if two intervals intersect in the start collection, expected ok")
    public void testNotAddNewSubtaskToFinishIfIntervalIsDuplicationInsertElementForward() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2016-11-10 21:50", 60);
        int expectedSize = 9;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test interval duplication test, if two intervals intersect in the start collection, expected ok")
    public void testNotAddNewSubtaskToFinishIfIntervalIsDuplicationInsertElementBehind() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2016-11-10 22:40", 60);
        int expectedSize = 9;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test add new subtask with default time params, expected ok")
    public void testAddNewSubtaskWithDefaultTimeParams() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L);
        int expectedSize = 10;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test add three new subtask with default time params, expected ok")
    public void testAddThreeNewSubtaskWithDefaultTimeParams() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L);
        tasksManager.createNewSubtask("Subtask 15", "Subtask 15", 2L);
        tasksManager.createNewSubtask("Subtask 16", "Subtask 16", 1L);
        int expectedSize = 12;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Time matching test of the earliest task in the list, expected ok")
    public void testTimeMatchingOfEarliestTaskInList() {
        final int INDEX_FIRST_ITEM_WITH_HIGHEST_PRIORITY = 0;
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2000-01-10 23:30", 60);
        Task expectedTask = tasksManager.getSubtaskById(14L);
        Task actualTask = tasksManager.getPrioritizedTasks().get(INDEX_FIRST_ITEM_WITH_HIGHEST_PRIORITY);
        assertEquals(actualTask, expectedTask);
    }

    @Test
    @DisplayName("Time matching test of the latest task in the list, expected ok")
    public void testTimeMatchingOfLatestTaskInList() {
        final int INDEX_LAST_ITEM_WITH_LEAST_PRIORITY = tasksManager.getPrioritizedTasks().size();
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2022-01-10 23:30", 60);
        Task expectedTask = tasksManager.getSubtaskById(14L);
        Task actualTask = tasksManager.getPrioritizedTasks().get(INDEX_LAST_ITEM_WITH_LEAST_PRIORITY);
        assertEquals(actualTask, expectedTask);
    }

    @Test
    @DisplayName("When deleting a subtask, the time interval should become free, expected ok")
    public void testDeleteTimeInterval() throws NoSuchFieldException, IllegalAccessException {
        tasksManager.removeSubtaskById(3L);
        tasksManager.createNewSubtask("Subtask 3", "Subtask 3", 1L, "2005-12-10 12:20", 40 );
        int expectedSize = 9;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }
}