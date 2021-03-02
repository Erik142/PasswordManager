package passwordmanager.forgotpassword;

import java.util.ArrayList;
import java.util.List;

import passwordmanager.Observable;
import passwordmanager.Observer;
import passwordmanager.PasswordClient;
import passwordmanager.util.EmailUtil;
import passwordmanager.util.StringExtensions;

public class ForgotPasswordModel implements Observable<ForgotPasswordModel> {
	private String email = "";
	private String dialogMessage = "";
	private boolean isViewVisible = false;
	
	private List<Observer<ForgotPasswordModel>> observers;
	
	private final PasswordClient client;
	
	public ForgotPasswordModel(PasswordClient client) {
		observers = new ArrayList<Observer<ForgotPasswordModel>>();
		
		this.client = client;
	}
	
	public String getDialogMessage() {
		String dialogMessage = this.dialogMessage;
		
		// Reset dialog message so that it doesn't trigger every time
		this.dialogMessage = "";
		
		return dialogMessage;
	}
	
	public String getEmail() {
		return email;
	}
	
	public boolean getIsViewVisible() {
		return isViewVisible;
	}
	
	public void sendEmail(String email) {
		String dialogMessage = "";
		
		if (StringExtensions.isNullOrEmpty(email)) {
			dialogMessage = "E-mail field cannot be empty!";
		}
		else if (!EmailUtil.isValidEmail(email)) {
			dialogMessage = "The value is not a valid e-mail address.";
		}
		else {
			try {
				boolean result = client.forgotPassword(email);
				
				if (result) {
					dialogMessage = "You will receive an email with a link to change your password.";
				}
				else {
					dialogMessage = "The account does not exist! Please sign up to create an account.";
				}
			} catch (Exception ex) {
				dialogMessage = "Error: The server could not handle the request!";
			}
		}
		
		if (!StringExtensions.isNullOrEmpty(dialogMessage)) {
			setEmail(email);
		}
		
		setDialogMessage(dialogMessage);
	}
	
	public void setDialogMessage(String dialogMessage) {
		this.dialogMessage = dialogMessage;
		updateObservers();
	}
	
	public void setEmail(String email) {
		this.email = email;
		updateObservers();
	}
	
	public void setIsViewVisible(boolean isViewVisible) {
		this.isViewVisible = isViewVisible;
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
