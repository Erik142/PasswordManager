package passwordmanager.model;

import java.util.Collection;
import java.util.HashSet;

import passwordmanager.communication.PasswordClient;

public class MainModel implements Observable<MainModel> {

	private final Collection<Observer<MainModel>> observers;
	
	private final PasswordClient client;
	private Object[][] tableData;
	private Credential[] credentials;
	
	private UserAccount account;
	
	public MainModel(PasswordClient client) {
		this.observers = new HashSet<Observer<MainModel>>();
		this.client = client;
		this.tableData = new Object[0][0];
		this.credentials = new Credential[0];
	}
	
	public Credential[] getCredentials() {
		return this.credentials;
	}
	
	public Object[][] getTableData() {
		return this.tableData;
	}
	
	public UserAccount getUserAccount() {
		return this.account;
	}
	
	public void setUserAccount(UserAccount account) {
		this.account = account;
		
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

}