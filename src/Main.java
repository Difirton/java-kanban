import service.FileBackedTasksManager;
import service.Manager;

public class Main {

    public static void main(String... args) {
        FileBackedTasksManager tasksManager = Manager.getFileBackedTasksManager();
        tasksManager.createNewEpic("Epic 1", "Desc 1");
        tasksManager.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L);
        tasksManager.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L);
        tasksManager.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L);
        tasksManager.createNewEpic("Epic 2", "Desc 2");
        tasksManager.createNewSubtask("Subtask 2.1", "Desc sub 2", 5L);
        tasksManager.createNewSubtask("Subtask 2.2", "Desc sub 2", 5L);
        System.out.println(tasksManager);
        tasksManager.save();

        FileBackedTasksManager newTaskManager = FileBackedTasksManager.read();
        System.out.println(newTaskManager);


    }
}
