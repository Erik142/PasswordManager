package passwordmanager;

import passwordmanager.communication.CommunicationProtocol;

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
	
	
	public Credential getCredential (UserAccount C) {
		return null;
		
	} 
	
	
	public boolean storeCredential (Credential credential) {
		return false;
		
	}
	
	public boolean modifyCredential (Credential credential) {
		return false;
		
	}
	
	public boolean deleteCredential (Credential credential) {
		return false;
		
	}
	
/**	Manipulates user account (Store/Retrieve/Modify/Delete) using UserAcc, PasswordServer and PasswordDatabase Credentials. For account used to login **/
//	to the password manager interface.
	
public Credential getCredential (String email, String password) {
		return null; 
	} 
	
	
	public boolean StoreUserAccount (UserAccount account) {
		return false;
		
	}
	
	public boolean modifyUserAccount (UserAccount account) {
		return false; 
	}
	
	public boolean deleteUserAccount (UserAccount account) {
		return false;
		
	}
	
	

}
