package passwordmanager.changecredential;

import java.util.Collection;
import java.util.HashSet;

import passwordmanager.Credential;
import passwordmanager.Observable;
import passwordmanager.Observer;
import passwordmanager.PasswordClient;
import passwordmanager.util.StringExtensions;

public class ManipulateCredentialModel implements Observable<ManipulateCredentialModel> {

	private final Collection<Observer<ManipulateCredentialModel>> observers;
	private final PasswordClient client;
	
	private boolean isChangeViewVisible = false;
	private boolean isManipulateCredentialSuccessful = false;
	
	private String dialogMessage = "";
	private boolean isDialogError = false;
	
	private Credential credential;
	
	public ManipulateCredentialModel(PasswordClient client) {
		this.observers = new HashSet<Observer<ManipulateCredentialModel>>();
		this.client = client;
	}
	
	public void deleteCredential() {
		if (credential != null) {
			boolean success = client.deleteCredential(credential);
			
			this.isManipulateCredentialSuccessful = success;
			
			if (success) {
				dialogMessage = "";
				isDialogError = false;
				
				credential = null;
			}
			else {
				isDialogError = true;
				dialogMessage = "Could not delete the credential from the database!";
			}
		}
		else {
			dialogMessage = "No credential has been chosen!";
			isDialogError = true;
		}
		
		updateObservers();
	}
	
	public Credential getCredential() {
		return this.credential;
	}
	
	public boolean getChangeViewVisibilityStatus() {
		return this.isChangeViewVisible;
	}
	
	public String getDialogMessage() {
		String dialogMessage = this.dialogMessage;
		
		this.dialogMessage = "";
		
		return dialogMessage;
	}
	
	public boolean getIsDialogError() {
		return this.isDialogError;
	}
	
	public boolean getManipulateCredentialSucceeded() {
		return this.isManipulateCredentialSuccessful;
	}
	
	public String getPassword() {
		return this.credential != null ? this.credential.getPassword() : "";
	}
	
	public String getUrl() {
		return this.credential != null ? this.credential.getURL() : "";
	}
	
	public String getUserName() {
		return this.credential != null ? this.credential.getUsername() : "";
	}
	
	public void reset() {
		this.isManipulateCredentialSuccessful = false;
	}
	
	public void setChangeViewVisibilityStatus(boolean isVisible) {
		this.isChangeViewVisible = isVisible;
		
		updateObservers();
	}
	
	public void setCredential(Credential credential) {
		this.credential = credential;
		
		updateObservers();
	}
	
	public void updateCredential(String url, String username, String password) {
		if (StringExtensions.isNullOrEmpty(url) || StringExtensions.isNullOrEmpty(username) || StringExtensions.isNullOrEmpty(password)) {
			this.isDialogError = true;
			this.dialogMessage = "Error: One or more fields are empty!";
		}
		
		if (this.credential != null) {
			this.credential.setURL(url);
			this.credential.setUsername(username);
			this.credential.setPassword(password);
			
			boolean success = client.modifyCredential(credential);
			
			if (success) {
				this.dialogMessage = "";
				this.isDialogError = false;
				this.isChangeViewVisible = false;
			}
			else {
				this.dialogMessage = "An error occured when updating the credential. Try again.";
				this.isDialogError = true;
			}
		}
		else {
			this.isDialogError = true;
			this.dialogMessage = "No credential has been chosen!";
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
