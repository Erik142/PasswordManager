package passwordmanager.model;

import passwordmanager.communication.PasswordClient;
import passwordmanager.exception.ModelException;
import passwordmanager.util.EmailUtil;
import passwordmanager.util.StringExtensions;

/**
 * Model used to validate data and manipulate UserAccount objects
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public class AccountModel extends AbstractObservable<AccountModel> {
	private final int MIN_PASSWORD_LENGTH = 8;
	private final PasswordClient client;

	private boolean isLoggedIn = false;
	private UserAccount account = null;

	/**
	 * Create a new instance of the AccountModel class with the specified PasswordClient object
	 * @param client The PasswordClient
	 */
	public AccountModel(PasswordClient client) {
		super();

		this.client = client;
	}

	/**
	 * Changes the password for the UserAccount
	 * 
	 * @param oldPassword     The old password String
	 * @param newPassword     The new password String
	 * @param confirmPassword The new password String, confirmed
	 * @throws ModelException Throws on data validation or server errors
	 */
	public void changeUserPassword(String oldPassword, String newPassword, String confirmPassword)
			throws ModelException {
		if (isValidPassword(newPassword) && isValidPassword(confirmPassword) && newPassword.equals(confirmPassword)
				&& !newPassword.equals(oldPassword)) {
			if (oldPassword.equals(account.getPassword())) {
				UserAccount updatedAccount = new UserAccount(account.getEmail(), newPassword);
				boolean success = client.modifyUserAccount(updatedAccount);

				if (!success) {
					throw new ModelException("The user password could not be updated. Try again later.");
				}
			} else {
				throw new ModelException("The old password is not correct!");
			}
		} else if (!newPassword.equals(confirmPassword)) {
			throw new ModelException("The passwords do not match!");
		} else if (oldPassword.equals(newPassword)) {
			throw new ModelException("The new password cannot be the same as the old password!");
		} else if (!isValidPassword(newPassword) || !isValidPassword(confirmPassword)) {
			throw new ModelException("The password must be at least 8 characters long!");
		} else {
			throw new ModelException("Password cannot be empty!");
		}

		updateObservers(this);
	}

	/**
	 * Deletes the UserAccount from the database
	 * 
	 * @throws ModelException Throws on data validation or server errors
	 */
	public void deleteAccount() throws ModelException {
		if (account != null) {
			boolean success = client.deleteUserAccount(account);

			if (!success) {
				throw new ModelException("The account could not be deleted.");
			}
		} else {
			throw new ModelException("There is no account to delete.");
		}

		updateObservers(this);
	}

	/**
	 * Send an e-mail to the specified e-mail address
	 * 
	 * @param email The e-mail address
	 * @throws ModelException Throws on data validation or server errors
	 */
	public void forgotPassword(String email) throws ModelException {
		if (StringExtensions.isNullOrEmpty(email)) {
			throw new ModelException("E-mail field cannot be empty!");
		} else if (!EmailUtil.isValidEmail(email)) {
			throw new ModelException("The value is not a valid e-mail address.");
		} else {
			try {
				boolean result = client.forgotPassword(email);

				if (!result) {
					throw new ModelException("The account does not exist! Please sign up to create an account.");
				}
			} catch (Exception ex) {
				throw new ModelException("Error: The server could not handle the request!");
			}
		}
	}

	/**
	 * Get the current login status
	 * 
	 * @return true if the UserAccount is logged in, false otherwise
	 */
	public boolean getLoggedInStatus() {
		return isLoggedIn;
	}

	/**
	 * Get the logged in user
	 * 
	 * @return the UserAccount
	 */
	public UserAccount getUserAccount() {
		return account;
	}

	/**
	 * Checks if the specified String is a valid password
	 * 
	 * @param password The String to be verified
	 * @return true if the String is a valid password, false otherwise
	 */
	private boolean isValidPassword(String password) {
		return !StringExtensions.isNullOrEmpty(password) && password.trim().length() >= MIN_PASSWORD_LENGTH;
	}

	/**
	 * Log in a UserAccount with the specified e-mail address and password into the
	 * application
	 * 
	 * @param email    The e-mail address
	 * @param password The password
	 * @throws ModelException Throws on data validation or server errors
	 */
	public void login(String email, String password) throws ModelException {
		boolean isValidEmail = EmailUtil.isValidEmail(email);
		boolean isPasswordEmpty = StringExtensions.isNullOrEmpty(password);

		if (isValidEmail && !isPasswordEmpty) {
			UserAccount account = client.getUserAccount(email);

			if (account != null && account.getPassword().equals(password)) {
				isLoggedIn = true;
				this.account = account;
			} else {
				isLoggedIn = false;
				throw new ModelException("Invalid username or password");
			}
		} else if (isPasswordEmpty) {
			isLoggedIn = false;
			throw new ModelException("The password field cannot be emtpy!");
		} else {
			isLoggedIn = false;
			throw new ModelException("You entered an invalid e-mail address.");
		}

		updateObservers(this);
	}

	/**
	 * Logout the user from the application
	 */
	public void logout() {
		this.isLoggedIn = false;

		this.updateObservers(this);
	}

	/**
	 * Add a new user to the database with the specified e-mail address, password
	 * and confirmed password Strings
	 * 
	 * @param email           The e-mail address
	 * @param password        The password String
	 * @param confirmPassword The password String, confirmed
	 * @throws ModelException Throws on data validation or server errors
	 */
	public void signup(String email, String password, String confirmPassword) throws ModelException {
		boolean isValidEmail = EmailUtil.isValidEmail(email);
		boolean arePasswordsValid = password.trim().equals(confirmPassword.trim())
				&& password.length() >= MIN_PASSWORD_LENGTH;

		if (arePasswordsValid && isValidEmail) {
			boolean success = client.addUserAccount(new UserAccount(email, password));

			if (!success) {
				throw new ModelException("The user is already registered.");
			}
		} else if (!isValidEmail) {
			throw new ModelException("The entered email is not valid.");
		} else {
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
}
