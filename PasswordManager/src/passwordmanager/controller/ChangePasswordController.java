package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.exception.BadResponseException;
import passwordmanager.exception.ModelException;
import passwordmanager.model.AccountModel;
import passwordmanager.view.ChangeUserPasswordDialog;

/**
 * The controller for the ChangeUserPasswordDialog
 * 
 * @author Hannes Larsson
 * @version 2021-03-11
 */
public class ChangePasswordController implements ActionListener {

	/**
	 * Action command used to change the user password
	 */
	public final int CHANGE_PASSWORD = 0;
	/**
	 * Action command used to cancel the password change
	 */
	public final int CANCEL = 1;

	private ChangeUserPasswordDialog view;
	private AccountModel model;

	
	/**
	 * Creates an instance of the controller with the parentView and model
	 * @param view The view that this controller will be used with
	 * @param model The model that this controller will be used with
	 */
	public ChangePasswordController(ChangeUserPasswordDialog view, AccountModel model) {
		this.view = view;
		this.model = model;
	}
	
	/**
	 * Retrieves the fields from the GUI and calls on model to update the UserAccount to the database
	 *
	 */
	private void changeUserAccountPassword() {
		String oldPassword = view.getPasswordOld();
		String newPassword = view.getPasswordNew();
		String confirmPassword = view.getPasswordConfirm();

		try {
			model.changeUserPassword(oldPassword, newPassword, confirmPassword);
			JOptionPane.showMessageDialog(view, "Successfully changed the user password!",
					"Change user account password", JOptionPane.INFORMATION_MESSAGE);
			view.dispose();
		} catch (ModelException | BadResponseException e1) {
			JOptionPane.showMessageDialog(view, e1.getMessage(), "Change user account password",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Cancels the act and disposes the parentview
	 */
	private void cancel() {
		view.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Integer.parseInt(e.getActionCommand())) {
		case CHANGE_PASSWORD:
			changeUserAccountPassword();
			break;
		case CANCEL:
			cancel();
			break;
		default:
		}
	}

}
