package service;

import entity.Epic;
import entity.Subtask;
import entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask3;
    private Subtask subtask4;

    @BeforeEach
    public void setUp() {
        historyManager = Manager.getDefaultHistory();
        epic1 = new Epic.EpicBuilder(1L)
                .Name("Epic 1")
                .Description("Epic 1")
                .build();
        epic2 = new Epic.EpicBuilder(2L)
                .Name("Epic 2")
                .Description("Epic 2")
                .build();
        subtask3 = new Subtask.SubtaskBuilder(3L, 1L)
                .Name("Subtask 3")
                .Description("Subtask 3")
                .build();
        subtask4 = new Subtask.SubtaskBuilder(4L, 1L)
                .Name("Subtask 4")
                .Description("Subtask 4")
                .build();
        historyManager.add(epic1);
        historyManager.add(subtask3);
    }

    @Test
    @DisplayName("Test add in history one tasks, expected ok")
    public void testAddSingleTask() {
        historyManager.add(epic2);
        int actual = historyManager.getHistory().size();
        int expected = 3;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test add in history two another type tasks, expected ok")
    public void testAddTwoAnotherTypeTask() {
        historyManager.add(epic2);
        historyManager.add(subtask4);
        int actual = historyManager.getHistory().size();
        int expected = 4;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test remove in history two another type tasks, expected ok")
    void testRemoveTask() {
        historyManager.remove(1L);
        historyManager.remove(3L);
        int actual = historyManager.getHistory().size();
        int expected = 0;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test remove in history task if this is absent, expected ok")
    void testRemoveTaskIfThisTaskIsNotExistInHistory() {
        historyManager.remove(2L);
        historyManager.remove(4L);
        int actual = historyManager.getHistory().size();
        int expected = 2;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test return List with history calls tasks, expected ok")
    void testGetHistory() {
        List<Task> expected = new ArrayList<>();
        expected.add(epic1);
        expected.add(subtask3);
        List<Task> actual = historyManager.getHistory();
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test clear history, expected ok")
    void testClearHistory() {
        historyManager.clearHistory();
        boolean actual = historyManager.getHistory().isEmpty();
        assertTrue(actual);
    }

    @Test
    @DisplayName("No-repetition test, expected ok")
    void testNoRepetitionHistory() {
        historyManager.add(epic2);
        historyManager.add(subtask3);
        historyManager.add(epic1);
        historyManager.add(epic2);
        historyManager.add(subtask4);
        historyManager.add(epic2);
        int actual = historyManager.getHistory().size();
        int expected = 4;
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Test controlling the position of adding the last task, expected ok")
    void testControllingAddingLastPositionTaskInHistory() {
        historyManager.add(epic2);
        historyManager.add(subtask3);
        historyManager.add(epic1);
        historyManager.add(subtask4);
        historyManager.add(epic2);
        List<Task> currentHistory =  historyManager.getHistory();
        Task actual = currentHistory.get(currentHistory.size() - 1);
        Task expected = epic2;
        assertEquals(actual, expected);
    }
}