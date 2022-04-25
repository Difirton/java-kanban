package entitys;

import constants.TaskStatus;

public class Subtask extends Task {
    private static long amountId = 1;
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

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + this.getId() + '\'' +
                ", epicsId=" + this.getEpicsId() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() +
                '}';
    }
}
