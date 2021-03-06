package passwordmanager.model;

import java.util.Collection;
import java.util.HashSet;

import passwordmanager.communication.PasswordClient;
import passwordmanager.util.StringExtensions;

public class ChangeUserAccountModel implements Observable<ChangeUserAccountModel>, Observer<LoginDialogModel> {

	private final int MIN_PASSWORD_LENGTH = 8;
	
	private final Collection<Observer<ChangeUserAccountModel>> observers;
	private final PasswordClient client;
	
	private UserAccount account;
	
	private String dialogMessage = "";
	private boolean isDialogError = false;
	
	private boolean isViewVisible = false;
	
	private String oldPassword = "";
	private String confirmPassword = "";
	private String newPassword = "";
	
	public ChangeUserAccountModel(PasswordClient client) {
		this.observers = new HashSet<Observer<ChangeUserAccountModel>>();
		this.client = client;
	}
	
	public void changeUserPassword(String oldPassword, String newPassword, String confirmPassword) {
		isViewVisible = true;
		
		if (isValidPassword(newPassword) && isValidPassword(confirmPassword) && newPassword.equals(confirmPassword) && !newPassword.equals(oldPassword)) {
			if (oldPassword.equals(account.getPassword())) {
				UserAccount updatedAccount = new UserAccount(account.getEmail(), newPassword); 
				boolean success = client.modifyUserAccount(updatedAccount);
				
				if (success) {
					account = updatedAccount;
					dialogMessage = "Successfully changed the user password!";
					isDialogError = false;
					isViewVisible = false;
				} else {
					dialogMessage = "The user password could not be updated. Try again later.";
					isDialogError = true;
				}
			} else {
				this.dialogMessage = "The old password is not correct!";
				isDialogError = true;
			}
		}
		else if (!newPassword.equals(confirmPassword)) {
			this.dialogMessage = "The passwords do not match!";
			this.isDialogError = true;
		}
		else if (oldPassword.equals(newPassword)) {
			this.dialogMessage = "The new password cannot be the same as the old password!";
			this.isDialogError = true;
		}
		else if (!isValidPassword(newPassword) || !isValidPassword(confirmPassword)) {
			this.dialogMessage = "The password must be at least 8 characters long!";
			this.isDialogError = true;
		}
		else {
			dialogMessage = "Password cannot be empty!";
			isDialogError = true;
		}
		
		updateObservers();
	}
	
	public String getDialogMessage() {
		String dialogMessage = this.dialogMessage;
		
		this.dialogMessage = "";
		
		return dialogMessage;
	}
	
	public boolean getIsDialogError() {
		return this.isDialogError;
	}
	
	public boolean getIsViewVisible() {
		return this.isViewVisible;
	}
	
	public String getNewPassword() {
		return this.newPassword;
	}
	
	public String getOldPassword() {
		return this.oldPassword;
	}
	
	public String getConfirmPassword() {
		return this.confirmPassword;
	}
	
	private boolean isValidPassword(String password) {
		return !StringExtensions.isNullOrEmpty(password) && password.trim().length() >= MIN_PASSWORD_LENGTH;
	}
	
	public void setAccount(UserAccount account) {
		this.account = account;
		
		updateObservers();
	}
	
	public void setIsViewVisible(boolean isVisible) {
		this.isViewVisible = isVisible;
		
		updateObservers();
	}
	
	public void reset() {
		this.oldPassword = "";
		this.newPassword = "";
		this.confirmPassword = "";
		this.dialogMessage = "";
		this.isDialogError = false;
		
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

	@Override
	public void update(LoginDialogModel observable) {
		String email = observable.getEmail();
		String password = observable.getPassword();
		
		setAccount(new UserAccount(email, password));
	}

}
