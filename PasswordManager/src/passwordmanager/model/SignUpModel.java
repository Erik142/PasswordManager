package passwordmanager.model;

import passwordmanager.communication.PasswordClient;
import passwordmanager.exception.ModelException;
import passwordmanager.util.EmailUtil;
import passwordmanager.util.StringExtensions;

/**
 * Used to sign up a user to the database
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public class SignUpModel extends AbstractObservable<SignUpModel> {

	private final int minimumPasswordLength = 8;

	private String email = "";
	private String password = "";
	private String confirmPassword = "";

	private boolean status = false;

	private final PasswordClient client;

	/**
	 * Creates a new instance of the SignUpModel class with the specified
	 * PasswordClient
	 * 
	 * @param client
	 */
	public SignUpModel(PasswordClient client) {
		super();
		this.client = client;
	}

	/**
	 * Get the sign up status
	 * 
	 * @return true if the sign up was successful, false otherwise
	 */
	public boolean getStatus() {
		return status;
	}

	/**
	 * Get the e-mail address String
	 * 
	 * @return The e-mail address
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Get the password String
	 * 
	 * @return The password String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Get the password String, confirmed
	 * 
	 * @return The password String, confirmed
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * Add a new user to the database with the specified e-mail address, password
	 * and confirmed password Strings
	 * 
	 * @param email           The e-mail address
	 * @param password        The password String
	 * @param confirmPassword The password String, confirmed
	 * @throws ModelException
	 */
	public void signup(String email, String password, String confirmPassword) throws ModelException {
		this.password = password;
		this.confirmPassword = confirmPassword;

		boolean isValidEmail = EmailUtil.isValidEmail(email);
		boolean arePasswordsValid = password.trim().equals(confirmPassword.trim())
				&& password.length() >= minimumPasswordLength;

		if (arePasswordsValid && isValidEmail) {
			boolean success = client.addUserAccount(new UserAccount(email, password));

			if (!success) {
				throw new ModelException("The server could not handle the request at this moment. Please try again.");
			}

			this.status = success;
		} else if (!isValidEmail) {
			this.status = false;
			throw new ModelException("The entered email is not valid.");
		} else {
			this.status = false;

			if (password.trim().equals(confirmPassword.trim())) {
				if (!StringExtensions.isNullOrEmpty(password)) {
					throw new ModelException("Passwords are required to be at least 8 characters long.");
				} else {
					throw new ModelException("Passwords are empty!");
				}
			} else {
				throw new ModelException("Passwords do not match!");
			}
		}

		updateObservers(this);
	}

	/**
	 * Reset all text fields
	 */
	public void resetFields() {
		this.email = "";
		this.password = "";
		this.confirmPassword = "";

		updateObservers(this);
	}

}
