package passwordmanager.exception;

import passwordmanager.communication.Response.ResponseCode;

/**
 * Used to throw Exceptions during network communication errors
 * @author Erik Wahlberger
 * @version 2021-03-11
 */
public class BadResponseException extends Exception {
    private final ResponseCode responseCode;

    public BadResponseException(String message, ResponseCode responseCode) {
        super(message);

        this.responseCode = responseCode;
    }

    public ResponseCode getResponseCode() {
        return this.responseCode;
    }
}
