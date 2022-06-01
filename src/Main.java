import service.Manager;
import service.TasksManager;

public class Main {

    public static void main(String... args) {
        TasksManager tasksManager = Manager.getDefault();
        tasksManager.createNewEpic("Epic 1", "Description Epic 1");
        tasksManager.createNewEpic("Epic 2", "Description Epic 2");
        tasksManager.createNewSubtask("Subtask 3", "Subtask 3", 1L);
        tasksManager.createNewSubtask("Subtask 4", "Subtask 4", 1L);
        tasksManager.createNewSubtask("Subtask 5", "Subtask 5", 1L);
        System.out.println(tasksManager.getHistory());
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(2L);
        tasksManager.getSubtaskByIdOrNull(3L);
        System.out.println(tasksManager.getHistory());
        tasksManager.getEpicById(2L);
        System.out.println(tasksManager.getHistory());
        tasksManager.getEpicById(1L);
        System.out.println(tasksManager.getHistory());
        tasksManager.getSubtaskByIdOrNull(3L);
        tasksManager.getSubtaskByIdOrNull(5L);
        tasksManager.removeSubtasksById(3L);
        System.out.println(tasksManager.getHistory());
        tasksManager.removeEpicById(1L);
        System.out.println(tasksManager.getHistory());
        tasksManager.removeEpicById(2L);
        System.out.println(tasksManager.getHistory());




    }
}
