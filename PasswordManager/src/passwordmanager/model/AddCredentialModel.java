package passwordmanager.model;

import passwordmanager.communication.PasswordClient;
import passwordmanager.exception.ModelException;
import passwordmanager.util.StringExtensions;

/**
 * The model used to add a credential to the database
 * 
 * @author Erik Wahlberger
 */
public class AddCredentialModel extends AbstractObservable<AddCredentialModel> {
	private final PasswordClient client;

	private String url = "";
	private String username = "";
	private String password = "";

	/**
	 * Creates a new instance of the AddCredentialModel class with the specified
	 * PasswordClient object
	 * 
	 * @param client The PasswordClient
	 */
	public AddCredentialModel(PasswordClient client) {
		super();
		this.client = client;
	}

	/**
	 * Add a Credential object to the database with the specified UserAccount e-mail
	 * address, url, username and password
	 * 
	 * @param account  The e-mail address for the corresponding UserAccount object
	 * @param url      The URL for the service
	 * @param username The username for the service
	 * @param password The password for the service
	 * @throws ModelException
	 */
	public void addCredential(UserAccount account, String url, String username, String password) throws ModelException {
		if (StringExtensions.isNullOrEmpty(url) || StringExtensions.isNullOrEmpty(username)
				|| StringExtensions.isNullOrEmpty(password)) {
			throw new ModelException("Please fill in all the fields");
		} else {
			Credential credential = new Credential(account.getEmail(), url, username, password);
			boolean success = client.storeCredential(credential);

			this.url = url;
			this.username = username;
			this.password = password;
			updateObservers(this);

			if (!success) {
				throw new ModelException("An error occured while adding the credential. Try again.");
			}

		}
	}

	/**
	 * Retrieves the password String
	 * 
	 * @return The password String
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Retrieves the Url String
	 * 
	 * @return The Url String
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Retrieves the username String
	 * 
	 * @return The username String
	 */
	public String getUserName() {
		return this.username;
	}

	/**
	 * Resets all text field data
	 */
	public void reset() {
		this.username = "";
		this.url = "";
		this.password = "";
	}

}
