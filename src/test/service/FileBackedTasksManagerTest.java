package service;

import junit.framework.TestCase;
import main.java.constant.TypeTasksManager;
import main.java.service.FileBackedTasksManager;
import main.java.service.Manager;
import main.java.service.TasksManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;

public class FileBackedTasksManagerTest extends TestCase {
    private TasksManager fileBackedTasksManager;
    private File testFile;

    @Before
    public void setUp() throws Exception{
        fileBackedTasksManager = Manager.getTaskManager(TypeTasksManager.FILE_BACKED_TASKS_MANAGER);
        testFile = new File("src" + File.separator + "test" + File.separator + "resources" + File.separator
                + "data" + File.separator + "data.bin");
        fileBackedTasksManager.createNewEpic("Epic 1", "Desc 1");
        fileBackedTasksManager.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L);
        fileBackedTasksManager.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L);
        fileBackedTasksManager.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L);
        fileBackedTasksManager.createNewEpic("Epic 2", "Desc 2");
        fileBackedTasksManager.createNewSubtask("Subtask 2.1", "Desc sub 2", 5L);
        fileBackedTasksManager.createNewSubtask("Subtask 2.2", "Desc sub 2", 5L);
    }

    @Test
    public void testSave() throws NoSuchFieldException, IllegalAccessException {
        Field field = fileBackedTasksManager.getClass().getDeclaredField("file");
        field.setAccessible(true);
        field.set(fileBackedTasksManager, testFile);
        fileBackedTasksManager.getEpicById(1L);
        File actualFile = new File("src" + File.separator + "test" + File.separator + "resources" + File.separator
                + "data" + File.separator + "data.bin");
        Assert.assertTrue(actualFile.exists());
        actualFile.delete();
    }

    @Test
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
        Assert.assertEquals(fileBackedTasksManager.getAllEpics(), testFileBackedTasksManager.getAllEpics());
        Assert.assertEquals(fileBackedTasksManager.getAllSubtasks(), testFileBackedTasksManager.getAllSubtasks());
        Assert.assertEquals(fileBackedTasksManager.getHistory(), testFileBackedTasksManager.getHistory());
        testFile.delete();
    }
}