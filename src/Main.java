import constant.TaskManagerType;
import utill.Manager;
import utill.TasksManager;

public class Main {

    public static void main(String[] args) {
        TasksManager tasksManager = Manager.getDefault(TaskManagerType.MANAGER_OF_EPIC_AND_SUBTASK);
        tasksManager.createNewEpic("Epic 1", "Desc 1");
        tasksManager.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L);
        tasksManager.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L);
        tasksManager.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L);
        tasksManager.createNewEpic("Epic 2", "Desc 2");
        tasksManager.createNewSubtask("Subtask 2.1", "Desc sub 2", 2L);
        tasksManager.createNewSubtask("Subtask 2.2", "Desc sub 2", 2L);

        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getHistory().forEach(System.out::println);
        System.out.println(tasksManager.getHistory().size());
        System.out.println("_________________________________________________");
        tasksManager.getEpicById(2L);
        tasksManager.getAllSubtasks();
        tasksManager.getSubtaskByIdOrNull(5L);
        tasksManager.getSubtaskByIdOrNull(3L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getHistory().forEach(System.out::println);
        System.out.println(tasksManager.getHistory().size());
        System.out.println("_________________________________________________");
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getEpicById(1L);
        tasksManager.getHistory().forEach(System.out::println);
        System.out.println(tasksManager.getHistory().size());
        System.out.println("_________________________________________________");
        tasksManager.removeEpicById(2L);
        tasksManager.getEpicById(2L);
        tasksManager.getSubtaskByIdOrNull(4L);
        tasksManager.getSubtaskByIdOrNull(5L);
        tasksManager.getSubtaskByIdOrNull(2L);
        tasksManager.getSubtaskByIdOrNull(2L);
        tasksManager.getHistory().forEach(System.out::println);
        System.out.println(tasksManager.getHistory().size());
    }
}
