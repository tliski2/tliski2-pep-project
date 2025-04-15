package Exception;

/**
 * Custom exception class to handle user input errors (account registration, login, etc)
 */
public class InputException extends RuntimeException{
    public InputException(String message) {
        super(message);
    }
}
