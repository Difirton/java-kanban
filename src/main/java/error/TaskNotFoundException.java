package error;

public class TaskNotFoundException extends RuntimeException{
    private final String message;

    public TaskNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
