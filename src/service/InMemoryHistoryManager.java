package service;

import entity.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int LIMIT_HISTORY_QUEUE_TASKS = 10;
    private final List<Task> historyQueueTasks = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            historyQueueTasks.add(task);
        }
        //while был установлен, чтобы наверняка исключить все возможные варианты=)
        if (historyQueueTasks.size() > LIMIT_HISTORY_QUEUE_TASKS) {
            historyQueueTasks.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyQueueTasks;
    }
}
