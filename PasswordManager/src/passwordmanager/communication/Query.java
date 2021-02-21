package passwordmanager.communication;

import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;

public class Query<T> extends Message<T> {
	private final String authToken;
	
	public Query(String authToken, CommunicationOperation operation) {
		this(authToken, operation, null);
	}
	
	public Query(String authToken, CommunicationOperation operation, T data) {
		super(operation, data);
		this.authToken = authToken;
	}
	
	public String getAuthToken() {
		return authToken;
	}
	
}
