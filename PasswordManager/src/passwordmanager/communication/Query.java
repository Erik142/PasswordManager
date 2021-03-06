package passwordmanager.communication;

import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;

/**
 * The Query class represents a query sent from the client to the server
 * 
 * @author Erik Wahlberger
 */
public class Query<T> extends Message<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8618620107514649552L;
	private final String authToken;

	/**
	 * Creates a new instance of a Query object with the specified authorization
	 * token and the specified CommunicationOperation
	 * 
	 * @param authToken The authorization token
	 * @param operation The CommunicationOperation
	 */
	public Query(String authToken, CommunicationOperation operation) {
		this(authToken, operation, null);
	}

	/**
	 * Creates a new instance of a Query object with the specified authorization
	 * token, CommunicationOperation and data
	 * 
	 * @param authToken The authorization token
	 * @param operation The CommunicationOperation
	 * @param data      The data
	 */
	public Query(String authToken, CommunicationOperation operation, T data) {
		super(operation, data);
		this.authToken = authToken;
	}

	/**
	 * Retrieves the authorization token
	 * 
	 * @return The authorization token
	 */
	public String getAuthToken() {
		return authToken;
	}

}
