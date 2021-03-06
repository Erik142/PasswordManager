package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import passwordmanager.model.AddCredentialModel;
import passwordmanager.model.MainModel;
import passwordmanager.model.UserAccount;
import passwordmanager.view.AddDialog;

public class AddCredentialController implements ActionListener {

	public final String ADD_COMMAND = "AddCredentialClick";
	public final String CANCEL_COMMAND = "CancelCredentialClick";
	
	private AddDialog view;
	private AddCredentialModel addCredentialModel;
	private MainModel mainModel;
	
	public AddCredentialController(AddDialog view, AddCredentialModel addCredentialModel, MainModel mainModel) {
		this.view = view;
		this.addCredentialModel = addCredentialModel;
		this.mainModel = mainModel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == ADD_COMMAND) {
			String username = view.getEmail();
			String url = view.getWebsite();
			String password = view.getPassword();
			UserAccount account = mainModel.getUserAccount();
			
			addCredentialModel.addCredential(account, url, username, password);
		}
		
		if (e.getActionCommand() == CANCEL_COMMAND) {
			addCredentialModel.reset();
			addCredentialModel.setVisibilityStatus(false);
		}
	}

}
