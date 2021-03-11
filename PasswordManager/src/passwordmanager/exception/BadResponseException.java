package passwordmanager.exception;

import passwordmanager.communication.Response.ResponseCode;

/**
 * Used to throw Exceptions during network communication errors
 * @author Erik Wahlberger
 * @version 2021-03-11
 */
public class BadResponseException extends Exception {
	/**
	 * The response code constant for this instance
	 */
    private final ResponseCode responseCode;

    /**
     * Creates a new instance of the BadResponseException class with the corresponding message and response code
     * @param message The message for this BadResponseException
     * @param responseCode The response code for this BadResponseException
     */
    public BadResponseException(String message, ResponseCode responseCode) {
        super(message);

        this.responseCode = responseCode;
    }

    /**
     * Retrieves the response code for this instance
     * @return The response code
     */
    public ResponseCode getResponseCode() {
        return this.responseCode;
    }
}
