package passwordmanager.controller;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.exception.ModelException;
import passwordmanager.model.LoginDialogModel;
import passwordmanager.view.LoginDialog;
/**
 * The controller for the LoginDialog
 * 
 * @author ???
 * @version 2021-03-07
 */

public class LoginDialogController implements ActionListener {
	public final int LOGIN = 0;
	public final int CANCEL = 1;

	private LoginDialog parentView;
	private LoginDialogModel model;
	
	/**
	 * Creates an instance of the controller with the parentView and model
	 * @param parentview
	 * @param model
	 */
	public LoginDialogController(LoginDialog parentView, LoginDialogModel model) {
		this.parentView = parentView;
		this.model = model;
	}
	
	/**
	 * Retrieves the fields from the GUI and calls on model to login the UserAccount
	 *
	 */
	public void login() {
		String email = parentView.getUsername();
		String password = parentView.getPassword();

		try {
			model.login(email, password);
		} catch (ModelException e) {
			JOptionPane.showMessageDialog(parentView, e.getMessage(), "Log in", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Cancels the act and disposes the parentview
	 */
	public void cancel() {
		model.removeObserver(parentView);
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
