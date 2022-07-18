package error;

public class ManagerSaveException extends RuntimeException {
    private final String message;

    public ManagerSaveException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
