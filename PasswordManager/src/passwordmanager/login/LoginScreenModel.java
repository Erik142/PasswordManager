package passwordmanager.login;

import passwordmanager.Observable;
import passwordmanager.Observer;
import passwordmanager.PasswordClient;

public class LoginScreenModel implements Observable<LoginScreenModel> {
	private final PasswordClient client;
	
	public LoginScreenModel(PasswordClient client) {
		this.client = client;
	}

	@Override
	public void addObserver(Observer<LoginScreenModel> observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeObserver(Observer<LoginScreenModel> observer) {
		// TODO Auto-generated method stub
		
	}
}
