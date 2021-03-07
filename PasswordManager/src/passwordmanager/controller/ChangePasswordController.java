package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.exception.ModelException;
import passwordmanager.model.ChangeUserAccountModel;
import passwordmanager.view.ChangeUserPasswordDialog;

/**
 * The controller for the ChangeUserPasswordDialog
 * 
 * @author ???
 * @version 2021-03-07
 */
public class ChangePasswordController implements ActionListener {

	public final int CHANGE_PASSWORD = 0;
	public final int CANCEL = 1;

	private ChangeUserPasswordDialog view;
	private ChangeUserAccountModel model;

	
	/**
	 * Creates an instance of the controller with the parentView and model
	 * @param view
	 * @param model
	 */
	public ChangePasswordController(ChangeUserPasswordDialog view, ChangeUserAccountModel model) {
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
		} catch (ModelException e1) {
			JOptionPane.showMessageDialog(view, e1.getMessage(), "Change user account password",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Cancels the act and disposes the parentview
	 */
	private void cancel() {
		view.dispose();
		model.removeObserver(view);
		model.reset();
		model.removeObserver(view);
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
