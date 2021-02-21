package passwordmanager;

import java.io.IOException;
import java.net.Socket;

import passwordmanager.communication.CommunicationProtocol;
import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;
import passwordmanager.communication.CommunicationProtocol.ProtocolMode;
import passwordmanager.communication.Query;
import passwordmanager.communication.Response;
import passwordmanager.config.Configuration;



public class PasswordClient {
	
/**	
 * 
 *  
 * @author Yemeri 
 *  
 */
	
/** Public classes because the PasswordController has to be able to call on them **/
	
	private CommunicationProtocol Protocol; 
	
	public PasswordClient(Configuration config) throws IOException {
		Socket socket = new Socket(config.serverIp, config.serverPort);
		Protocol = new CommunicationProtocol(socket, ProtocolMode.Client);
	
	}
	
	
	
	
/**	Code below regards Credentials. Manipulates user passwords (Store/Retrieve/Modify/Delete) using PasswordServer, UserAcc, PasswordDatabase & Credentials. For passwords stored inside the Password manager **/
	
	
	public Credential[] getCredential (UserAccount account) {
		Query<UserAccount> query = new Query<UserAccount>("", CommunicationOperation.GetAllCredentials, account);
		Response<Credential[]> response = Protocol.sendAndReceive(query);
		return response.getData();
		
	} 
	
	
	public boolean storeCredential (Credential credential) {
		Query<Credential> query = new Query<Credential>("", CommunicationOperation.AddCredential, credential);
		Response<Boolean> response = Protocol.sendAndReceive(query);
		
		return response.getData();
		
	}
	
	public boolean modifyCredential (Credential credential) {
		Query<Credential> query = new Query<Credential>("", CommunicationOperation.UpdateCredential, credential);
		Response<Credential> response = Protocol.sendAndReceive(query);
		return response.getData() != null;
		
	}
	
	public boolean deleteCredential (Credential credential) {
		Query<Credential> query = new Query<Credential>("", CommunicationOperation.DeleteCredential, credential);
		Response<Credential> response = Protocol.sendAndReceive(query);
		return response.getData() != null;
		
	}
	
/**	Manipulates user account (Store/Retrieve/Modify/Delete) using UserAcc, PasswordServer and PasswordDatabase Credentials. For account used to login **/
//	to the password manager interface.
	
public boolean verifyUser (UserAccount account) {
	
	Query<UserAccount> query = new Query<UserAccount>("", CommunicationOperation.VerifyUser, account);
	Response<Boolean> response = Protocol.sendAndReceive(query);
	return response.getData();

	} 
	
	
	public boolean StoreUserAccount (UserAccount account) {
		Query<UserAccount> query = new Query<UserAccount>("", CommunicationOperation.AddUser, account);
		Response<Boolean> response = Protocol.sendAndReceive(query);
		return response.getData();
		
		
	}
	
	public boolean modifyUserAccount (UserAccount account) {
		
		Query<UserAccount> query = new Query<UserAccount>("", CommunicationOperation.UpdateUser, account);
		Response<Boolean> response = Protocol.sendAndReceive(query);
		return response.getData();
	}
	
	public boolean deleteUserAccount (UserAccount account) {
		
		Query<UserAccount> query = new Query<UserAccount>("", CommunicationOperation.DeleteUser, account);
		Response<Boolean> response = Protocol.sendAndReceive(query);
		return response.getData();
		
	}
	
	

}
