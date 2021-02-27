package passwordmanager.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import passwordmanager.Credential;
import passwordmanager.PasswordDatabase;
import passwordmanager.UserAccount;
import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;
import passwordmanager.communication.Response.ResponseCode;
import passwordmanager.config.Configuration;
import passwordmanager.util.EmailUtil;
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
		this.database = new PasswordDatabase(config);
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
				ResponseCode responseCode = ResponseCode.OK;
				
				Object returnValue = null;
				Boolean result = false;
				
				switch (operation) {
				case AddCredential:
					result = addCredential(credential);
					returnValue = result;
					
					responseCode = result == true ? ResponseCode.OK : ResponseCode.Fail;
					break;
				case DeleteCredential:
					result = deleteCredential(credential);
					returnValue = result;
					
					responseCode = result == true ? ResponseCode.OK : ResponseCode.Fail;
					break;
				case UpdateCredential:
					result = updateCredential(credential);
					returnValue = result;
					
					responseCode = result == true ? ResponseCode.OK : ResponseCode.Fail;
					break;
				default:
					responseCode = ResponseCode.Fail;
					break;
				}
				
				Response<Object> serverResponse = new Response<Object>(responseCode, operation, returnValue);
				communicationProtocol.send(serverResponse);
			}

			@Override
			public void onUserAccountEvent(UserAccount userAccount, CommunicationOperation operation) {
				ResponseCode responseCode = ResponseCode.OK;
				
				Object returnValue = null;
				Boolean result = true;
				
				switch (operation) {
				case AddUser:
					returnValue = addAccount(userAccount);
					
					responseCode = (boolean)returnValue == true ? ResponseCode.OK : ResponseCode.Fail;
					
					break;
				case DeleteUser:
					result &= deleteAllPasswords(userAccount);
					result &= deleteAccount(userAccount);
					returnValue = result;
					
					responseCode = result == true ? ResponseCode.OK : ResponseCode.Fail;
					break;
				case GetAllCredentials:
					try {
						returnValue = getCredentials(userAccount);
					} catch (SQLException e) {
						responseCode = ResponseCode.Fail;
					}
					break;
				case GetUser:
					try {
						returnValue = getAccount(userAccount.getEmail());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						responseCode = ResponseCode.Fail;
					}
					break;
				case UpdateUser:
					result = updateAccount(userAccount);
					returnValue = result;
					
					responseCode = result == true ? ResponseCode.OK : ResponseCode.Fail;
					break;
				case VerifyUser:
					returnValue = isUserAuthorized(userAccount);
					break;
				case ForgotPassword:
					result = forgotPassword(userAccount);
					returnValue = result;
				default:
					responseCode = ResponseCode.Fail;
					break;
				}
				
				Response<Object> serverResponse = new Response<Object>(responseCode, operation, returnValue);
				communicationProtocol.send(serverResponse);
			}
		});
	}
	
	private boolean addAccount(UserAccount account) {
		try {
			database.addAccount(account);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	private boolean addCredential(Credential credential) {
		try {
			database.addCredential(credential);
			return true;
		} catch (SQLException exadd) {
		return false;
		}
	}
	
	private boolean deleteAccount(UserAccount account) {
		try {
			database.deleteAccount(account);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	private boolean deleteAllPasswords(UserAccount account) {
		return true;
	}
	
	private boolean deleteCredential(Credential credential) {
		return false;
	}
	
	private boolean forgotPassword(UserAccount account) {
		try {
			database.insertResetRequest(account);
			int requestId = database.getResetRequestId(account);
			
			if (requestId <= 0) {
				return false;
			}
			
			System.out.println("Reset password request id: " + requestId);
			
			// Configure smtp properties with ssl authentication
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			
			System.out.println("Server e-mail: " + config.serverEmail);
			System.out.println("Server e-mail password: " + config.serverPassword);
			
			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(config.serverEmail, config.serverPassword);
				}
			};
			
			String publicDomainName = config.publicDomainName;
			
			System.out.println("Public domain name: " + publicDomainName);
			
			if (StringExtensions.isNullOrEmpty(publicDomainName)) {
				System.out.println("Public domain name is empty!");
				return false;
			}
			
			URL baseUrl = new URL("https://" + publicDomainName);
			URL finalUrl = new URL(baseUrl, "" + requestId);
			
			Session session = Session.getDefaultInstance(props, auth);
			EmailUtil.sendEmail(session, config.serverEmail, account.getEmail(), "PasswordManager: Reset password", "A password reset was requested for this user account. Press the following link to reset your password:\r\n\r\n" + finalUrl.toString());
			
			System.out.println("Recipient e-mail: " + account.getEmail());
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private UserAccount getAccount(String email) throws SQLException {
		return database.getAccount(email);
	}
	
	private Credential[] getCredentials(UserAccount account) throws SQLException {
		return new Credential[] {
				new Credential("", "", "", ""),
				new Credential("", "", "", ""),
				new Credential("", "", "", "")
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
