package passwordmanager.communication;

import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;

public class Response<T> extends Message<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4474155900326959886L;

	public enum ResponseCode {
		OK,
		Fail,
		InvalidKey
	}
	
	private final ResponseCode responseCode;
	
	public Response(ResponseCode responseCode, CommunicationOperation operation, T data) {
		super(operation, data);
		this.responseCode = responseCode;
	}
	
	public ResponseCode getResponseCode() {
		return responseCode;
	}
}
