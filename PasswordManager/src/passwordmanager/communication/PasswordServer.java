package passwordmanager.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import passwordmanager.Credential;
import passwordmanager.PasswordDatabase;
import passwordmanager.UserAccount;
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
	private ArrayList<Thread> clientThreads;
	
	public PasswordServer(Configuration config) {
		this.config = config;
		
		this.clientThreads = new ArrayList<Thread>();
	}
	
	public void listen() {
		try {
			if (!StringExtensions.isNullOrEmpty(config.serverIp.getHostAddress())) {
				serverSocket = new ServerSocket(config.serverPort, MAX_PENDING_CONNECTIONS, config.serverIp);
			}
			else {
				serverSocket = new ServerSocket(config.serverPort, MAX_PENDING_CONNECTIONS);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		while(true) {
			try {
				Socket clientSocket = serverSocket.accept();
				
				Thread clientThread = new Thread(() -> {
					processClient(clientSocket);
				});
				
				clientThread.start();
				
				clientThreads.add(clientThread);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void processClient(Socket client) {
		
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
	
	private Credential getCredential(int id) {
		return null;
	}
	
	private Credential[] getCredentials(UserAccount account) {
		return null;
	}
	
	private boolean updateAccount(UserAccount account) {
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
