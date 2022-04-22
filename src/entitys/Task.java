package entitys;

import controllers.TaskStatus;
import static controllers.TaskStatus.*;

public abstract class Task {
    long id;
    String name;
    String description;
    TaskStatus status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = NEW;
    }

    public long getId() {
        return id;
    }
}
