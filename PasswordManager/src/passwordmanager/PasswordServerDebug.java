package passwordmanager;

import java.net.Socket;

import passwordmanager.communication.CommunicationProtocol;
import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;
import passwordmanager.communication.PasswordServer;
import passwordmanager.communication.Query;
import passwordmanager.communication.Response;
import passwordmanager.config.Configuration;

public class PasswordServerDebug {
	public PasswordServerDebug(Configuration config) {
		run(config);
	}
	
	private void run(Configuration config) {
		System.out.println("Starting new server...");
		
		PasswordServer server;
		
		try {
			server = new PasswordServer(config);
			
			Thread serverThread = new Thread(server);
			serverThread.start();
			
			System.out.println("Server started!");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println("Server test!!");
		
		Socket clientSocket = null;
		
		try {
			Thread.sleep(1000);
			clientSocket = new Socket(config.serverIp, config.serverPort);
			CommunicationProtocol protocol = new CommunicationProtocol(clientSocket, CommunicationProtocol.ProtocolMode.Client);
			
			UserAccount testAccount = new UserAccount("eriktest@test.se", "password123");
			System.out.println("Adding user account to database!!");
			
			Query<UserAccount> addUserQuery = new Query<UserAccount>("", CommunicationOperation.AddUser, testAccount); 
			
			Response<Boolean> addUserResponse = protocol.sendAndReceive(addUserQuery);
			
			System.out.println("Response code: " + addUserResponse.getResponseCode().toString());
			System.out.println("Response data: " + addUserResponse.getData());
			
			Boolean addUserResult = addUserResponse.getData();
			
			if (addUserResult != null) {
				System.out.println("MAIN: Received credential! " + addUserResult);
			}
			
			Query<UserAccount> getUserAccountQuery = new Query<UserAccount>("", CommunicationOperation.GetUser, testAccount); 
			
			System.out.println("Retrieving user account!!");
			Response<UserAccount> getUserAccountResponse = protocol.sendAndReceive(getUserAccountQuery);
			System.out.println("User account received!");
			System.out.println("Response code: " + getUserAccountResponse.getResponseCode().toString());
			
			
			UserAccount userFromDb = getUserAccountResponse.getData();
			
			System.out.println("User email: " + userFromDb.getEmail() + ", user password: " + userFromDb.getPassword());
			
			Query<UserAccount> deleteUserAccountQuery = new Query<UserAccount>("", CommunicationOperation.DeleteUser, testAccount);
			
			System.out.println("Deleting user account!!");
			Response<Boolean> deleteUserAccountResponse = protocol.sendAndReceive(deleteUserAccountQuery);
			System.out.println("User account deleted!");
			System.out.println("Response code: " + getUserAccountResponse.getResponseCode().toString());
			
			Boolean isUserDeleted = deleteUserAccountResponse.getData();
			System.out.println("Is user deleted: " + isUserDeleted);
			
			clientSocket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		server.close();
		System.out.println("PasswordServerDebug finished executing!");
		
	}
}
