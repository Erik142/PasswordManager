package passwordmanager.communication;

import java.io.Serializable;

import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;

public abstract class Message<T> implements Serializable {
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