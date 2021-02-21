package passwordmanager;

import passwordmanager.communication.CommunicationProtocol;
import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;
import passwordmanager.communication.Query;
import passwordmanager.communication.Response;

public class PasswordClient {
	
/**	
 * 
 *  
 * @author Yemeri 
 *  
 */
	
/** Public classes because the PasswordController has to be able to call on them **/
	
	
	
	
//	Initiate communication with server
	public void ActivateServerNetwork() {
		//Use items from configuration file here. Use Socket
		
	}
	
	
	private CommunicationProtocol Protocol; 
	
	
/**	Code below regards Login instructions for UserAccount. Send & receive Login information **/
	
	public UserAccount LoginManipulation (UserAccount account) {
		return account;
		
		
	}
	
/**	Code below regards Credentials. Manipulates user passwords (Store/Retrieve/Modify/Delete) using PasswordServer, UserAcc, PasswordDatabase & Credentials. For passwords stored inside the Password manager **/
	
	
	public Credential getCredential (UserAccount account) {
		Query<UserAccount> query = new Query<UserAccount>("", CommunicationOperation.GetCredential, account);
		Response<Credential> response = Protocol.sendAndReceive(query);
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
	
public String verifyUser (UserAccount account) {
	//*Not sure if Credentials is the credential for the individual or not* Not sure about the email and password part//
	
	Query<UserAccount> query = new Query<UserAccount>("", CommunicationOperation.VerifyUser, account);
	Response<String> response = Protocol.sendAndReceive(query);
	return response.getData();

	} 
	
	
	public boolean StoreUserAccount (UserAccount account) {
		Query<UserAccount> query = new Query<UserAccount>("", CommunicationOperation.AddUser, account);
		Response<UserAccount> response = Protocol.sendAndReceive(query);
		return response.getData() != null;
		
		
	}
	
	public boolean modifyUserAccount (UserAccount account) {
		
		Query<UserAccount> query = new Query<UserAccount>("", CommunicationOperation.UpdateUser, account);
		Response<Credential> response = Protocol.sendAndReceive(query);
		return response.getData() != null;
	}
	
	public boolean deleteUserAccount (UserAccount account) {
		
		Query<UserAccount> query = new Query<UserAccount>("", CommunicationOperation.DeleteUser, account);
		Response<Credential> response = Protocol.sendAndReceive(query);
		return response.getData() != null;
		
	}
	
	

}
