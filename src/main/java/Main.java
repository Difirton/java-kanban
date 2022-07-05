import constant.TypeTasksManager;
import service.Manager;
import service.TasksManager;

public class Main {

    public static void main(String... args) {
        TasksManager inMemoryTaskManager = Manager.getTaskManager(TypeTasksManager.IN_MEMORY_TASKS_MANAGER);
        inMemoryTaskManager.createNewEpic("Epic 1", "Desc 1");
        inMemoryTaskManager.createNewSubtask("Subtask 1.1", "Desc sub 1", 1L);
        inMemoryTaskManager.createNewSubtask("Subtask 1.2", "Desc sub 1", 1L);
        inMemoryTaskManager.createNewSubtask("Subtask 1.3", "Desc sub 1", 1L, "2020-01-01 02:00", 40);
        inMemoryTaskManager.createNewEpic("Epic 2", "Desc 2");
        inMemoryTaskManager.createNewSubtask("Subtask 2.1", "Desc sub 2", 5L, "2020-01-01 03:00", 40);
        inMemoryTaskManager.createNewSubtask("Subtask 2.4", "Desc sub 2", 5L);
        inMemoryTaskManager.createNewSubtask("Subtask 2.2", "Desc sub 2", 5L, "2020-01-01 04:00", 40);
        inMemoryTaskManager.createNewSubtask("Subtask 2.3", "Desc sub 2", 5L);
        inMemoryTaskManager.createNewSubtask("Subtask 2.5", "Desc sub 2", 5L);

        System.out.println(inMemoryTaskManager.getPrioritizedTasks());

        inMemoryTaskManager.removeSubtaskById(6L);
        System.out.println(inMemoryTaskManager.getPrioritizedTasks());
    }
}
