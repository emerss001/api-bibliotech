package exception;

public class NullConnectionException extends RuntimeException {
    public NullConnectionException(String message) {
        super(message);
    }
}
