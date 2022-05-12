package service;

import entity.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int LIMIT_HISTORY_QUEUE_TASKS = 10;
    private final List<Task> historyQueueTasks = new LinkedList<>();
    /* В LinkedList нет конструктора с интовым аргументом для указания емкости, он же просто подвязывает двойные ссылки
    к смежным элементам, емкость есть у ArrayList, но его DEFAULT_CAPACITY итак равна 10 */

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
