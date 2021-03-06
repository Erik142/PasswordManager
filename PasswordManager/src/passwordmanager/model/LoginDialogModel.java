package passwordmanager.model;

import java.util.Collection;
import java.util.HashSet;

import passwordmanager.communication.PasswordClient;
import passwordmanager.exception.ModelException;
import passwordmanager.util.EmailUtil;
import passwordmanager.util.StringExtensions;

public class LoginDialogModel implements Observable<LoginDialogModel> {

	private final Collection<Observer<LoginDialogModel>> observers;
	
	private String email = "";
	private String password = "";

	private boolean isLoggedIn = false;
	
	private final PasswordClient client;
	
	public LoginDialogModel(PasswordClient client) {
		this.observers = new HashSet<Observer<LoginDialogModel>>();
		this.client = client;
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
		
		updateObservers();
	}
	
	public void logout() {
		this.isLoggedIn = false;
		this.email = "";
		this.password = "";
		
		this.updateObservers();
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
