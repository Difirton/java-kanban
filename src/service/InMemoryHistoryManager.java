package service;

import entity.Task;
import utill.CustomLinkedList;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList historyQueueTasks = new CustomLinkedList();

    @Override
    public void add(Task task) {
        historyQueueTasks.add(task);
    }

    @Override
    public void remove(Long id) {
        historyQueueTasks.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyQueueTasks.toList();
    }

    @Override
    public void clearHistory() {
        historyQueueTasks.clear();
    }
}
