package passwordmanager.model;

import passwordmanager.communication.PasswordClient;
import passwordmanager.exception.ModelException;
import passwordmanager.util.StringExtensions;

/**
 * Model used to validate data and manipulate Credential objects
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public class CredentialModel extends AbstractObservable<CredentialModel> {
	private final PasswordClient client;

	private Object[][] tableData;
	private Credential[] credentials;
	private UserAccount account = null;
	private Credential selectedCredential = null;

	public CredentialModel(PasswordClient client) {
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
	public void addCredential(String url, String username, String password) throws ModelException {
		if (StringExtensions.isNullOrEmpty(url) || StringExtensions.isNullOrEmpty(username)
				|| StringExtensions.isNullOrEmpty(password)) {
			throw new ModelException("Please fill in all the fields");
		} else {
			Credential credential = new Credential(this.account.getEmail(), url, username, password);
			boolean success = client.storeCredential(credential);

			updateObservers(this);

			if (!success) {
				throw new ModelException("An error occured while adding the credential. Try again.");
			}

		}
	}

	/**
	 * Deletes the Credential in this instance from the database
	 * 
	 * @throws ModelException
	 */
	public void deleteCredential() throws ModelException {
		if (selectedCredential != null) {
			boolean success = client.deleteCredential(selectedCredential);

			if (success) {
				selectedCredential = null;
			} else {
				throw new ModelException("Could not delete the credential from the database!");
			}
		} else {
			throw new ModelException("No credential has been chosen!");
		}

		updateObservers(this);
	}

	/**
	 * Get the Credentials for the UserAccount object in this instance
	 * 
	 * @return The Credentials
	 */
	public Credential[] getCredentials() {
		return this.credentials;
	}

	/**
	 * Get the password for the selected Credential object
	 * 
	 * @return The password
	 */
	public String getPassword() {
		return selectedCredential != null ? selectedCredential.getPassword() : "";
	}

	/**
	 * Get the service url for the selected Credential object
	 * 
	 * @return The service url
	 */
	public String getService() {
		return selectedCredential != null ? selectedCredential.getURL() : "";
	}

	/**
	 * Get the logged in user
	 * 
	 * @return the UserAccount
	 */
	public UserAccount getUserAccount() {
		return this.account;
	}

	/**
	 * Get the user name for the selected Credential object
	 * 
	 * @return The user name
	 */
	public String getUserName() {
		return selectedCredential != null ? selectedCredential.getUsername() : "";
	}

	/**
	 * Get the Credentials for the UserAccount object in this instance, as a
	 * two-dimensional Object array
	 * 
	 * @return The Credentials
	 */
	public Object[][] getTableData() {
		return this.tableData;
	}

	/**
	 * Refreshes the Credentials from the database
	 */
	public void refreshCredentials() {
		if (account != null) {
			Credential[] credentials = client.getCredentials(account);

			if (credentials != null && credentials.length > 0) {
				Object[][] tableData = new Object[credentials.length][3];

				for (int i = 0; i < credentials.length; i++) {
					Credential cred = credentials[i];

					Object[] credObj = new Object[3];
					credObj[0] = cred.getURL();
					credObj[1] = cred.getUsername();
					credObj[2] = cred.getPassword();

					tableData[i] = credObj;
				}

				this.tableData = tableData;
			} else {
				this.tableData = new Object[0][0];
			}

			this.credentials = credentials;
		}

		updateObservers(this);
	}

	/**
	 * Sets the selected Credential
	 * 
	 * @param credential The selected Credential
	 */
	public void setSelectedCredential(Credential credential) {
		this.selectedCredential = credential;
	}

	/**
	 * Sets the UserAccount object for this instance
	 * 
	 * @param account The UserAccount
	 */
	public void setUserAccount(UserAccount account) {
		this.account = account;

		updateObservers(this);
	}

	/**
	 * Updates the Credential in this instance with the specified url, username and
	 * password strings
	 * 
	 * @param url      The new service url String
	 * @param username The new username
	 * @param password The new password
	 * @throws ModelException
	 */
	public void updateCredential(String url, String username, String password) throws ModelException {
		if (StringExtensions.isNullOrEmpty(url) || StringExtensions.isNullOrEmpty(username)
				|| StringExtensions.isNullOrEmpty(password)) {
			throw new ModelException("Error: One or more fields are empty!");
		}

		if (this.selectedCredential != null) {
			Credential updatedCredential = new Credential(selectedCredential.getId(), selectedCredential.getUser(), url,
					username, password);

			boolean success = client.modifyCredential(updatedCredential);

			if (!success) {
				throw new ModelException("An error occured when updating the credential. Try again.");
			}
		} else {
			throw new ModelException("No credential has been chosen!");
		}

		updateObservers(this);
	}

}
