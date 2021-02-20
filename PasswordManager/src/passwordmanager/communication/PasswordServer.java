package passwordmanager.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import passwordmanager.Credential;
import passwordmanager.PasswordDatabase;
import passwordmanager.UserAccount;
import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;
import passwordmanager.config.Configuration;
import passwordmanager.util.StringExtensions;

/**
 * 
 * @author Erik Wahlberger
 *
 */
public class PasswordServer implements Runnable {
	private final int MAX_PENDING_CONNECTIONS = 10;
	
	private Configuration config;
	private PasswordDatabase database;
	private ServerSocket serverSocket;
	
	public PasswordServer(Configuration config) {
		this.config = config;
	}
	
	protected void finalize() {
		close();
	}
	
	public void listen() {
		try {
			serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			
			if (!StringExtensions.isNullOrEmpty(config.serverIp.getHostAddress())) {
				serverSocket.bind(new InetSocketAddress(config.serverIp, config.serverPort), MAX_PENDING_CONNECTIONS);
			}
			else {
				serverSocket.bind(new InetSocketAddress(config.serverPort), MAX_PENDING_CONNECTIONS);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		while(!serverSocket.isClosed()) {
			try {
				Socket clientSocket = serverSocket.accept();
				
				Thread clientThread = new Thread(() -> {
					processClient(clientSocket);
				});
				
				clientThread.start();
			} catch (IOException e) {
			}
		}
	}
	
	public void close() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void processClient(Socket client) {
		CommunicationProtocol communicationProtocol = new CommunicationProtocol(client, CommunicationProtocol.ProtocolMode.Server);
		
		communicationProtocol.subscribeOnSocket(new CommunicationEventListener() {
			@Override
			public void onCredentialEvent(Credential credential, CommunicationOperation operation) {
				Object returnValue = null;
				
				switch (operation) {
				case AddCredential:
					returnValue = addCredential(credential);
					break;
				case DeleteCredential:
					returnValue = deleteCredential(credential);
					break;
				case UpdateCredential:
					returnValue = updateCredential(credential);
				}
				
				if (returnValue != null) {
					communicationProtocol.send(returnValue, operation);
				}
			}

			@Override
			public void onUserAccountEvent(UserAccount userAccount, CommunicationOperation operation) {
				// TODO Auto-generated method stub
				Object[] returnValue = new Object[1];
				
				switch (operation) {
				case AddUser:
					returnValue[0] = addAccount(userAccount);
					break;
				case DeleteUser:
					boolean result = true;
					result &= deleteAllPasswords(userAccount);
					result &= deleteAccount(userAccount);
					returnValue[0] = result;
					break;
				case GetCredential:
					returnValue[0] = getCredential(userAccount);
					break;
				case GetAllCredentials:
					returnValue = getCredentials(userAccount);
					break;
				case GetUser:
					// TODO: Implement this method with actual user name and password values
					returnValue[0] = getAccount(null, null);
					break;
				case UpdateUser:
					returnValue[0] = updateAccount(userAccount);
					break;
				case VerifyUser:
					returnValue[0] = isUserAuthorized(userAccount);
					break;
				}
				
				if (returnValue != null) {
					communicationProtocol.send(returnValue, operation);
				}
			}
		});
	}
	
	private boolean addAccount(UserAccount account) {
		return false;
	}
	
	private boolean addCredential(Credential credential) {
		return false;
	}
	
	private boolean deleteAccount(UserAccount account) {
		return false;
	}
	
	private boolean deleteAllPasswords(UserAccount account) {
		return true;
	}
	
	private boolean deleteCredential(Credential credential) {
		return false;
	}
	
	private UserAccount getAccount(String username, String passwordHash) {
		return null;
	}
	
	private Credential getCredential(UserAccount userAccount) {
		return new Credential();
	}
	
	private Credential[] getCredentials(UserAccount account) {
		return new Credential[] {
				new Credential(),
				new Credential(),
				new Credential()
		};
	}
	
	private boolean updateAccount(UserAccount account) {
		return false;
	}
	
	private boolean updateCredential(Credential credential) {
		return false;
	}
	
	private boolean isUserAuthorized(UserAccount account) {
		return false;
	}

	@Override
	public void run() {
		listen();
	}
}
