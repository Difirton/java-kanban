package service;

import constant.TypeTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest {
    private TasksManager fileBackedTasksManager;
    private File testFile;

    @BeforeEach
    public void setUp() throws Exception{
        fileBackedTasksManager = Manager.getTaskManager(TypeTasksManager.FILE_BACKED_TASKS_MANAGER);
        testFile = new File("src" + File.separator + "test" + File.separator + "resources" +
                File.separator + "data" + File.separator + "dataTestFileBackedTasksManager.bin");
        fileBackedTasksManager.createNewEpic("Epic 1", "Desc 1");
        fileBackedTasksManager.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L);
        fileBackedTasksManager.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L);
        fileBackedTasksManager.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L);
        fileBackedTasksManager.createNewEpic("Epic 2", "Desc 2");
        fileBackedTasksManager.createNewSubtask("Subtask 2.1", "Desc sub 2", 5L);
        fileBackedTasksManager.createNewSubtask("Subtask 2.2", "Desc sub 2", 5L);
    }

    @Test
    @DisplayName("Test save in file all parameters of fileBackedTasksManager, expected ok")
    public void testSave() throws NoSuchFieldException, IllegalAccessException {
        Field field = fileBackedTasksManager.getClass().getDeclaredField("file");
        field.setAccessible(true);
        field.set(fileBackedTasksManager, testFile);
        fileBackedTasksManager.getEpicById(1L);
        File actualFile = new File("src" + File.separator + "test" + File.separator + "resources" +
                File.separator + "data" + File.separator + "data.bin");
        assertTrue(actualFile.exists());
        actualFile.delete();
    }

    @Test
    @DisplayName("Test load from file fileBackedTasksManager, expected ok")
    public void testLoadFromFile() throws NoSuchFieldException, IllegalAccessException {
        Field field = fileBackedTasksManager.getClass().getDeclaredField("file");
        field.setAccessible(true);
        field.set(fileBackedTasksManager, testFile);
        fileBackedTasksManager.getEpicById(1L);
        fileBackedTasksManager.getEpicById(5L);
        fileBackedTasksManager.getSubtaskByIdOrNull(2L);
        fileBackedTasksManager.getSubtaskByIdOrNull(6L);
        fileBackedTasksManager.getEpicById(1L);

        FileBackedTasksManager testFileBackedTasksManager = FileBackedTasksManager.loadFromFile(testFile);
        assertEquals(fileBackedTasksManager.getAllEpics(), testFileBackedTasksManager.getAllEpics());
        assertEquals(fileBackedTasksManager.getAllSubtasks(), testFileBackedTasksManager.getAllSubtasks());
        assertEquals(fileBackedTasksManager.getHistory(), testFileBackedTasksManager.getHistory());
        testFile.delete();
    }
}