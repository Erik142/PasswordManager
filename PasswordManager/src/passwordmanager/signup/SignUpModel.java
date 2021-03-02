package passwordmanager.signup;

import java.util.Collection;
import java.util.HashSet;

import passwordmanager.Observable;
import passwordmanager.Observer;
import passwordmanager.PasswordClient;
import passwordmanager.UserAccount;
import passwordmanager.util.EmailUtil;
import passwordmanager.util.StringExtensions;

public class SignUpModel implements Observable<SignUpModel> {

	private final int minimumPasswordLength = 8;
	
	private String email = "";
	private String password = "";
	private String confirmPassword = "";
	private String dialogMessage = "";
	
	private boolean status = false;
	private boolean isViewVisible = false;
	
	private Collection<Observer<SignUpModel>> observers;
	
	private final PasswordClient client;
	
	public SignUpModel(PasswordClient client) {
		this.client = client;
		observers = new HashSet<Observer<SignUpModel>>();
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public String getDialogMessage() {
		String dialogMessage = this.dialogMessage;
		
		// Reset dialog message so that it doesn't trigger every time
		this.dialogMessage = "";
		
		return dialogMessage;
	}
	
	public boolean getIsViewVisible() {
		return isViewVisible;
	}
	
	public void signup(String email, String password, String confirmPassword) {
		this.password = password;
		this.confirmPassword = confirmPassword;
		
		boolean isValidEmail = EmailUtil.isValidEmail(email);
		boolean arePasswordsValid = password.trim().equals(confirmPassword.trim()) && password.length() >= minimumPasswordLength;
		
		System.out.println("Is email valid: " + isValidEmail);
		
		if (arePasswordsValid && isValidEmail) {
			boolean success = client.addUserAccount(new UserAccount(email, password));
			
			this.dialogMessage = success ? "Success!" : "The server could not handle the request at this moment. Please try again.";
			this.isViewVisible = !success;
			this.status = success;
		}
		else if (!isValidEmail) {
			this.dialogMessage = "The entered email is not valid.";
			this.isViewVisible = true;
			this.status = false;
		}
		else {
			if (password.trim().equals(confirmPassword.trim())) {
				if (!StringExtensions.isNullOrEmpty(password)) { 
					this.dialogMessage = "Passwords are required to be at least 8 characters long.";
				}
				else {
					this.dialogMessage = "Passwords are empty!";
				}
			}
			else {
				this.dialogMessage = "Passwords do not match!";
			}
			
			this.isViewVisible = true;
			this.status = false;
		}
		
		updateObservers();
	}
	
	public void resetFields() {
		this.email = "";
		this.password = "";
		this.confirmPassword = "";
		
		updateObservers();
	}
	
	public void setIsViewVisible(boolean isVisible) {
		this.isViewVisible = isVisible;
		
		updateObservers();
	}
	
	public void setDialogMessage(String dialogMessage) {
		this.dialogMessage = dialogMessage;
		
		updateObservers();
	}
	
	private void updateObservers() {
		for (Observer<SignUpModel> observer : observers) {
			observer.update(this);
		}
	}
	
	@Override
	public void addObserver(Observer<SignUpModel> observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer<SignUpModel> observer) {
		observers.remove(observer);
	}

}
