package service;

import constant.TypeTasksManager;

public class Manager {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TasksManager getTaskManager(TypeTasksManager typeTasksManager) {
        TasksManager tasksManager = null;
        switch (typeTasksManager) {
            case IN_MEMORY_TASKS_MANAGER:
                tasksManager = new InMemoryTaskManager();
                break;
            case FILE_BACKED_TASKS_MANAGER:
                tasksManager = new FileBackedTasksManager();
        }
        return tasksManager;
    }
}
