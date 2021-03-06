package passwordmanager.model;

import java.util.Collection;
import java.util.HashSet;

import passwordmanager.communication.PasswordClient;
import passwordmanager.exception.ModelException;
import passwordmanager.util.EmailUtil;
import passwordmanager.util.StringExtensions;

public class SignUpModel implements Observable<SignUpModel> {

	private final int minimumPasswordLength = 8;
	
	private String email = "";
	private String password = "";
	private String confirmPassword = "";
	
	private boolean status = false;
	
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
	
	public void signup(String email, String password, String confirmPassword) throws ModelException {
		this.password = password;
		this.confirmPassword = confirmPassword;
		
		boolean isValidEmail = EmailUtil.isValidEmail(email);
		boolean arePasswordsValid = password.trim().equals(confirmPassword.trim()) && password.length() >= minimumPasswordLength;
		
		if (arePasswordsValid && isValidEmail) {
			boolean success = client.addUserAccount(new UserAccount(email, password));
			
			if (!success) {
				throw new ModelException("The server could not handle the request at this moment. Please try again.");
			}

			this.status = success;
		}
		else if (!isValidEmail) {
			this.status = false;
			throw new ModelException("The entered email is not valid.");
		}
		else {
			this.status = false;
			
			if (password.trim().equals(confirmPassword.trim())) {
				if (!StringExtensions.isNullOrEmpty(password)) { 
					throw new ModelException("Passwords are required to be at least 8 characters long.");
				}
				else {
					throw new ModelException("Passwords are empty!");
				}
			}
			else {
				throw new ModelException("Passwords do not match!");
			}
		}
		
		updateObservers();
	}
	
	public void resetFields() {
		this.email = "";
		this.password = "";
		this.confirmPassword = "";
		
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
