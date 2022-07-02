package entity;

import constant.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    private final long serialVersionUID = 2L;
    private List<Long> subtasksId; //TODO Переименовать поле

    private Epic() {
        super(0);
    }

    private Epic(EpicBuilder epicBuilder) {
        super(epicBuilder.id);
        super.setName(epicBuilder.name);
        super.setDescription(epicBuilder.description);
        super.setStatus(epicBuilder.status);
        super.setTimeExecution(epicBuilder.timeExecution);
        super.setStartDateTime(epicBuilder.startDateTime);
        this.subtasksId = epicBuilder.subtasksId;
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
                .add("subtasksId=" + subtasksId)
                .add("name='" + this.getName() + "'")
                .add("description='" + this.getDescription() + "'")
                .add("status=" + this.getStatus())
                .add("start=" + this.getStartDateTime())
                .add("execution=" + this.getTimeExecution().toMinutes())
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

    public static class EpicBuilder {
        private final long id;
        private String name = "";
        private String description = "";
        private TaskStatus status = TaskStatus.NEW;
        private List<Long> subtasksId = new ArrayList<>();
        private Duration timeExecution = Duration.ofMinutes(0);
        private LocalDateTime startDateTime = LocalDateTime.MAX;

        public EpicBuilder(long id) {
            this.id = id;
        }

        public EpicBuilder Name(String name) {
            this.name = name;
            return this;
        }

        public EpicBuilder Description(String description) {
            this.description = description;
            return this;
        }

        public Epic build() {
            return new Epic(this);
        }
    }
}
