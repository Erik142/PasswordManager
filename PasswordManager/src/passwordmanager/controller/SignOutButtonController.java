package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.model.LoginDialogModel;
import passwordmanager.model.LoginScreenModel;
import passwordmanager.model.MainModel;

public class SignOutButtonController implements ActionListener {
	MainModel mainModel;
	LoginScreenModel loginScreenModel;
	LoginDialogModel loginDialogModel;
	
	public SignOutButtonController(MainModel mainModel, LoginScreenModel loginScreenModel, LoginDialogModel loginDialogModel) {
		this.mainModel = mainModel;
		this.loginScreenModel = loginScreenModel;
		this.loginDialogModel = loginDialogModel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int reply = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to sign out?" ,
                "Sign Out",
                JOptionPane.YES_NO_OPTION);
		
    	if (reply == JOptionPane.YES_OPTION) {
			mainModel.setViewVisibility(false);
			loginScreenModel.setViewVisibility(true);
			loginDialogModel.logout();
    	}
	}
}
