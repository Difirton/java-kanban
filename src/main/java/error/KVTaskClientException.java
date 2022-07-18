package error;

public class KVTaskClientException extends RuntimeException{
    private final String message;

    public KVTaskClientException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
