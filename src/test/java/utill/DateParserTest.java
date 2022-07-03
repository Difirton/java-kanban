package utill;

import constant.TypeTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Manager;
import service.TasksManager;

import static org.junit.jupiter.api.Assertions.*;

class DateParserTest {
    TasksManager tasksManager;

    @BeforeEach
    public void setUp() {
        tasksManager = Manager.getTaskManager(TypeTasksManager.IN_MEMORY_TASKS_MANAGER);
        tasksManager.createNewEpic("Epic 1", "Description Epic 1");
    }

    @Test
    @DisplayName("Test date parsing test in various formats, expected ok")
    void testNotAddNewSubtaskToMilleIfIntervalIsDuplication() {
        tasksManager.createNewSubtask("Subtask 1", "Subtask 1", 1L, "2005-01-01 01:00", 40 );
        tasksManager.createNewSubtask("Subtask 2", "Subtask 2", 1L, "2005/01/01 02:00", 40 );
        tasksManager.createNewSubtask("Subtask 3", "Subtask 3", 1L, "2005.01.01 03:00", 40 );
        tasksManager.createNewSubtask("Subtask 4", "Subtask 4", 1L, "2005-01-1 04:00", 40 );
        tasksManager.createNewSubtask("Subtask 5", "Subtask 5", 1L, "2005/01/1 05:00", 40 );
        tasksManager.createNewSubtask("Subtask 6", "Subtask 6", 1L, "2005.01.1 06:00", 40 );
        tasksManager.createNewSubtask("Subtask 7", "Subtask 7", 1L, "2005-1-01 07:00", 40 );
        tasksManager.createNewSubtask("Subtask 8", "Subtask 8", 1L, "2005/1/01 08:00", 40 );
        tasksManager.createNewSubtask("Subtask 9", "Subtask 9", 1L, "01.01.2005 09:00", 40 );
        tasksManager.createNewSubtask("Subtask 10", "Subtask 10", 1L, "01-01-2005 10:00", 40 );
        tasksManager.createNewSubtask("Subtask 11", "Subtask 11", 1L, "02/01/05 01:00", 40 );
        tasksManager.createNewSubtask("Subtask 12", "Subtask 12", 1L, "02-01-05 02:00", 40 );
        tasksManager.createNewSubtask("Subtask 13", "Subtask 13", 1L, "02.01.05 03:00", 40 );
        tasksManager.createNewSubtask("Subtask 14", "Subtask 14", 1L, "2.01.2005 04:00", 40 );
        tasksManager.createNewSubtask("Subtask 15", "Subtask 15", 1L, "2/01/2005 05:00", 40 );
        tasksManager.createNewSubtask("Subtask 16", "Subtask 16", 1L, "2-01-2005 06:00", 40 );
        tasksManager.createNewSubtask("Subtask 17", "Subtask 17", 1L, "2.1.2005 07:00", 40 );
        tasksManager.createNewSubtask("Subtask 18", "Subtask 18", 1L, "2/1/2005 08:00", 40 );
        tasksManager.createNewSubtask("Subtask 19", "Subtask 19", 1L, "2-1-2005 09:00", 40 );
        tasksManager.createNewSubtask("Subtask 20", "Subtask 20", 1L, "02.1.2005 10:00", 40 );
        tasksManager.createNewSubtask("Subtask 21", "Subtask 21", 1L, "02-1-2005 11:00", 40 );
        tasksManager.createNewSubtask("Subtask 22", "Subtask 22", 1L, "02/1/2005 12:00", 40 );
        tasksManager.createNewSubtask("Subtask 23", "Subtask 23", 1L, "02.1.05 13:00", 40 );
        tasksManager.createNewSubtask("Subtask 24", "Subtask 24", 1L, "02/1/05 14:00", 40 );
        tasksManager.createNewSubtask("Subtask 25", "Subtask 25", 1L, "02-1-2005 15:00", 40 );
        int expectedSize = 25;
        int actualSize = tasksManager.getPrioritizedTasks().size();
        assertEquals(actualSize, expectedSize);
    }
}