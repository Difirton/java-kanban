package constant;

public enum TypeTask {
    EPIC ("Epic"),
    SUBTASK ("Subtask");

    private String typeTask;

    TypeTask(String typeTask) {
        this.typeTask = typeTask;
    }

    public String getTypeTask() {
        return typeTask;
    }
}
