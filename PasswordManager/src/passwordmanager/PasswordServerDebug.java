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
		
		PasswordServer server = new PasswordServer(config);
		
		Thread serverThread = new Thread(server);
		serverThread.start();
		
		System.out.println("Server started!");
		
		System.out.println("Server test!!");
		
		Socket clientSocket = null;
		
		try {
			Thread.sleep(1000);
			clientSocket = new Socket(config.serverIp, config.serverPort);
			CommunicationProtocol protocol = new CommunicationProtocol(clientSocket, CommunicationProtocol.ProtocolMode.Client);
			
			UserAccount testAccount = new UserAccount("", "");
			System.out.println("Retrieving single credential!!");
			
			Query<UserAccount> singleCredentialQuery = new Query<UserAccount>("", CommunicationOperation.GetCredential, testAccount); 
			
			Response<Credential> credentialResponse = protocol.sendAndReceive(singleCredentialQuery);
			
			System.out.println("Response code: " + credentialResponse.getResponseCode().toString());
			System.out.println("Response data: " + credentialResponse.getData());
			
			Credential credential = credentialResponse.getData();
			
			if (credential != null) {
				System.out.println("MAIN: Received credential! " + credential);
			}
			
			Query<UserAccount> multiCredentialQuery = new Query<UserAccount>("", CommunicationOperation.GetAllCredentials, testAccount); 
			
			System.out.println("Retrieving multiple credentials!!");
			Response<Credential[]> multiCredentialResponse = protocol.sendAndReceive(multiCredentialQuery);
			System.out.println("Credentials received!");
			
			Credential[] credentials = multiCredentialResponse.getData();
			
			System.out.println("Main program Length of Credentials: " + credentials.length);
			if (credentials != null) {
				System.out.println("MAIN: Received credentials!");
				
				for (Credential cred : credentials) {
					System.out.println(cred);
				}
			}
			
			clientSocket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		server.close();
		System.out.println("PasswordServerDebug finished executing!");
		
	}
}
