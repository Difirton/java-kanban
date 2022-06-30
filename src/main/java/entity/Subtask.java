package entity;

import constant.TaskStatus;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.StringJoiner;

public class Subtask extends Task implements Serializable {
    private final long serialVersionUID = 2L;
    private final long epicsId;

    private Subtask (long epicsId) {
        super(0);
        this.epicsId = 0;
    }

    private Subtask(SubtaskBuilder subtaskBuilder) {
        super(subtaskBuilder.id);
        epicsId = subtaskBuilder.epicsId;
        super.setName(subtaskBuilder.name);
        super.setDescription(subtaskBuilder.description);
        super.setStatus(subtaskBuilder.status);
        super.setExecutionTime(subtaskBuilder.executionTime);
        super.setStartDateTime(subtaskBuilder.startDateTime);
    }

    public Long getEpicsId() {
        return epicsId;
    }

    public void changeStatusDone() {
        this.setStatus(TaskStatus.DONE);
    }

    public void changeStatusInProgress() {
        this.setStatus(TaskStatus.IN_PROGRESS);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Subtask.class.getSimpleName() + "[", "]")
                .add("id=" + this.getId())
                .add("epicsId=" + this.getEpicsId())
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
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicsId, subtask.epicsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicsId);
    }

    public static class SubtaskBuilder {
        private final long id;
        private final long epicsId;
        private String name = "";
        private String description = "";
        private TaskStatus status = TaskStatus.NEW;
        private Duration executionTime = Duration.ofMinutes(0);
        private LocalDateTime startDateTime = LocalDateTime.MAX;

        public SubtaskBuilder(long id, long epicsId) {
            this.id = id;
            this.epicsId = epicsId;
        }

        public SubtaskBuilder Name(String name) {
            this.name = name;
            return this;
        }

        public SubtaskBuilder Description(String description) {
            this.description = description;
            return this;
        }

        public SubtaskBuilder Status(TaskStatus taskStatus) {
            this.status = taskStatus;
            return this;
        }

        public SubtaskBuilder ExecutionTime(Duration executionTime) {
            this.executionTime = executionTime;
            return this;
        }

        public SubtaskBuilder StartDateTime(LocalDateTime startDateTime) {
            this.startDateTime = startDateTime;
            return this;
        }

        public SubtaskBuilder StartDateTime(String startDateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime formatterStartDateTime = LocalDateTime.parse(startDateTime, formatter);
            this.startDateTime = formatterStartDateTime;
            return this;
        }

        public Subtask build() {
            return new Subtask(this);
        }
    }
}
