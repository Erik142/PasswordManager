package passwordmanager.model;

import java.util.Collection;
import java.util.HashSet;

import passwordmanager.communication.PasswordClient;
import passwordmanager.util.EmailUtil;
import passwordmanager.util.StringExtensions;

public class LoginDialogModel implements Observable<LoginDialogModel> {

	private final Collection<Observer<LoginDialogModel>> observers;
	
	private String email = "";
	private String password = "";
	private String dialogMessage = "";
	
	private boolean isLoggedIn = false;
	private boolean isViewVisible = false;
	
	private final PasswordClient client;
	
	public LoginDialogModel(PasswordClient client) {
		this.observers = new HashSet<Observer<LoginDialogModel>>();
		this.client = client;
	}
	
	public String getDialogMessage() {
		String dialogMessage = this.dialogMessage;
		
		this.dialogMessage = "";
		
		return dialogMessage;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public boolean getLoggedInStatus() {
		return isLoggedIn;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean getViewVisibility() {
		return isViewVisible;
	}
	
	public void login(String email, String password) {
		boolean isValidEmail = EmailUtil.isValidEmail(email);
		boolean isPasswordEmpty = StringExtensions.isNullOrEmpty(password);
		
		if (isValidEmail && !isPasswordEmpty) {
			UserAccount account = client.getUserAccount(email);
			
			if (account != null && account.getPassword().equals(password)) {
				isLoggedIn = true;
			}
			else {
				isLoggedIn = false;
				dialogMessage = "Invalid username or password";
			}
		}
		else if (isPasswordEmpty) {
			isLoggedIn = false;
			dialogMessage = "The password field cannot be emtpy!";
		}
		else {
			isLoggedIn = false;
			dialogMessage = "You entered an invalid e-mail address.";
		}
		
		this.email = email;
		this.password = password;
		this.isViewVisible = !isLoggedIn;
		
		updateObservers();
	}
	
	public void logout() {
		this.isLoggedIn = false;
		this.email = "";
		this.password = "";
		
		this.updateObservers();
	}
	
	public void setViewVisibility(boolean visible) {
		this.isViewVisible = visible;
		
		updateObservers();
	}
	
	private void updateObservers() {
		for (Observer<LoginDialogModel> observer: observers) {
			observer.update(this);
		}
	}
	
	@Override
	public void addObserver(Observer<LoginDialogModel> observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer<LoginDialogModel> observer) {
		observers.add(observer);
	}

}
