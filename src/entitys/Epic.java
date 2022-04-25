package entitys;

import constants.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Epic extends Task {
    private static long amountId = 1;
    private HashMap<Long, Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasks = new HashMap<>();
        this.setId(amountId++);
    }

    public static long getNewId() {
        return amountId;
    }

    public void addSubtasks(String name, String description) {
        this.subtasks.put(Subtask.getNewId(), new Subtask(name, description, this.getId()));
    }

    public void removeSubtasks() {
        this.subtasks.clear();
    }

    public ArrayList<Subtask> getAllSubtask() {
        return this.subtasks.values().stream().collect(Collectors.toCollection(ArrayList::new));
    }

    public void changeStatusSubtaskDone(Long subtaskId) {
        subtasks.get(subtaskId).changeStatusDone();
        if (subtasks.values().stream()
                .allMatch(o -> o.getStatus().equals(TaskStatus.DONE))) {
            this.setStatus(TaskStatus.DONE);
        } else {
            this.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void changeStatusSubtaskInProgress(Long subtaskId) {
        subtasks.get(subtaskId).changeStatusInProgress();
        this.setStatus(TaskStatus.IN_PROGRESS);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id='" + this.getId()  + '\'' +
                ", subtaskId='" + subtasks.keySet()  + '\'' +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() +
                '}';
    }
}
