package passwordmanager.communication;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import passwordmanager.Credential;
import passwordmanager.PasswordDatabase;
import passwordmanager.UserAccount;
import passwordmanager.config.Configuration;

/**
 * 
 * @author Erik Wahlberger
 *
 */
public class PasswordServer implements Runnable {
	
	private Configuration config;
	private PasswordDatabase database;
	private ServerSocket serverSocket;
	private ArrayList<Socket> clientSockets;
	
	public PasswordServer(Configuration config) {
		this.config = config;
		
		this.clientSockets = new ArrayList<Socket>();
	}
	
	public void listen() {
		
	}
	
	
	private void addAccount(UserAccount account) {
		
	}
	
	private void addCredential(Credential credential) {
		
	}
	
	private void deleteAccount(UserAccount account) {
		
	}
	
	private boolean deleteAllPasswords(UserAccount account) {
		return true;
	}
	
	private void deleteCredential(Credential credential) {
		
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
	
	private void updateAccount(UserAccount account) {
		
	}
	
	private boolean isUserAuthorized(UserAccount account) {
		return false;
	}

	@Override
	public void run() {
		listen();
	}
}
