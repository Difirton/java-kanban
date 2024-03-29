package service;

import entity.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(Long id);

    void clearHistory();

    List<Task> getHistory();
}
