package entity;

import constant.TaskStatus;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public abstract class Task implements Serializable {
    private final long serialVersionUID = 2L;
    private final long id;
    private String name;
    private String description;
    private TaskStatus status;
    private Duration executionTime;
    private LocalDateTime startDateTime;

    public Task(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Duration getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Duration executionTime) {
        this.executionTime = executionTime;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Task.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("status=" + status)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(task.id, this.id) &&
                Objects.equals(task.name, this.name) &&
                Objects.equals(task.description, this.description) &&
                Objects.equals(task.status, this.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(17, id, name, description, status);
    }
}
