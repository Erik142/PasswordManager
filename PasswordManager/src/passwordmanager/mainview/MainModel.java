package passwordmanager.mainview;

import java.util.Collection;
import java.util.HashSet;

import passwordmanager.Credential;
import passwordmanager.Observable;
import passwordmanager.Observer;
import passwordmanager.PasswordClient;
import passwordmanager.UserAccount;
import passwordmanager.addcredential.AddCredentialModel;
import passwordmanager.changecredential.ManipulateCredentialModel;
import passwordmanager.login.LoginDialogModel;

public class MainModel implements Observable<MainModel>, Observer {

	private final Collection<Observer<MainModel>> observers;
	
	private final PasswordClient client;
	private Object[][] tableData;
	private Credential[] credentials;
	
	private boolean isViewVisible = false;
	private boolean isDialogError = false;
	
	private String dialogMessage = "";
	private String dialogTitle = "";
	
	private UserAccount account;
	
	public MainModel(PasswordClient client) {
		this.observers = new HashSet<Observer<MainModel>>();
		this.client = client;
		this.tableData = new Object[0][0];
		this.credentials = new Credential[0];
	}
	
	public void deleteAccount() {
		this.isDialogError = true;
		
		if (account != null) {
			boolean success = client.deleteUserAccount(account);
			
			dialogMessage = success ? "Successfully deleted account!" : "The account could not be deleted";
			this.isDialogError = false;
		}
		else {
			dialogMessage = "There is no account to delete.";
		}
		
		dialogTitle = "Delete account";
		
		updateObservers();
	}
	
	public Credential[] getCredentials() {
		return this.credentials;
	}
	
	public String getDialogTitle() {
		String dialogTitle = this.dialogTitle;
		
		this.dialogTitle = "";
		
		return dialogTitle;
	}
	
	public String getDialogMessage() {
		String dialogMessage = this.dialogMessage;
		
		this.dialogMessage = "";
		
		return dialogMessage;
	}
	
	public boolean getDialogErrorStatus() {
		return isDialogError;
	}
	
	public Object[][] getTableData() {
		return this.tableData;
	}
	
	public UserAccount getUserAccount() {
		return this.account;
	}
	
	public boolean getViewVisibility() {
		return this.isViewVisible;
	}
	
	public void setUserAccount(UserAccount account) {
		this.account = account;
		
		updateObservers();
	}
	
	public void setViewVisibility(boolean isVisible) {
		this.isViewVisible = isVisible;
		
		updateObservers();
	}
	
	public void updateCredentials() {
		if (account != null) {
			Credential[] credentials = client.getCredentials(account);
			
			if (credentials != null && credentials.length > 0) {
				Object[][] tableData = new Object[credentials.length][3];
				
				for (int i = 0; i < credentials.length; i++) {
					Credential cred = credentials[i];
					System.out.println("Setting credential: " + cred.getURL());
					
					Object[] credObj = new Object[3];
					credObj[0] = cred.getURL();
					credObj[1] = cred.getUsername();
					credObj[2] = cred.getPassword();
					
					tableData[i] = credObj;
				}
				
				this.tableData = tableData;
			}
			else {
				this.tableData = new Object[0][0];
			}
			
			this.credentials = credentials;
		}
		
		updateObservers();
	}
	
	private void updateObservers() {
		for (Observer<MainModel> observer : observers) {
			observer.update(this);
		}
	}
	
	@Override
	public void addObserver(Observer<MainModel> observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer<MainModel> observer) {
		observers.remove(observer);
	}

	private void updateLoginDialogModel(LoginDialogModel observable) {
		if (observable.getLoggedInStatus()) {
			String email = observable.getEmail();
			String password = observable.getPassword();
			
			setUserAccount(new UserAccount(email, password));
			updateCredentials();
			setViewVisibility(true);
		}
	}
	
	private void updateAddCredentialModel(AddCredentialModel observable) {
		if (observable.getCredentialAddedStatus()) {
			updateCredentials();
		}
	}
	
	private void updateManipulateCredentialModel(ManipulateCredentialModel observable) {
		if (!observable.getChangeViewVisibilityStatus()) {
			updateCredentials();
		}
	}
	
	@Override
	public void update(Object observable) {
		if (observable instanceof LoginDialogModel) {
			updateLoginDialogModel((LoginDialogModel)observable);
		}
		
		if (observable instanceof AddCredentialModel) {
			updateAddCredentialModel((AddCredentialModel)observable);
		}
		
		if (observable instanceof ManipulateCredentialModel) {
			updateManipulateCredentialModel((ManipulateCredentialModel)observable);
		}
	}
	
}
