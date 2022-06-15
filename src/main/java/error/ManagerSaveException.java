package main.java.error;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException() {
        super("There was a problem saving the file, probably the directory specified for saving the data does not " +
                "exist. Check if the directory exists: \"resources\\data\"");
    }
}
