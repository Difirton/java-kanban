package service;

import entity.Task;
import utill.HistoryManager;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final int LIMIT_HISTORY_QUEUE_TASKS = 10;
    private List<Task> historyQueueTasks = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            historyQueueTasks.add(task);
        }
        while (historyQueueTasks.size() > LIMIT_HISTORY_QUEUE_TASKS) {
            historyQueueTasks.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyQueueTasks;
    }
}
