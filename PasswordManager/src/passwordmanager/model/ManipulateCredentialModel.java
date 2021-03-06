package passwordmanager.model;

import java.util.Collection;
import java.util.HashSet;

import passwordmanager.communication.PasswordClient;
import passwordmanager.exception.ModelException;
import passwordmanager.util.StringExtensions;

/**
 * @author Erik Wahlberger
 * Manipulates Credential objects in the database
 */
public class ManipulateCredentialModel implements Observable<ManipulateCredentialModel> {

	private final Collection<Observer<ManipulateCredentialModel>> observers;
	private final PasswordClient client;
	
	private Credential credential;
	
	/**
	 * Creates a new instance of the ManipulateCredentialModel class with the specified PasswordClient
	 * @param client The PasswordClient
	 */
	public ManipulateCredentialModel(PasswordClient client) {
		this.observers = new HashSet<Observer<ManipulateCredentialModel>>();
		this.client = client;
	}
	
	/**
	 * Deletes the Credential in this instance from the database
	 * @throws ModelException
	 */
	public void deleteCredential() throws ModelException {
		if (credential != null) {
			boolean success = client.deleteCredential(credential);
			
			if (success) {
				credential = null;
			}
			else {
				throw new ModelException("Could not delete the credential from the database!");
			}
		}
		else {
			throw new ModelException("No credential has been chosen!");
		}
		
		updateObservers();
	}
	
	/**
	 * Get the Credential in this instance
	 * @return the Credential
	 */
	public Credential getCredential() {
		return this.credential;
	}
	
	/**
	 * Get the password String in the Credential
	 * @return The password String
	 */
	public String getPassword() {
		return this.credential != null ? this.credential.getPassword() : "";
	}
	
	/**
	 * Get the Service url String in the Credential
	 * @return The service url String
	 */
	public String getService() {
		return this.credential != null ? this.credential.getURL() : "";
	}
	
	/**
	 * Get the username String in the Credential
	 * @return The username String
	 */
	public String getUserName() {
		return this.credential != null ? this.credential.getUsername() : "";
	}
	
	/**
	 * Sets the Credential object for this instance
	 * @param credential The Credential object
	 */
	public void setCredential(Credential credential) {
		this.credential = credential;
		
		updateObservers();
	}
	
	/**
	 * Updates the Credential in this instance with the specified url, username and password strings
	 * @param url The new service url String
	 * @param username The new username 
	 * @param password The new password
	 * @throws ModelException
	 */
	public void updateCredential(String url, String username, String password) throws ModelException {
		if (StringExtensions.isNullOrEmpty(url) || StringExtensions.isNullOrEmpty(username) || StringExtensions.isNullOrEmpty(password)) {
			throw new ModelException("Error: One or more fields are empty!");
		}
	
		if (this.credential != null) {
			this.credential.setURL(url);
			this.credential.setUsername(username);
			this.credential.setPassword(password);
			
			boolean success = client.modifyCredential(credential);

			if (!success) {
				throw new ModelException("An error occured when updating the credential. Try again.");
			}
			else {
				this.credential = null;
			}
		}
		else {
			throw new ModelException("No credential has been chosen!");
		}
		
		updateObservers();
	}
	
	private void updateObservers() {
		for (Observer<ManipulateCredentialModel> observer : observers) {
			observer.update(this);
		}
	}
	
	@Override
	public void addObserver(Observer<ManipulateCredentialModel> observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer<ManipulateCredentialModel> observer) {
		observers.remove(observer);
	}

}
