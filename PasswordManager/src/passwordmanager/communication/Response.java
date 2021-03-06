package passwordmanager.communication;

import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;

/**
 * Represents a response from a server to a client
 * 
 * @author Erik Wahlberger
 */
public class Response<T> extends Message<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4474155900326959886L;

	/**
	 * Represents the possible responses codes a server can send back to a client
	 */
	public enum ResponseCode {
		OK, Fail, InvalidKey
	}

	private final ResponseCode responseCode;

	/**
	 * Creates a new Response object with the specified ResponseCode,
	 * CommunicationOperation and data
	 * 
	 * @param responseCode The ResponseCode
	 * @param operation    The CommunicationOperation
	 * @param data         The data
	 */
	public Response(ResponseCode responseCode, CommunicationOperation operation, T data) {
		super(operation, data);
		this.responseCode = responseCode;
	}

	/**
	 * Retrieves the ResponseCode
	 * 
	 * @return The ResponseCode
	 */
	public ResponseCode getResponseCode() {
		return responseCode;
	}
}
