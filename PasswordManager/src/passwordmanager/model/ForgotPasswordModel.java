package passwordmanager.model;

import java.util.ArrayList;
import java.util.List;

import passwordmanager.communication.PasswordClient;
import passwordmanager.exception.ModelException;
import passwordmanager.util.EmailUtil;
import passwordmanager.util.StringExtensions;

public class ForgotPasswordModel implements Observable<ForgotPasswordModel> {
	private String email = "";
	
	private List<Observer<ForgotPasswordModel>> observers;
	
	private final PasswordClient client;
	
	public ForgotPasswordModel(PasswordClient client) {
		observers = new ArrayList<Observer<ForgotPasswordModel>>();
		
		this.client = client;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void sendEmail(String email) throws ModelException {
		if (StringExtensions.isNullOrEmpty(email)) {
			throw new ModelException("E-mail field cannot be empty!");
		}
		else if (!EmailUtil.isValidEmail(email)) {
			throw new ModelException("The value is not a valid e-mail address.");
		}
		else {
			try {
				boolean result = client.forgotPassword(email);
				
				if (result) {
					setEmail("");
				}
				else {
					throw new ModelException("The account does not exist! Please sign up to create an account.");
				}
			} catch (Exception ex) {
				setEmail(email);
				throw new ModelException("Error: The server could not handle the request!");
			}
		}
	}
	
	public void setEmail(String email) {
		this.email = email;
		updateObservers();
	}
	
	private void updateObservers() {
		for (Observer<ForgotPasswordModel> observer: observers) {
			observer.update(this);
		}
	}

	@Override
	public void addObserver(Observer<ForgotPasswordModel> observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer<ForgotPasswordModel> observer) {
		observers.remove(observer);
	}
}
