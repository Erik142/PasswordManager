package passwordmanager.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import passwordmanager.communication.CommunicationProtocol.CommunicationOperation;
import passwordmanager.communication.Response.ResponseCode;
import passwordmanager.config.Configuration;
import passwordmanager.model.Credential;
import passwordmanager.model.UserAccount;
import passwordmanager.util.EmailUtil;
import passwordmanager.util.StringExtensions;

/**
 * 
 * @author Erik Wahlberger
 * @author Yemeri Nisa
 * @version 2021-03-07
 *
 */
public class PasswordServer implements Runnable {
	private final int MAX_PENDING_CONNECTIONS = 10;

	private Configuration config;
	private PasswordDatabase database;
	private ServerSocket serverSocket;
	
	/**
	 * Creates a new instance of the PasswordServer class, with the parameters
	 * specified in the Configuration object
	 * @param config
	 * @throws Exception
	 */
	public PasswordServer(Configuration config) throws Exception {
		this.config = config;
		this.database = new PasswordDatabase(config);
	}
	
	/**
	 * Closes the server
	 */
	protected void finalize() {
		close();
	}
	
	/**
	 * Initializes the server to be able to connect to clients
	 */
	public void listen() {
		try {
			serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);

			if (!StringExtensions.isNullOrEmpty(config.serverIp.getHostAddress())) {
				serverSocket.bind(new InetSocketAddress(config.serverIp, config.serverPort), MAX_PENDING_CONNECTIONS);
			} else {
				serverSocket.bind(new InetSocketAddress(config.serverPort), MAX_PENDING_CONNECTIONS);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		while (!serverSocket.isClosed()) {
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
	
	/**
	 * Closes the server's socket
	 */
	public void close() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Successfully connects the client for communication once connection has been established
	 * @param client
	 */
	private void processClient(Socket client) {
		CommunicationProtocol communicationProtocol = new CommunicationProtocol(client,
				CommunicationProtocol.ProtocolMode.Server);

		communicationProtocol.subscribeOnSocket(new CommunicationEventListener() {
			@Override
			public void onCredentialEvent(Credential credential, CommunicationOperation operation) {
				try {
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
				} catch (Exception e) {
					e.printStackTrace();
					Response<Object> failedResponse = new Response<Object>(ResponseCode.Fail, operation, null);
				}
			}
			
			/**
			 * This method is used to see what operation was called by the user client-side
			 * 
			 * @param userAccount
			 * @param operation
			 */
			@Override
			public void onUserAccountEvent(UserAccount userAccount, CommunicationOperation operation) {
				try {
					ResponseCode responseCode = ResponseCode.OK;

					Object returnValue = null;
					Boolean result = true;

					switch (operation) {
					case AddUser:
						returnValue = addAccount(userAccount);

						responseCode = (boolean) returnValue == true ? ResponseCode.OK : ResponseCode.Fail;

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
					case ForgotPassword:
						result = forgotPassword(userAccount);
						returnValue = result;
						break;
					default:
						responseCode = ResponseCode.Fail;
						break;
					}

					Response<Object> serverResponse = new Response<Object>(responseCode, operation, returnValue);
					communicationProtocol.send(serverResponse);
				} catch (Exception e) {
					e.printStackTrace();
					Response<Object> failedResponse = new Response<Object>(ResponseCode.Fail, operation, null);
				}
			}
		});
	}
	
	/**
	 * Sends a query to add a UserAccount to the database
	 * 
	 * @param account that shall be added
	 * @return returns the success of the operation
	 */
	private boolean addAccount(UserAccount account) {
		try {
			database.addAccount(account);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * Sends a query to store a credential in the database
	 * 
	 * @param credential is the credential that shall be stored
	 * @return returns the success of the operation
	 */
	private boolean addCredential(Credential credential) {
		try {
			database.addCredential(credential);
			return true;
		} catch (SQLException exadd) {
			exadd.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Sends a query to delete a UserAccount from the database
	 * 
	 * @param account that shall be deleted
	 * @return returns the success of the operation
	 */
	private boolean deleteAccount(UserAccount account) {
		try {
			database.deleteAccount(account);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * Sends a query to delete all credentials for account
	 * 
	 * @param account whose credentials shall be deleted
	 * @return returns the success of the operation
	 */
	private boolean deleteAllPasswords(UserAccount account) {
		try {
			database.deleteAllCredentials(account);
			return true;
		} catch (SQLException eAll) {
			return false;
		}
	}
	
	/**
	 * Sends a query to delete a credential from the database
	 * 
	 * @param credential that shall be deleted
	 * @return returns the success of the operation
	 */
	private boolean deleteCredential(Credential credential) {
		try {
			database.deleteCredential(credential);
			return true;
		} catch (SQLException exdel) {
			return false;
		}
	}
	
	/**
	 * Sends a query that the account has forgotten password to the server
	 * 
	 * @param email of the account
	 * @return returns the success of the operation
	 */
	private boolean forgotPassword(UserAccount account) {
		try {
			UserAccount dbAccount = database.getAccount(account.getEmail());

			if (dbAccount == null) {
				System.out.println("There is no user with the e-mail " + "'" + account.getEmail()
						+ "' registered in the database!");
				return false;
			}

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
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
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
			EmailUtil.sendEmail(session, config.serverEmail, account.getEmail(), "PasswordManager: Reset password",
					"A password reset was requested for this user account. Press the following link to reset your password:\r\n\r\n"
							+ finalUrl.toString());

			System.out.println("Recipient e-mail: " + account.getEmail());

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * This method returns the UserAccount with the email specified
	 * 
	 * @param email
	 * @return the UserAccount with the email
	 * @throws SQLException
	 */
	private UserAccount getAccount(String email) throws SQLException {
		return database.getAccount(email);
	}
	
	/**
	 * Sends a query to search the database for all credentials for user account
	 * 
	 * @param account is the account whose credentials is retrieved
	 * @return list of credentials stored on server database.
	 */
	private Credential[] getCredentials(UserAccount account) throws SQLException {
		List<Credential> credentials = database.listAllCredentials(account);
		Credential[] returnValue = credentials.toArray(new Credential[credentials.size()]);

		return returnValue;
	}
	
	/**
	 * Sends a query to update a UserAccount from the database
	 * 
	 * @param account that shall be updated
	 * @return returns the success of the operation
	 */
	private boolean updateAccount(UserAccount account) {
		try {
			database.changeAccountPassword(account, account.getPassword());
			return true;
		} catch (SQLException eAcc) {
			return false;
		}
	}
	
	/**
	 * Sends a query to update the database on the specified credential
	 * 
	 * @param credential is the credential that shall be updated
	 * @return returns the success of the operation
	 */
	private boolean updateCredential(Credential credential) {
		try {
			database.changeCredential(credential);
			return true;
		} catch (SQLException eCred) {
			eCred.printStackTrace();
			return false;
		}
	}

	@Override
	public void run() {
		listen();
	}
}
