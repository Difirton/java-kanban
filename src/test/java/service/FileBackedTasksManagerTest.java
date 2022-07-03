package service;

import constant.TypeTasksManager;
import error.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TasksManagerTest {
    TasksManager fileBackedTasksManager;
    private File testFile;
    String pathToTestSaveAndLoadData;

    @Override
    TasksManager createTaskManager() {
        fileBackedTasksManager = Manager.getTaskManager(TypeTasksManager.FILE_BACKED_TASKS_MANAGER);
        pathToTestSaveAndLoadData = readPathToSaveTestData();
        testFile = new File(pathToTestSaveAndLoadData + File.separator + "dataFileBackedTasksManager.bin");
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
    public void testSave() throws NoSuchFieldException, IllegalAccessException {
        Field field = fileBackedTasksManager.getClass().getDeclaredField("file");
        field.setAccessible(true);
        field.set(fileBackedTasksManager, testFile);
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
        fileBackedTasksManager.getEpicById(1L);
        fileBackedTasksManager.getEpicById(5L);
        fileBackedTasksManager.getSubtaskById(2L);
        fileBackedTasksManager.getSubtaskById(6L);
        fileBackedTasksManager.getEpicById(1L);

        FileBackedTasksManager testFileBackedTasksManager = FileBackedTasksManager.loadFromFile(testFile);
        assertEquals(fileBackedTasksManager.getAllEpics(), testFileBackedTasksManager.getAllEpics());
        assertEquals(fileBackedTasksManager.getAllSubtasks(), testFileBackedTasksManager.getAllSubtasks());
        assertEquals(fileBackedTasksManager.getHistory(), testFileBackedTasksManager.getHistory());
        testFile.delete();
    }
}