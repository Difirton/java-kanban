package utill;

import constant.TypeTasksManager;
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
        tasksManager.createNewSubtask("Subtask 8", "Subtask 8", 7L, "2010-12-10 19:20", 900);
        tasksManager.createNewSubtask("Subtask 9", "Subtask 9", 2L, "2008-12-10 22:20", 6000);
        tasksManager.createNewSubtask("Subtask 10", "Subtask 10", 2L, "2004-10-10 22:20", 60);
        tasksManager.createNewSubtask("Subtask 11", "Subtask 11", 2L, "2004-11-10 22:20", 60);
        tasksManager.createNewSubtask("Subtask 12", "Subtask 12", 2L, "2016-11-10 22:20", 60);
        tasksManager.createNewSubtask("Subtask 13", "Subtask 13", 2L, "2012-11-10 22:20", 60);
        tasksManager.createNewSubtask("Subtask 13", "Subtask 13", 2L, "2012-11-10 22:50", 60);
    }

    @Test
    @DisplayName("Test interval duplication test, if two intervals intersect, expected ok")
    void testNotAddNewSubtaskToMilleIfIntervalIsDuplication() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2012-11-10 22:30", 60);
        int expectedSize = 9;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test interval duplication test, if two intervals not intersect and previous is intersect, expected ok")
    void testAddNewSubtaskToMilleIfIntervalIsNotDuplication() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2012-11-10 23:30", 60);
        int expectedSize = 10;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test intersect to the start collection, expected ok")
    void testAddNewSubtaskToStartIfIntervalIsNotDuplication() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2004-10-10 20:20", 60);
        int expectedSize = 10;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test interval duplication test, if two intervals intersect in the start collection, expected ok")
    void testNotAddNewSubtaskToStartIfIntervalIsDuplicationInsertElementForward() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2004-10-10 22:10", 60);
        int expectedSize = 9;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test interval duplication test, if two intervals intersect in the start collection, expected ok")
    void testNotAddNewSubtaskToStartIfIntervalIsDuplicationInsertElementBehind() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2004-10-10 22:30", 60);
        int expectedSize = 9;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test intersect to the finish collection, expected ok")
    void testAddNewSubtaskToFinishIfIntervalIsNotDuplication() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2016-12-10 22:30", 60);
        int expectedSize = 10;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test interval duplication test, if two intervals intersect in the start collection, expected ok")
    void testNotAddNewSubtaskToFinishIfIntervalIsDuplicationInsertElementForward() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2016-11-10 21:50", 60);
        int expectedSize = 9;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }

    @Test
    @DisplayName("Test interval duplication test, if two intervals intersect in the start collection, expected ok")
    void testNotAddNewSubtaskToFinishIfIntervalIsDuplicationInsertElementBehind() {
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 2L, "2016-11-10 22:40", 60);
        int expectedSize = 9;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }
}