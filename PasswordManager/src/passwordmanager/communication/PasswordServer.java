package passwordmanager.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import passwordmanager.Credential;
import passwordmanager.PasswordDatabase;
import passwordmanager.UserAccount;
import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;
import passwordmanager.communication.Response.ResponseCode;
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
					break;
				default:
					break;
				}
				
				if (returnValue != null) {
					Response<Object> serverResponse = new Response<Object>(ResponseCode.OK, operation, returnValue);
					communicationProtocol.send(serverResponse);
				}
			}

			@Override
			public void onUserAccountEvent(UserAccount userAccount, CommunicationOperation operation) {
				// TODO Auto-generated method stub
				
				Response<Object> response;
				
				Object returnValue = null;
				
				switch (operation) {
				case AddUser:
					returnValue = addAccount(userAccount);
					break;
				case DeleteUser:
					boolean result = true;
					result &= deleteAllPasswords(userAccount);
					result &= deleteAccount(userAccount);
					returnValue = result;
					break;
				case GetCredential:
					returnValue = getCredential(userAccount);
					break;
				case GetAllCredentials:
					returnValue = getCredentials(userAccount);
					break;
				case GetUser:
					// TODO: Implement this method with actual user name and password values
					returnValue = getAccount(null, null);
					break;
				case UpdateUser:
					returnValue = updateAccount(userAccount);
					break;
				case VerifyUser:
					returnValue = isUserAuthorized(userAccount);
					break;
				default:
					break;
				}
				
				if (returnValue != null) {
					
					Response<Object> serverResponse = new Response<Object>(ResponseCode.OK, operation, returnValue);
					
					communicationProtocol.send(serverResponse);
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
