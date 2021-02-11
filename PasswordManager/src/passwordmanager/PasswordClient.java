package passwordmanager;

public class PasswordClient {
	
/**	
 * 
 *  
 * @author yemeri 
 *  
 */
	
/** Public classes because the PasswordController has to be able to call on them **/
	
	
	
	
//	Initiate communication with server
	public void ActivateServerNetwork() {
		
	}
	
/**	Code below regards Login instructions for UserAccount. Send & recieve Login information **/
	
	public UserAccount LoginManipulation (UserAccount C) {
		
		
	}
	
/**	Code below regards Credentials. Manipulates user passwords (Store/Retrieve/Modify/Delete) using PasswordServer, UserAcc, PasswordDatabase & Credentials. For passwords stored inside the Password manager **/
	
	
	public Credential GetCredential (UserAccount C) {
		
	} 
	
	
	public boolean StoreCredential (Credential C) {
		
		
	}
	
	public boolean ModifyCredential (Credential C) {
		
	}
	
	public boolean DeleteCredential (Credential C) {
		
		
	}
	
/**	Manipulates user account (Store/Retrieve/Modify/Delete) using UserAcc, PasswordServer and PasswordDatabase Credentials. For account used to login **/
//	to the password manager interface.
	
	public void UserAccountManipulation () {
		
	}
	
	

}
