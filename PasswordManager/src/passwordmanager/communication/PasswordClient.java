package passwordmanager.communication;

import java.io.IOException;
import java.net.Socket;

import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;
import passwordmanager.communication.CommunicationProtocol.ProtocolMode;
import passwordmanager.communication.Response.ResponseCode;
import passwordmanager.config.Configuration;
import passwordmanager.model.Credential;
import passwordmanager.model.UserAccount;

public class PasswordClient {

	/**
	 * Used to send queries to the server and receive responses from the server 
	 * @author Yemeri Nisa
	 * @version 2021-03-07
	 * 
	 */

	private CommunicationProtocol protocol;
	/**
	 * Creates a new instance of the PasswordClient class, with the parameters
	 * specified in the Configuration object
	 * 
	 * @param config
	 * @throws IOException
	 */

	public PasswordClient(Configuration config) throws IOException {
		Socket socket = new Socket(config.serverIp, config.serverPort);
		protocol = new CommunicationProtocol(socket, ProtocolMode.Client);

	}

	/**
	 * Sends a query to search the database for all credentials for user account
	 * 
	 * @param account is the account whose credentials is retrieved
	 * @return list of credentials stored on server database.
	 */

	public Credential[] getCredentials(UserAccount account) {
		Query<UserAccount> query = new Query<UserAccount>(CommunicationOperation.GetAllCredentials, account);
		Response<Credential[]> response = protocol.sendAndReceive(query);
		return response.getData();

	}
	/**
	 * Sends a query to store a credential in the database
	 * 
	 * @param credential is the credential that shall be stored
	 * @return returns the success of the operation
	 */


	public boolean storeCredential(Credential credential) {
		Query<Credential> query = new Query<Credential>(CommunicationOperation.AddCredential, credential);
		Response<Boolean> response = protocol.sendAndReceive(query);

		return response.getData();

	}
	
	/**
	 * Sends a query to update the database on the specified credential
	 * 
	 * @param credential is the credential that shall be updated
	 * @return returns the success of the operation
	 */
	public boolean modifyCredential(Credential credential) {
		Query<Credential> query = new Query<Credential>(CommunicationOperation.UpdateCredential, credential);
		Response<Boolean> response = protocol.sendAndReceive(query);

		return response.getData();

	}
	
	/**
	 * Sends a query to delete a credential from the database
	 * 
	 * @param credential that shall be deleted
	 * @return returns the success of the operation
	 */
	public boolean deleteCredential(Credential credential) {
		Query<Credential> query = new Query<Credential>(CommunicationOperation.DeleteCredential, credential);
		Response<Boolean> response = protocol.sendAndReceive(query);
		return response.getData();

	}

	/**
	 * Method below verifies User's account.
	 * 
	 * @param account is to be verified with the stored information on the server
	 *                database.
	 * @return true if the UserAccount is successfully stored, otherwise false. Same
	 *         principle applies to classes storeUserAccount, modifyAccount &
	 *         deleteAccount.
	 */

	public boolean verifyUser(UserAccount account) {

		Query<UserAccount> query = new Query<UserAccount>(CommunicationOperation.VerifyUser, account);
		Response<Boolean> response = protocol.sendAndReceive(query);
		return response.getData();

	}

	/**
	 * Sends a query to add a UserAccount to the database
	 * 
	 * @param account that shall be added
	 * @return returns the success of the operation
	 */
	public boolean addUserAccount(UserAccount account) {
		Query<UserAccount> query = new Query<UserAccount>(CommunicationOperation.AddUser, account);
		Response<Boolean> response = protocol.sendAndReceive(query);
		return response.getData();

	}
	
	/**
	 * Sends a query to update a UserAccount from the database
	 * 
	 * @param account that shall be updated
	 * @return returns the success of the operation
	 */
	public boolean modifyUserAccount(UserAccount account) {

		Query<UserAccount> query = new Query<UserAccount>(CommunicationOperation.UpdateUser, account);
		Response<Boolean> response = protocol.sendAndReceive(query);
		return response.getData();
	}
	
	/**
	 * Sends a query to delete a UserAccount from the database
	 * 
	 * @param account that shall be deleted
	 * @return returns the success of the operation
	 */
	public boolean deleteUserAccount(UserAccount account) {

		Query<UserAccount> query = new Query<UserAccount>(CommunicationOperation.DeleteUser, account);
		Response<Boolean> response = protocol.sendAndReceive(query);
		return response.getData();

	}
	/**
	 * Sends a query to retrieve a UserAccount from the database
	 * 
	 * @param email of the UserAccount that shall be retrieved
	 * @return returns the success of the operation
	 */
	public UserAccount getUserAccount(String email) {
		UserAccount account = new UserAccount(email, "");
		Query<UserAccount> query = new Query<UserAccount>(CommunicationOperation.GetUser, account);
		Response<UserAccount> response = protocol.sendAndReceive(query);

		return response.getData();
	}
	/**
	 * Sends a query that the account has forgotten password to the server
	 * 
	 * @param email of the account
	 * @return returns the success of the operation
	 */
	public boolean forgotPassword(String email) throws Exception {
		Query<UserAccount> query = new Query<UserAccount>(CommunicationOperation.ForgotPassword,
				new UserAccount(email, ""));
		Response<Boolean> response = protocol.sendAndReceive(query);

		if (response.getResponseCode() != ResponseCode.OK) {
			throw new Exception("The query did not complete successfully!");
		}

		return response.getData();
	}

}
