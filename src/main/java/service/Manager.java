package service;

import constant.TypeTasksManager;

public class Manager {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TasksManager getTaskManager(TypeTasksManager typeTasksManager) {
        TasksManager tasksManager;
        switch (typeTasksManager) {
            case IN_MEMORY_TASKS_MANAGER:
                tasksManager = new InMemoryTaskManager();
                break;
            case FILE_BACKED_TASKS_MANAGER:
                tasksManager = new FileBackedTasksManager();
                break;
            case HTTP_TASKS_MANAGER:
                tasksManager = new HTTPTasksManager();
                break;
            default:
                throw new RuntimeException("No such TaskManager " + typeTasksManager);
        }
        return tasksManager;
    }

    public static TasksManager getDefault(String key) {
        return Manager.getTaskManager(TypeTasksManager.HTTP_TASKS_MANAGER);
    }
}
