package passwordmanager;

import java.net.Socket;

import com.google.common.collect.Multimap;
import java.util.Map.Entry;

import passwordmanager.communication.CommunicationProtocol;
import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;
import passwordmanager.communication.PasswordServer;
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
			
			UserAccount testAccount = new UserAccount();
			System.out.println("Retrieving single credential!!");
			Credential credential = protocol.sendAndReceive(testAccount, CommunicationProtocol.CommunicationOperation.GetCredential);
			
			if (credential != null) {
				System.out.println("MAIN: Received credential! " + credential);
			}
			
			System.out.println("Retrieving multiple credentials!!");
			Multimap<CommunicationOperation, Object> credentials = protocol.sendAndReceiveMultiple(testAccount, CommunicationProtocol.CommunicationOperation.GetAllCredentials);
			System.out.println("Credentials received!");
			
			
			System.out.println("Main program Length of Credentials: " + credentials.size());
			if (credentials != null) {
				System.out.println("MAIN: Received credentials!");
				
				for (Entry<CommunicationOperation, Object> cred : credentials.entries()) {
					System.out.println(cred.getValue());
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
