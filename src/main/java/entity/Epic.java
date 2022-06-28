package entity;

import constant.TaskStatus;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Epic extends Task implements Serializable {
    private final long serialVersionUID = 1L;
    private List<Long> subtasksId; //TODO Переименовать поле

    public Epic(String name, String description) {
        super(name, description);
        this.subtasksId = new ArrayList<>();
        this.setId(amountId++);
    }

    public static long getNewId() {
        return amountId;
    }

    public void addSubtask(Long idSubtask) {
        this.subtasksId.add(idSubtask);
    }

    public void removeSubtasks() {
        this.subtasksId.clear();
    }

    public void removeSubtask(Long idSubtask) {
        this.subtasksId.remove(idSubtask);
    }

    public List<Long> getAllIdSubtasks() {
        return subtasksId;
    }

    public static long getAmountId() {
        return amountId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Task.class.getSimpleName() + "[", "]")
                .add("id=" + this.getId())
                .add(", subtaskId=" + subtasksId)
                .add("name='" + this.getName() + "'")
                .add("description='" + this.getDescription() + "'")
                .add("status=" + this.getStatus())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        Collections.sort(subtasksId);
        Collections.sort( epic.subtasksId);
        return Objects.equals(subtasksId, epic.subtasksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksId);
    }
}
