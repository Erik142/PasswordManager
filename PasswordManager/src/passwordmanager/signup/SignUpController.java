package passwordmanager.signup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import passwordmanager.PasswordClient;
import passwordmanager.config.Configuration;

public class SignUpController implements ActionListener {
	public final String SIGNUP_COMMAND = "SignupClick";
	public final String CANCEL_COMMAND = "SignupCancelClick";
	
	private SignUpDialog parentView;
	
	private SignUpModel model;

	public SignUpController(SignUpDialog parentView, SignUpModel model) throws IOException {
		this.model = model;
		this.parentView = parentView;
	}
	
	public void signUpButton() {
		model.signup(parentView.getEmail(), parentView.getPassword(), parentView.getConfirmPassword());
	}
	
	public void cancelButton() {
		model.setIsViewVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == SIGNUP_COMMAND) {
			signUpButton();
		}
		else if (e.getActionCommand() == CANCEL_COMMAND) {
			cancelButton();
		}
		
	}
    

}
