package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.exception.ModelException;
import passwordmanager.model.AccountModel;
import passwordmanager.view.SignUpDialog;

/**
 * The controller for the SignUpDialog
 * 
 * @author Arian Alikashani
 * @version 2021-03-07
 *
 */
public class SignUpController implements ActionListener {
	/**
	 * Action command used to sign up
	 */
	public final int SIGNUP = 0;
	/**
	 * Action command used to cancel and close the window
	 */
	public final int CANCEL = 1;

	private SignUpDialog parentView;

	private AccountModel model;
	
	/**
	 * Creates an instance of the controller with the parentView and model
	 * @param parentView The view used together with this controller
	 * @param model The model used together with this controller
	 */
	public SignUpController(SignUpDialog parentView, AccountModel model) {
		this.model = model;
		this.parentView = parentView;
	}

	/**
	 * Retrieves the fields from the GUI and calls on model to sign up the UserAccount to the database
	 *
	 */
	private void signup() {
		try {
			model.signup(parentView.getEmail(), parentView.getPassword(), parentView.getConfirmPassword());
			JOptionPane.showMessageDialog(parentView, "Success!", "Sign up", JOptionPane.INFORMATION_MESSAGE);
			parentView.dispose();
		} catch (ModelException e) {
			JOptionPane.showMessageDialog(parentView, e.getMessage(), "Sign up", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Cancels the act and disposes the parentview
	 */
	private void cancel() {
		parentView.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Integer.parseInt(e.getActionCommand())) {
			case SIGNUP:
			signup();
			break;
			case CANCEL:
			cancel();
			break;
			default:
		}
	}

}
