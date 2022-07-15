package service;

import constant.TypeTasksManager;
import error.ManagerSaveException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TasksManagerTest {
    private TasksManager fileBackedTasksManager;
    private File testFile;
    private String pathToTestSaveAndLoadData;

    @Override
    TasksManager createTaskManager() throws NoSuchFieldException, IllegalAccessException {
        fileBackedTasksManager = Manager.getTaskManager(TypeTasksManager.FILE_BACKED_TASKS_MANAGER);
        pathToTestSaveAndLoadData = readPathToSaveTestData();
        testFile = new File(pathToTestSaveAndLoadData + File.separator + "dataFileBackedTasksManager.bin");
        Field field = fileBackedTasksManager.getClass().getDeclaredField("file");
        field.setAccessible(true);
        field.set(fileBackedTasksManager, testFile);
        return fileBackedTasksManager;
    }

    private String readPathToSaveTestData() {
        try (FileInputStream propertiesReader = new FileInputStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(propertiesReader);
            return properties.getProperty("test.directory.toSaveFileBackedTasksManager");
        } catch (IOException exception) {
            throw new ManagerSaveException("There is no data for the path where the object will be serialized. " +
                    "Check that there is a config.properties file at the root of the project with the key " +
                    "test.directory.toSaveFileBackedTasksManager" + exception.getMessage());
        }
    }

    @Test
    @DisplayName("Test save in file all parameters of fileBackedTasksManager, expected ok")
    public void testSave() {
        fileBackedTasksManager.getEpicById(1L);
        File actualFile = new File(pathToTestSaveAndLoadData + File.separator + "dataFileBackedTasksManager.bin");
        assertTrue(actualFile.exists());
        actualFile.delete();
    }

    @Test
    @DisplayName("Test load from file fileBackedTasksManager, expected ok")
    public void testLoadFromFile() throws NoSuchFieldException, IllegalAccessException {
        Field field = fileBackedTasksManager.getClass().getDeclaredField("file");
        field.setAccessible(true);
        field.set(fileBackedTasksManager, testFile);
        fileBackedTasksManager.getTaskById(1L);
        fileBackedTasksManager.getTaskById(5L);
        fileBackedTasksManager.getTaskById(2L);
        fileBackedTasksManager.getTaskById(6L);
        fileBackedTasksManager.getTaskById(1L);

        FileBackedTasksManager testFileBackedTasksManager = FileBackedTasksManager.loadFromFile(testFile);
        assertEquals(fileBackedTasksManager.getAllEpics(), testFileBackedTasksManager.getAllEpics());
        assertEquals(fileBackedTasksManager.getAllSubtasks(), testFileBackedTasksManager.getAllSubtasks());
        assertEquals(fileBackedTasksManager.getHistory(), testFileBackedTasksManager.getHistory());
        testFile.delete();
    }

    @Test
    @DisplayName("Test throw ManagerSaveException if URI is wrong, expected ok")
    public void testThrow() throws NoSuchFieldException, IllegalAccessException {
        File emptyFile = new File("");
        Field field = fileBackedTasksManager.getClass().getDeclaredField("file");
        field.setAccessible(true);
        field.set(fileBackedTasksManager, emptyFile);
        assertThrows(ManagerSaveException.class, () -> fileBackedTasksManager.getTaskById(1L));
    }
}