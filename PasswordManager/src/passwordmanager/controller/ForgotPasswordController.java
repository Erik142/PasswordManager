package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import passwordmanager.model.ForgotPasswordModel;

public class ForgotPasswordController implements ActionListener {

	public final String FORGOT_PASSWORD_EVENT = "ForgotPasswordClick";
	
	private ForgotPasswordModel model;
	
	public ForgotPasswordController(ForgotPasswordModel model) {
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == FORGOT_PASSWORD_EVENT) {
			model.setIsViewVisible(true);
		}
	}

}
