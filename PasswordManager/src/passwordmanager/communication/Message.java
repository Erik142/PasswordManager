package passwordmanager.communication;

import java.io.Serializable;

import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;

/**
 * Abstract class used to represent Messages that are sent and received between
 * the client and server
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public abstract class Message<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3037518128810042924L;
	/**
	 * The data in the message
	 */
	protected final T data;
	/**
	 * The operation for the message
	 */
	protected final CommunicationOperation operation;

	/**
	 * Creates a new instance of a Message object for the specified
	 * CommunicationOperation and with the specified data
	 * 
	 * @param operation The CommunicationOperation
	 * @param data      The data
	 */
	public Message(CommunicationOperation operation, T data) {
		this.operation = operation;
		this.data = data;
	}

	/**
	 * Retrieves the data
	 * 
	 * @return The data
	 */
	public T getData() {
		return this.data;
	}

	/**
	 * Retrieves the CommunicationOperation
	 * 
	 * @return The CommunicationOperation
	 */
	public CommunicationOperation getOperation() {
		return this.operation;
	}
}
