package passwordmanager.model;

import passwordmanager.communication.PasswordClient;
import passwordmanager.exception.ModelException;
import passwordmanager.util.EmailUtil;
import passwordmanager.util.StringExtensions;

/**
 * Used to login and logout a UserAccount into/from the application
 */
public class LoginDialogModel extends AbstractObservable<LoginDialogModel> {

	private String email = "";
	private String password = "";

	private boolean isLoggedIn = false;
	
	private final PasswordClient client;
	
	/**
	 * Creates a new instance of the LoginDialogModel class with the specified PasswordClient
	 * @param client The PasswordClient
	 */
	public LoginDialogModel(PasswordClient client) {
		super();
		this.client = client;
	}
	
	/**
	 * Get the e-mail address
	 * @return The e-mail address
	 */
	public String getEmail() {
		return this.email;
	}
	
	/**
	 * Get the current login status
	 * @return true if the UserAccount is logged in, false otherwise
	 */
	public boolean getLoggedInStatus() {
		return isLoggedIn;
	}
	
	/**
	 * Get the password
	 * @return The password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Log in a UserAccount with the specified e-mail address and password into the application
	 * @param email The e-mail address
	 * @param password The password
	 * @throws ModelException
	 */
	public void login(String email, String password) throws ModelException {
		boolean isValidEmail = EmailUtil.isValidEmail(email);
		boolean isPasswordEmpty = StringExtensions.isNullOrEmpty(password);
		
		if (isValidEmail && !isPasswordEmpty) {
			UserAccount account = client.getUserAccount(email);
			
			if (account != null && account.getPassword().equals(password)) {
				isLoggedIn = true;
			}
			else {
				isLoggedIn = false;
				throw new ModelException("Invalid username or password");
			}
		}
		else if (isPasswordEmpty) {
			isLoggedIn = false;
			throw new ModelException("The password field cannot be emtpy!");
		}
		else {
			isLoggedIn = false;
			throw new ModelException("You entered an invalid e-mail address.");
		}
		
		this.email = email;
		this.password = password;
		
		updateObservers(this);
	}
	
	/**
	 * Logout the user from the application
	 */
	public void logout() {
		this.isLoggedIn = false;
		this.email = "";
		this.password = "";
		
		this.updateObservers(this);
	}
	
}
