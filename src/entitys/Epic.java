package entitys;

import java.util.HashMap;

public class Epic extends Task {
    private static long amountId = 1;
    private HashMap<Long, Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasks = new HashMap<>();
        this.id = amountId++;
    }

    public static long getNewId() {
        return ++amountId;
    }

    public void addSubtasks(String name, String description) {
        this.subtasks.put(Subtask.getNewId(), new Subtask(name, description, this.getId()));
    }

    public void removeSubtasks() {
        this.subtasks.clear();
    }

    public Subtask returnSubtaskById(Long subtaskId) {
        return subtasks.get(subtaskId);
    }
}
