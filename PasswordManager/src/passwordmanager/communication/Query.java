package passwordmanager.communication;

import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;

/**
 * The Query class represents a query sent from the client to the server
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public class Query<T> extends Message<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8618620107514649552L;

	/**
	 * Creates a new instance of a Query object with the specified authorization
	 * token and the specified CommunicationOperation
	 * 
	 * @param operation The CommunicationOperation
	 */
	public Query(CommunicationOperation operation) {
		this(operation, null);
	}

	/**
	 * Creates a new instance of a Query object with the specified authorization
	 * token, CommunicationOperation and data
	 * 
	 * @param operation The CommunicationOperation
	 * @param data      The data
	 */
	public Query(CommunicationOperation operation, T data) {
		super(operation, data);
	}

}
