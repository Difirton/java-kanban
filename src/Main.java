import service.Manager;
import service.TasksManager;

public class Main {

    public static void main(String[] args) {
        TasksManager tasksManager = Manager.getDefault();
        tasksManager.createNewEpic("Epic 1", "Description Epic 1");
        tasksManager.createNewEpic("Epic 2", "Description Epic 2");
        tasksManager.createNewEpic("Epic 2", "Description Epic 3");
//        tasksManager.createNewSubtask("Subtask 1", "Subtask 1", 1L);
//        tasksManager.createNewSubtask("Subtask 2", "Subtask 2", 1L);
//        tasksManager.createNewSubtask("Subtask 3", "Subtask 3", 2L);
//        tasksManager.createNewSubtask("Subtask 4", "Subtask 4", 2L);
//        tasksManager.createNewSubtask("Subtask 5", "Subtask 5", 2L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(2L);
        tasksManager.getEpicById(3L);
        System.out.println(tasksManager.getHistory());

    }
}
