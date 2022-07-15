package service;

import constant.TypeTasksManager;
import controller.KVServer;
import entity.Epic;
import entity.Subtask;
import entity.Task;
import error.ManagerSaveException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTasksManagerTest extends TasksManagerTest{
    private static KVServer kvServer;
    private TasksManager tasksManager;
    private String key;

    @Override
    TasksManager createTaskManager() {
        readKey();
        tasksManager = Manager.getTaskManager(TypeTasksManager.HTTP_TASKS_MANAGER);
        return tasksManager;
    }

    @BeforeAll
    public static void startServer() throws IOException {
        kvServer = new KVServer().start();
    }

    @AfterAll
    public static void stopServer() {
        kvServer.stop();
    }

    private void readKey() {
        try (FileInputStream propertiesReader = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(propertiesReader);
            key = properties.getProperty("KVServer.key");
        } catch (IOException exception) {
            throw new ManagerSaveException("There is no data on the address and port, the address is located KVServer. " +
                    "Check that there is a config.properties file at the root of the project with the keys " +
                    "KVServer.address and KVServer.port" + exception.getMessage());
        }
    }

    @Test
    @DisplayName("Test save epics of HTTPTasksManager in KVServer and load this, expected ok")
    public void testSaveAndLoadEpics() {
        List<Epic> expected = tasksManager.getAllEpics();
        TasksManager newManager = HTTPTasksManager.loadFromServer(key);
        List<Epic> actual = newManager.getAllEpics();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test save subtasks of HTTPTasksManager in KVServer and load this, expected ok")
    public void testSaveAndLoadSubtasks() {
        List<Subtask> expected = tasksManager.getAllSubtasks();
        TasksManager newManager = HTTPTasksManager.loadFromServer(key);
        List<Subtask> actual = newManager.getAllSubtasks();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test save amountTaskId of HTTPTasksManager in KVServer and load this, expected ok")
    public void testSaveAndLoadAmountTaskId() {
        Long expected = 8L;
        TasksManager newManager = HTTPTasksManager.loadFromServer(key);
        newManager.createNewEpic("test", "test");
        Epic testEpic = newManager.getEpicById(8L);
        Long actual = testEpic.getId();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test save history of HTTPTasksManager in KVServer and load this, expected ok")
    public void testSaveAndLoadHistory() {
        tasksManager.getTaskById(2L);
        tasksManager.getTaskById(1L);
        tasksManager.getTaskById(6L);
        List<Task> expected = tasksManager.getHistory();
        TasksManager newManager = HTTPTasksManager.loadFromServer(key);
        List<Task> actual = newManager.getHistory();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test save priority tasks list of HTTPTasksManager in KVServer and load this, expected ok")
    public void testSaveAndLoadPrioritizedTasks() {
        List<Task> expected = tasksManager.getPrioritizedTasks();
        TasksManager newManager = HTTPTasksManager.loadFromServer(key);
        List<Task> actual = newManager.getPrioritizedTasks();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test save occupied slots time intervals list of HTTPTasksManager in KVServer and load this, expected ok")
    public void testSaveAndLoadTimeIntervalsList() {
        int expected = tasksManager.getPrioritizedTasks().size();
        TasksManager newManager = HTTPTasksManager.loadFromServer(key);
        newManager.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L, "2020-01-01 01:00", 40);
        int actual = newManager.getPrioritizedTasks().size();
        assertEquals(expected, actual);
    }
}
