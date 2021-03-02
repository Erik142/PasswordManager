package passwordmanager;

import java.io.IOException;
import java.net.Socket;

import passwordmanager.communication.CommunicationProtocol;
import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;
import passwordmanager.communication.CommunicationProtocol.ProtocolMode;
import passwordmanager.communication.Query;
import passwordmanager.communication.Response;
import passwordmanager.communication.Response.ResponseCode;
import passwordmanager.config.Configuration;



public class PasswordClient {
	
/**	
 * 
 *  
 * @author Yemeri 
 *  
 */
	
	
	private CommunicationProtocol protocol; 
	
	public PasswordClient(Configuration config) throws IOException {
		Socket socket = new Socket(config.serverIp, config.serverPort);
		protocol = new CommunicationProtocol(socket, ProtocolMode.Client);
	
	}
	
	
	
	
/**
 * 
 * @param credential is searched for on server database.
 * @return credential stored on server database.
 */
	
	
	public Credential[] getCredentials (UserAccount account) {
		Query<UserAccount> query = new Query<UserAccount>("", 
				CommunicationOperation.GetAllCredentials, account);
		Response<Credential[]> response = protocol.sendAndReceive(query);
		return response.getData();
		
	} 
	
	
	public boolean storeCredential (Credential credential) {
		Query<Credential> query = new Query<Credential>("", 
				CommunicationOperation.AddCredential, credential);
		Response<Boolean> response = protocol.sendAndReceive(query);
		
		return response.getData();
		
	}
	
	public boolean modifyCredential (Credential credential) {
		Query<Credential> query = new Query<Credential>("", 
				CommunicationOperation.UpdateCredential, credential);
		Response<Boolean> response = protocol.sendAndReceive(query);
		
		return response.getData();
		
	}
	
	public boolean deleteCredential (Credential credential) {
		Query<Credential> query = new Query<Credential>("", 
				CommunicationOperation.DeleteCredential, credential);
		Response<Boolean> response = protocol.sendAndReceive(query);
		return response.getData();
		
	}
	
	/**
	 * Class below verifies User's account.
	 * @param account is to be verified with the stored information on the server database.
	 * @return true if the UserAccount is successfully stored, otherwise false.
	 * Same principle applies to classes storeUserAccount, modifyAccount & deleteAccount.
	 */

	
public boolean verifyUser (UserAccount account) {
	
	Query<UserAccount> query = new Query<UserAccount>("", 
			CommunicationOperation.VerifyUser, account);
	Response<Boolean> response = protocol.sendAndReceive(query);
	return response.getData();

	} 
	
	
	public boolean addUserAccount (UserAccount account) {
		Query<UserAccount> query = new Query<UserAccount>("", CommunicationOperation.AddUser, account);
		Response<Boolean> response = protocol.sendAndReceive(query);
		return response.getData();
		
		
	}
	
	public boolean modifyUserAccount (UserAccount account) {
		
		Query<UserAccount> query = new Query<UserAccount>("", CommunicationOperation.UpdateUser, account);
		Response<Boolean> response = protocol.sendAndReceive(query);
		return response.getData();
	}
	
	public boolean deleteUserAccount (UserAccount account) {
		
		Query<UserAccount> query = new Query<UserAccount>("", CommunicationOperation.DeleteUser, account);
		Response<Boolean> response = protocol.sendAndReceive(query);
		return response.getData();
		
	}
	
	public UserAccount getUserAccount(String email) {
		UserAccount account = new UserAccount(email, "");
		Query<UserAccount> query = new Query<UserAccount>("", CommunicationOperation.GetUser, account);
		Response<UserAccount> response = protocol.sendAndReceive(query);
		
		return response.getData();
	}
	
	public boolean forgotPassword(String email) throws Exception {
		Query<UserAccount> query = new Query<UserAccount>("", CommunicationOperation.ForgotPassword, new UserAccount(email, ""));
		Response<Boolean> response = protocol.sendAndReceive(query);
		
		if (response.getResponseCode() != ResponseCode.OK) {
			throw new Exception("The query did not complete successfully!");
		}
		
		return response.getData();
	}

}
