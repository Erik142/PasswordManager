package passwordmanager.model;

import java.util.Collection;
import java.util.HashSet;

import passwordmanager.communication.PasswordClient;
import passwordmanager.exception.ModelException;
import passwordmanager.util.StringExtensions;

public class ManipulateCredentialModel implements Observable<ManipulateCredentialModel> {

	private final Collection<Observer<ManipulateCredentialModel>> observers;
	private final PasswordClient client;
	
	private Credential credential;
	
	public ManipulateCredentialModel(PasswordClient client) {
		this.observers = new HashSet<Observer<ManipulateCredentialModel>>();
		this.client = client;
	}
	
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
	
	public Credential getCredential() {
		return this.credential;
	}
	
	public String getPassword() {
		return this.credential != null ? this.credential.getPassword() : "";
	}
	
	public String getService() {
		return this.credential != null ? this.credential.getURL() : "";
	}
	
	public String getUserName() {
		return this.credential != null ? this.credential.getUsername() : "";
	}
	
	public void setCredential(Credential credential) {
		this.credential = credential;
		
		updateObservers();
	}
	
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
