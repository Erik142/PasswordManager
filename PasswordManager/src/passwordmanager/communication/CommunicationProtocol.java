package passwordmanager.communication;


import java.io.Serializable;
import java.net.Socket;

import passwordmanager.Credential;
import passwordmanager.UserAccount;

/**
 * 
 * @author Erik Wahlberger
 *
 */
public class CommunicationProtocol implements Serializable {
	public enum CommunicationOperation {
		AddCredential,
		DeleteCredential,
		UpdateCredential,
		GetCredential,
		AddUser,
		DeleteUser,
		UpdateUser,
		GetUser,
		VerifyUser
	}
	
	public CommunicationProtocol(Socket socket) {
		
	}
	
	public void sendCredential(Credential credential, CommunicationOperation operation) {
		
	}
	
	public void sendUserAccount(UserAccount userAccount, CommunicationOperation operation) {
		
	}
	
	public void subscribe(CommunicationEventListener eventListener) {
		
	}
}
