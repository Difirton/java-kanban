package entity;

import constant.TaskStatus;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public abstract class Task implements Serializable, Comparable<Task> {
    private final long serialVersionUID = 2L;
    private final long id;
    private String name;
    private String description;
    private TaskStatus status;
    private Duration timeExecution;
    private LocalDateTime startDateTime;

    public Task(long id) {
        this.id = id;
    }

    @Override
    public int compareTo(Task anotherTask) {
        if (startDateTime.compareTo(anotherTask.startDateTime) != 0) {
            return startDateTime.compareTo(anotherTask.startDateTime);
        }
        return (int)(id - anotherTask.id);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Duration getTimeExecution() {
        return timeExecution;
    }

    public void setTimeExecution(Duration executionTime) {
        this.timeExecution = executionTime;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return startDateTime.plus(timeExecution);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Task.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("status=" + status)
                .add("start=" + startDateTime)
                .add("execution=" + timeExecution)
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
                Objects.equals(task.status, this.status) &&
                Objects.equals(task.startDateTime, this.startDateTime) &&
                Objects.equals(task.timeExecution, this.timeExecution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(17, id, name, description, status);
    }
}
