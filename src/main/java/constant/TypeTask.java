package constant;

public enum TypeTask {
    EPIC ("Epic"),
    SUBTASK ("Subtask");

    private final String nameTypeTask;

    TypeTask(String nameTypeTask) {
        this.nameTypeTask = nameTypeTask;
    }

    public String getNameTypeTask() {
        return nameTypeTask;
    }
}
