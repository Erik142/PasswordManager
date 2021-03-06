package passwordmanager.model;

import java.util.Collection;
import java.util.HashSet;

import passwordmanager.communication.PasswordClient;
import passwordmanager.exception.ModelException;
import passwordmanager.util.StringExtensions;

/**
 * @author Erik Wahlberger
 * Used to change the password for a UserAccount object
 */
public class ChangeUserAccountModel implements Observable<ChangeUserAccountModel> {

	private final int MIN_PASSWORD_LENGTH = 8;
	
	private final Collection<Observer<ChangeUserAccountModel>> observers;
	private final PasswordClient client;
	
	private UserAccount account;
	
	private String oldPassword = "";
	private String confirmPassword = "";
	private String newPassword = "";
	
	/**
	 * Creates a new instance of the ChangeUserAccountModel with the specified PasswordClient object
	 * @param client The PasswordClient
	 */
	public ChangeUserAccountModel(PasswordClient client) {
		this.observers = new HashSet<Observer<ChangeUserAccountModel>>();
		this.client = client;
	}
	
	/**
	 * Changes the password for the UserAccount
	 * @param oldPassword The old password String
	 * @param newPassword The new password String
	 * @param confirmPassword The new password String, confirmed
	 * @throws ModelException
	 */
	public void changeUserPassword(String oldPassword, String newPassword, String confirmPassword) throws ModelException {
		if (isValidPassword(newPassword) && isValidPassword(confirmPassword) && newPassword.equals(confirmPassword) && !newPassword.equals(oldPassword)) {
			if (oldPassword.equals(account.getPassword())) {
				UserAccount updatedAccount = new UserAccount(account.getEmail(), newPassword); 
				boolean success = client.modifyUserAccount(updatedAccount);
				
				if (success) {
					account = updatedAccount;
				} else {
					throw new ModelException("The user password could not be updated. Try again later.");
				}
			} else {
				throw new ModelException("The old password is not correct!");
			}
		}
		else if (!newPassword.equals(confirmPassword)) {
			throw new ModelException("The passwords do not match!");
		}
		else if (oldPassword.equals(newPassword)) {
			throw new ModelException("The new password cannot be the same as the old password!");
		}
		else if (!isValidPassword(newPassword) || !isValidPassword(confirmPassword)) {
			throw new ModelException("The password must be at least 8 characters long!");
		}
		else {
			throw new ModelException("Password cannot be empty!");
		}
		
		updateObservers();
	}
	
	/**
	 * Deletes the UserAccount from the database
	 * @throws ModelException
	 */
	public void deleteAccount() throws ModelException {
		if (account != null) {
			boolean success = client.deleteUserAccount(account);
			
			if (!success) {
				throw new ModelException("The account could not be deleted.");	
			}
		}
		else {
			throw new ModelException("There is no account to delete.");
		}
		
		updateObservers();
	}
	
	/**
	 * Get the new password String
	 * @return The new password String
	 */
	public String getNewPassword() {
		return this.newPassword;
	}
	
	/**
	 * Get the old password String
	 * @return The old password String
	 */
	public String getOldPassword() {
		return this.oldPassword;
	}
	
	/**
	 * Get the new password String, confirmed
	 * @return The new password String, confirmed
	 */
	public String getConfirmPassword() {
		return this.confirmPassword;
	}
	
	/**
	 * Checks if the specified String is a valid password
	 * @param password The String to be verified
	 * @return true if the String is a valid password, false otherwise
	 */
	private boolean isValidPassword(String password) {
		return !StringExtensions.isNullOrEmpty(password) && password.trim().length() >= MIN_PASSWORD_LENGTH;
	}
	
	/**
	 * Set the UserAccount instance variable
	 * @param account The UserAccount object
	 */
	public void setAccount(UserAccount account) {
		this.account = account;
		
		updateObservers();
	}
	
	/**
	 * Resets the text fields
	 */
	public void reset() {
		this.oldPassword = "";
		this.newPassword = "";
		this.confirmPassword = "";
		
		updateObservers();
	}
	
	private void updateObservers() {
		for (Observer<ChangeUserAccountModel> observer : observers) {
			observer.update(this);
		}
	}
	
	@Override
	public void addObserver(Observer<ChangeUserAccountModel> observer) {
		this.observers.add(observer);
	}

	@Override
	public void removeObserver(Observer<ChangeUserAccountModel> observer) {
		this.observers.remove(observer);
	}

}
