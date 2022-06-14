package error;

public class ManagerSaveException extends RuntimeException {
//TODO одумать, как можно улучшить
    public ManagerSaveException() {
        super("File is not found");
    }
}
