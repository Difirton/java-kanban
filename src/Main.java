import constant.TaskManagerType;
import utill.Manager;
import utill.TasksManager;

public class Main {

    public static void main(String[] args) {
        TasksManager tasksManager = Manager.getDefault(TaskManagerType.MANAGER_OF_EPIC_AND_SUBTASK);
        tasksManager.createNewEpic("Epic 1", "Description epic 1");

    }
}
