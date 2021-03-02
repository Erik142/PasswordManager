package passwordmanager.mainview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.login.LoginDialogModel;
import passwordmanager.login.LoginScreenModel;

public class DeleteAccountButtonController implements ActionListener {
	private MainModel mainModel;
	private LoginDialogModel loginDialogModel;
	private LoginScreenModel loginScreenModel;
	
	public DeleteAccountButtonController(MainModel mainModel, LoginDialogModel loginDialogModel, LoginScreenModel loginScreenModel) {
		this.mainModel = mainModel;
		this.loginDialogModel = loginDialogModel;
		this.loginScreenModel = loginScreenModel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int reply = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete your account?" ,
                "Delete Account",
                JOptionPane.YES_NO_OPTION);
		
    	if (reply == JOptionPane.YES_OPTION) {
			mainModel.deleteAccount();
			mainModel.setViewVisibility(false);
			loginScreenModel.setViewVisibility(true);
			loginDialogModel.logout();
    	}
	}
}
