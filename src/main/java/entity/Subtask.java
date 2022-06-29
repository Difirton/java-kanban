package entity;

import constant.TaskStatus;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

public class Subtask extends Task implements Serializable {
    private final long serialVersionUID = 1L;
    private Long epicsId;

    public Subtask(long id, String name, String description, long epicsId) {
        super(id, name, description);
        this.epicsId = epicsId;
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
}
