package entity;

import constant.TaskStatus;
import java.util.Objects;

public class Subtask extends Task {
    private Long epicsId;

    public Subtask(String name, String description, long epicsId) {
        super(name, description);
        this.setId(amountId++);
        this.epicsId = epicsId;
    }

    public static long getNewId() {
        return amountId;
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
        StringBuilder sb = new StringBuilder("Subtask{");
        sb.append("id=").append(this.getId());
        sb.append(", epicsId=").append(this.getEpicsId());
        sb.append(", name='").append(this.getName()).append('\'');
        sb.append(", description='").append(this.getDescription()).append('\'');
        sb.append(", status=").append(this.getStatus()).append('}');
        return sb.toString();
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
