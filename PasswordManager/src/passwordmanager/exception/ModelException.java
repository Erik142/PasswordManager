package passwordmanager.exception;

/**
 * Custom Exception class used to throw error messages in MVC Models
 */
public class ModelException extends Exception {
    /**
     * Creates a new instance of a ModelException object, with the specified message String
     * @param message The message String
     */
    public ModelException(String message) {
        super(message);
    }
}
