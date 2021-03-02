package passwordmanager.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialogController implements ActionListener {
	public final String LOGIN_COMMAND = "LoginClick";
	public final String CANCEL_COMMAND = "LoginCancelClick";
	
	private LoginDialog parentView;
	private LoginDialogModel model;
	
	public LoginDialogController(LoginDialog parentView, LoginDialogModel model) 
	{
		this.parentView = parentView;
		this.model = model;
	}
	
	public void login() {
		String email = parentView.getUsername();
		String password = parentView.getPassword();
		
		model.login(email, password);
	}
	
	public void cancel() {
		model.setViewVisibility(false);
		model.logout();
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == LOGIN_COMMAND) {
			login();
		}
		else if (e.getActionCommand() == CANCEL_COMMAND) {
			cancel();
		}
	}
	
}
