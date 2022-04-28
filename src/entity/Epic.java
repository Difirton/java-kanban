package entity;

import constant.TaskStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class Epic extends Task {
    private static long amountId = 1L;
    private HashMap<Long, Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasks = new HashMap<>();
        this.setId(amountId++);
    }

    public static long getNewId() {
        return amountId;
    }

    public void addSubtask(String name, String description) {
        this.subtasks.put(Subtask.getNewId(), new Subtask(name, description, this.getId()));
    }

    public void removeSubtasks() {
        this.subtasks.clear();
    }

    public void removeSubtask(Long subtaskId) {
        this.subtasks.remove(subtaskId);
    }

    public ArrayList<Subtask> getAllSubtask() {
        return this.subtasks.values().stream().collect(Collectors.toCollection(ArrayList::new));
    }

    public void changeStatusSubtaskInProgress(Long subtaskId) {
        subtasks.get(subtaskId).changeStatusInProgress();
        this.setStatus(TaskStatus.IN_PROGRESS);
    }

    public static long getAmountId() {
        return amountId;
    }

    public HashMap<Long, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" +  + this.getId() +
                ", subtaskId=" + subtasks.keySet() +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }
}
