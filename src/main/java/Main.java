import constant.TypeTasksManager;
import service.FileBackedTasksManager;
import service.Manager;
import service.TasksManager;

import java.io.File;

public class Main {

    public static void main(String... args) {
        TasksManager tasksManager = Manager.getTaskManager(TypeTasksManager.FILE_BACKED_TASKS_MANAGER);
        tasksManager.createNewEpic("Epic 1", "Description Epic 1");
        tasksManager.createNewEpic("Epic 2", "Description Epic 2");
        tasksManager.createNewSubtask("Subtask 3", "Subtask 3", 1L);
        tasksManager.createNewSubtask("Subtask 4", "Subtask 4", 1L);
        tasksManager.createNewSubtask("Subtask 5", "Subtask 5", 1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(2L);
        tasksManager.getSubtaskByIdOrNull(3L);
        tasksManager.getEpicById(2L);
        tasksManager.getEpicById(1L);
        tasksManager.getSubtaskByIdOrNull(3L);
        tasksManager.getSubtaskByIdOrNull(5L);
        System.out.println(tasksManager.getHistory());
        System.out.println(tasksManager);

        File fileToRead = new File("src" + File.separator + "main" + File.separator+ "resources"
                + File.separator + "data" + File.separator + "data.bin");
        FileBackedTasksManager newTaskManager = FileBackedTasksManager.loadFromFile(fileToRead);
        System.out.println(newTaskManager);
        System.out.println(newTaskManager.getHistory());
    }
}
