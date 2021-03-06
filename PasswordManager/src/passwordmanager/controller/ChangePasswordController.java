package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.exception.ModelException;
import passwordmanager.model.ChangeUserAccountModel;
import passwordmanager.view.ChangeUserPasswordDialog;

public class ChangePasswordController implements ActionListener {

	public final int CHANGE_PASSWORD = 0;
	public final int CANCEL = 1;

	private ChangeUserPasswordDialog view;
	private ChangeUserAccountModel model;

	public ChangePasswordController(ChangeUserPasswordDialog view, ChangeUserAccountModel model) {
		this.view = view;
		this.model = model;
	}

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
