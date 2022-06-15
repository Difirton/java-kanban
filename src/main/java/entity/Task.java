package main.java.entity;

import main.java.constant.TaskStatus;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

import static main.java.constant.TaskStatus.*;

public abstract class Task implements Serializable {
    private final long serialVersionUID = 1L;
    protected static long amountId = 1L;
    private long id;
    private String name;
    private String description;
    private TaskStatus status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = NEW;
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

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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
