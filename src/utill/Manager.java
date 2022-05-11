package utill;

import constant.HistoryManagerType;
import constant.TaskManagerType;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;

public enum Manager {
    INSTANCE;

    public static HistoryManager getDefaultHistory(HistoryManagerType historyManagerType) {
        HistoryManager historyManager = null;
        switch (historyManagerType) {
            case EPIC_AND_SUBTASK: historyManager = new InMemoryHistoryManager();
            break;
        }
        return historyManager;
    }

    public static TasksManager getDefault(TaskManagerType taskManagerType) {
        TasksManager tasksManager = null;
        switch (taskManagerType) {
            case MANAGER_OF_EPIC_AND_SUBTASK: tasksManager = new InMemoryTaskManager();
        }
        return tasksManager;
    }
}
