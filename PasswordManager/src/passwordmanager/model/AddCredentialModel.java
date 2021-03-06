package passwordmanager.model;

import java.util.Collection;
import java.util.HashSet;

import passwordmanager.communication.PasswordClient;
import passwordmanager.exception.ModelException;
import passwordmanager.util.StringExtensions;

public class AddCredentialModel implements Observable<AddCredentialModel> {
	private final Collection<Observer<AddCredentialModel>> observers;
	private final PasswordClient client;
	
	private String url = "";
	private String username = "";
	private String password = "";
	
	public AddCredentialModel(PasswordClient client) {
		this.observers = new HashSet<Observer<AddCredentialModel>>();
		this.client = client;
	}
	
	public void addCredential(UserAccount account, String url, String username, String password) throws ModelException {
		if (StringExtensions.isNullOrEmpty(url) || StringExtensions.isNullOrEmpty(username) || StringExtensions.isNullOrEmpty(password)) {
			throw new ModelException("Please fill in all the fields");
		}
		else {
			Credential credential = new Credential(account.getEmail(), url, username, password);
			boolean success = client.storeCredential(credential);
			
			this.url = url;
			this.username = username;
			this.password = password;
			updateObservers();

			if (!success) {
				throw new ModelException("An error occured while adding the credential. Try again.");
			}
			
		}
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getUserName() {
		return this.username;
	}
	
	public void reset() {
		this.username = "";
		this.url = "";
		this.password = "";
	}
	
	private void updateObservers() {
		for (Observer<AddCredentialModel> observer : observers) {
			observer.update(this);
		}
	}
	
	@Override
	public void addObserver(Observer<AddCredentialModel> observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer<AddCredentialModel> observer) {
		observers.remove(observer);
	}

}
