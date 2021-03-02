package passwordmanager.addcredential;

import java.util.Collection;
import java.util.HashSet;

import passwordmanager.Credential;
import passwordmanager.Observable;
import passwordmanager.Observer;
import passwordmanager.PasswordClient;
import passwordmanager.UserAccount;
import passwordmanager.util.StringExtensions;

public class AddCredentialModel implements Observable<AddCredentialModel> {
	private final Collection<Observer<AddCredentialModel>> observers;
	private final PasswordClient client;
	
	private String dialogMessage = "";
	
	private boolean isDialogError = false;
	private boolean isViewVisible = false;
	private boolean isCredentialAdded = false;
	
	private String url = "";
	private String username = "";
	private String password = "";
	
	public AddCredentialModel(PasswordClient client) {
		this.observers = new HashSet<Observer<AddCredentialModel>>();
		this.client = client;
	}
	
	public void addCredential(UserAccount account, String url, String username, String password) {
		if (StringExtensions.isNullOrEmpty(url) || StringExtensions.isNullOrEmpty(username) || StringExtensions.isNullOrEmpty(password)) {
			isDialogError = true;
			dialogMessage = "Please fill in all the fields";
		}
		else {
			Credential credential = new Credential(account.getEmail(), url, username, password);
			boolean success = client.storeCredential(credential);
			
			isDialogError = !success;
			dialogMessage = !isDialogError ? "Successfully added credential!" : "An error occured while adding the credential. Try again.";
			isCredentialAdded = success;
			isViewVisible = !success;
			
			this.url = url;
			this.username = username;
			this.password = password;
		}
		
		updateObservers();
	}
	
	public boolean getCredentialAddedStatus() {
		return this.isCredentialAdded;
	}
	
	public boolean getDialogErrorStatus() {
		return this.isDialogError;
	}
	
	public String getDialogMessage() {
		String dialogMessage = this.dialogMessage;
		
		this.dialogMessage = "";
		
		return dialogMessage;
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
	
	public boolean getVisibilityStatus() {
		return this.isViewVisible;
	}
	
	public void reset() {
		this.dialogMessage = "";
		this.isDialogError = false;
		
		this.username = "";
		this.url = "";
		this.password = "";
		
		this.isCredentialAdded = false;
	}
	
	public void setVisibilityStatus(boolean isVisible) {
		this.isViewVisible = isVisible;
		
		updateObservers();
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
