package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import passwordmanager.communication.PasswordClient;
import passwordmanager.config.Configuration;
import passwordmanager.exception.ModelException;
import passwordmanager.model.SignUpModel;
import passwordmanager.view.SignUpDialog;

public class SignUpController implements ActionListener {
	public final String SIGNUP_COMMAND = "SignupClick";
	public final String CANCEL_COMMAND = "SignupCancelClick";

	private SignUpDialog parentView;

	private SignUpModel model;

	public SignUpController(SignUpDialog parentView, SignUpModel model) {
		this.model = model;
		this.parentView = parentView;
	}

	public void signup() {
		try {
			model.signup(parentView.getEmail(), parentView.getPassword(), parentView.getConfirmPassword());
			JOptionPane.showMessageDialog(parentView, "Success!", "Sign up", JOptionPane.INFORMATION_MESSAGE);
			parentView.dispose();
			model.removeObserver(parentView);
		} catch (ModelException e) {
			JOptionPane.showMessageDialog(parentView, e.getMessage(), "Sign up", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void cancel() {
		parentView.dispose();
		model.removeObserver(parentView);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == SIGNUP_COMMAND) {
			signup();
		} else if (e.getActionCommand() == CANCEL_COMMAND) {
			cancel();
		}
	}

}
