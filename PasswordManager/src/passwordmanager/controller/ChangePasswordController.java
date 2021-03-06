package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import passwordmanager.model.ChangeUserAccountModel;
import passwordmanager.view.ChangePasswordDialog;

public class ChangePasswordController implements ActionListener {

	public final String CHANGE_COMMAND = "ChangePasswordCommand";
	public final String CANCEL_COMMAND = "CancelChangePasswordCommand";
	
	private ChangePasswordDialog view;
	private ChangeUserAccountModel model;
	
	public ChangePasswordController(ChangePasswordDialog view, ChangeUserAccountModel model) {
		this.view = view;
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == CHANGE_COMMAND) {
			String oldPassword = view.getPasswordOld();
			String newPassword = view.getPasswordNew();
			String confirmPassword = view.getPasswordConfirm();
			
			model.changeUserPassword(oldPassword, newPassword, confirmPassword);
		} else if (e.getActionCommand() == CANCEL_COMMAND) {
			model.setIsViewVisible(false);
			model.reset();
		}
	}

}
