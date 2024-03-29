package entity;

import constant.TaskStatus;
import constant.TypeTask;
import utill.DateParser;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

import static constant.TypeTask.SUBTASK;

public class Subtask extends Task {
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
        super.setTimeExecution(subtaskBuilder.timeExecution);
        super.setStartDateTime(subtaskBuilder.startDateTime);
    }

    public TypeTask getType() {
        return SUBTASK;
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
        return new StringJoiner(", ", Subtask.class.getSimpleName() + "{", "}")
                .add("id=" + this.getId())
                .add("epicsId=" + this.getEpicsId())
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
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicsId, subtask.epicsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicsId);
    }

    public static class SubtaskBuilder {
        private long id;
        private long epicsId;
        private String name = "";
        private String description = "";
        private TaskStatus status = TaskStatus.NEW;
        private Duration timeExecution = Duration.ofMinutes(0);
        private LocalDateTime startDateTime = LocalDateTime.MAX;

        public SubtaskBuilder() { }

        public SubtaskBuilder ID(long id) {
            this.id = id;
            return this;
        }

        public SubtaskBuilder EpicsID(long epicsId) {
            this.epicsId = epicsId;
            return this;
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

        public SubtaskBuilder TimeExecution(Duration timeExecution) {
            this.timeExecution = timeExecution;
            return this;
        }

        public SubtaskBuilder TimeExecutionInMinutes(int timeExecutionInMinutes) {
            this.timeExecution = Duration.ofMinutes(timeExecutionInMinutes);
            return this;
        }

        public SubtaskBuilder StartDateTime(LocalDateTime startDateTime) {
            this.startDateTime = startDateTime;
            return this;
        }

        public SubtaskBuilder StartDateTime(String startDateTime) {
            LocalDateTime formatterStartDateTime = DateParser.stringToDate(startDateTime);
            this.startDateTime = formatterStartDateTime;
            return this;
        }

        public Subtask build() {
            return new Subtask(this);
        }
    }
}
