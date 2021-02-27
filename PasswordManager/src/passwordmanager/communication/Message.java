package passwordmanager.communication;

import java.io.Serializable;

import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;

public abstract class Message<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3037518128810042924L;
	protected final T data;
	protected final CommunicationOperation operation;
	
	public Message(CommunicationOperation operation, T data) {
		this.operation = operation;
		this.data = data;
	}
	
	public T getData() {
		return this.data;
	}
	
	public CommunicationOperation getOperation() {
		return this.operation;
	}
}
