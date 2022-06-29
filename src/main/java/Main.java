import constant.TypeTasksManager;
import service.FileBackedTasksManager;
import service.Manager;
import service.TasksManager;

import java.io.File;

public class Main {

    public static void main(String... args) {
        TasksManager tasksManager = Manager.getTaskManager(TypeTasksManager.IN_MEMORY_TASKS_MANAGER);
        tasksManager.createNewEpic("Epic 1", "Description Epic 1");
        tasksManager.createNewEpic("Epic 2", "Description Epic 2");
        tasksManager.createNewSubtask("Subtask 3", "Subtask 3", 1L);
        tasksManager.createNewSubtask("Subtask 4", "Subtask 4", 1L);
        tasksManager.createNewSubtask("Subtask 5", "Subtask 5", 1L);
        tasksManager.removeEpicById(1L);
        tasksManager.createNewSubtask("Subtask 4", "Subtask 4", 2L);
        tasksManager.createNewEpic("Epic 3", "Description Epic 3");
        tasksManager.createNewSubtask("Subtask 4", "Subtask 4", 7L);
        tasksManager.removeAllSubtasks();
        tasksManager.createNewSubtask("Subtask 4", "Subtask 4", 2L);
        tasksManager.createNewEpic("Epic 4", "Description Epic 4");
    }
}
