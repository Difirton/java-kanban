package entitys;

import constants.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Epic extends Task {
    private static long amountId = 1;
    private HashMap<Long, Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasks = new HashMap<>();
        this.setId(amountId++);
    }

    public static long getNewId() {
        return ++amountId;
    }

    public void addSubtasks(String name, String description) {
        this.subtasks.put(Subtask.getNewId(), new Subtask(name, description, this.getId()));
    }

    public void removeSubtasks() {
        this.subtasks.clear();
    }

    public Subtask getSubtaskById(Long subtaskId) {
        return subtasks.get(subtaskId);
    }

    public ArrayList<Subtask> getAllSubtask() {
        return new ArrayList(List.of(subtasks.values()));
    }

    public void changeStatusSubtask(Long subtaskId) {
        subtasks.get(subtaskId).changeStatusDone();
        if (subtasks.values().stream()
                .allMatch(o -> o.getStatus().equals(TaskStatus.DONE))) {
            this.setStatus(TaskStatus.DONE);
        } else {
            this.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
