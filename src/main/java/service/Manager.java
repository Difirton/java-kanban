package main.java.service;

public class Manager {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TasksManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTasksManager getFileBackedTasksManager() {
        return new FileBackedTasksManager();
    }
}
