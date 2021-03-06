package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import passwordmanager.model.LoginDialogModel;

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
