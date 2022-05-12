package service;

public class Manager {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TasksManager getDefault() {
        return new InMemoryTaskManager();
    }
}
