package passwordmanager.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController implements ActionListener {
	
	private LoginDialogModel model;
	
	public LoginController(LoginDialogModel model) {
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		model.setViewVisibility(true);
	}

}
