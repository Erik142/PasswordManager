package passwordmanager.model;

import passwordmanager.communication.PasswordClient;

/**
 * @author Erik Wahlberger
 * Stores and updates the Credential data for the table in the MainView
 */
public class MainModel extends AbstractObservable<MainModel> {

	private final PasswordClient client;
	private Object[][] tableData;
	private Credential[] credentials;
	
	private UserAccount account;
	
	/**
	 * Creates a new instance of the MainModel class with the specified PasswordClient
	 * @param client The PasswordClient
	 */
	public MainModel(PasswordClient client) {
		super();
		this.client = client;
		this.tableData = new Object[0][0];
		this.credentials = new Credential[0];
	}

	/**
	 * Get the Credentials for the UserAccount object in this instance
	 * @return The Credentials
	 */
	public Credential[] getCredentials() {
		return this.credentials;
	}
	
	/**
	 * Get the Credentials for the UserAccount object in this instance, as a two-dimensional Object array
	 * @return The Credentials
	 */
	public Object[][] getTableData() {
		return this.tableData;
	}
	
	/**
	 * Get the UserAccount object
	 * @return The UserAccount
	 */
	public UserAccount getUserAccount() {
		return this.account;
	}
	
	/**
	 * Sets the UserAccount object for this instance
	 * @param account The UserAccount
	 */
	public void setUserAccount(UserAccount account) {
		this.account = account;
		
		updateObservers(this);
	}
	
	/**
	 * Updates the Credentials from the database
	 */
	public void updateCredentials() {
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
			}
			else {
				this.tableData = new Object[0][0];
			}
			
			this.credentials = credentials;
		}
		
		updateObservers(this);
	}
	
}