package entity;

import constant.TaskStatus;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Epic extends Task implements Serializable {
    private final long serialVersionUID = 1L;
    private List<Long> subtasksId; //TODO Переименовать поле

    public Epic(long id,String name, String description) {
        super(id, name, description);
        this.subtasksId = new ArrayList<>();
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

    @Override
    public String toString() {
        return new StringJoiner(", ", Epic.class.getSimpleName() + "[", "]")
                .add("id=" + this.getId())
                .add(", subtasksId=" + subtasksId)
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
