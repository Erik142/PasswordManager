package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.exception.ModelException;
import passwordmanager.model.SignUpModel;
import passwordmanager.view.SignUpDialog;

/**
 * The controller for the SignUpDialog
 * 
 * @author Arian Alikashani
 * @version 2021-03-07
 *
 */
public class SignUpController implements ActionListener {
	public final String SIGNUP_COMMAND = "SignupClick";
	public final String CANCEL_COMMAND = "SignupCancelClick";

	private SignUpDialog parentView;

	private SignUpModel model;
	
	/**
	 * Creates an instance of the controller with the parentView and model
	 * @param parentView
	 * @param model
	 */
	public SignUpController(SignUpDialog parentView, SignUpModel model) {
		this.model = model;
		this.parentView = parentView;
	}

	/**
	 * Retrieves the fields from the GUI and calls on model to sign up the UserAccount to the database
	 *
	 */
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

	/**
	 * Cancels the act and disposes the parentview
	 */
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
