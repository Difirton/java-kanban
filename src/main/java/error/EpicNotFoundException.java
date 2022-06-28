package error;

public class EpicNotFoundException extends RuntimeException{

    public EpicNotFoundException(String message) {
        super(message);
    }
}
