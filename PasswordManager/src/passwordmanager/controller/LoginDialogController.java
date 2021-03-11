package passwordmanager.controller;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.exception.BadResponseException;
import passwordmanager.exception.ModelException;
import passwordmanager.model.AccountModel;
import passwordmanager.view.LoginDialog;
/**
 * The controller for the LoginDialog
 * 
 * @author Hannes Larsson
 * @version 2021-03-11
 */

public class LoginDialogController implements ActionListener {
	/**
	 * Action command used to login
	 */
	public final int LOGIN = 0;
	/**
	 * Action command used to cancel and close the window
	 */
	public final int CANCEL = 1;

	private LoginDialog parentView;
	private AccountModel model;
	
	/**
	 * Creates an instance of the controller with the parentView and model
	 * @param parentView The view used together with this controller
	 * @param model The model used together with this controller
	 */
	public LoginDialogController(LoginDialog parentView, AccountModel model) {
		this.parentView = parentView;
		this.model = model;
	}
	
	/**
	 * Retrieves the fields from the GUI and calls on model to login the UserAccount
	 *
	 */
	private void login() {
		String email = parentView.getUsername();
		String password = parentView.getPassword();

		try {
			model.login(email, password);
		} catch (ModelException | BadResponseException e) {
			JOptionPane.showMessageDialog(parentView, e.getMessage(), "Log in", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Cancels the act and disposes the parent view
	 */
	private void cancel() {
		parentView.dispose();
		model.logout();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Integer.parseInt(e.getActionCommand())) {
		case LOGIN:
			login();
			break;
		case CANCEL:
			cancel();
			break;
		default:
		}
	}

}
