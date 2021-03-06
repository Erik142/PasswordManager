package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.exception.ModelException;
import passwordmanager.model.AddCredentialModel;
import passwordmanager.model.MainModel;
import passwordmanager.model.UserAccount;
import passwordmanager.view.AddCredentialDialog;

public class AddCredentialController implements ActionListener {

	public final int ADD_CREDENTIAL = 0;
	public final int CANCEL = 1;

	private AddCredentialDialog view;
	private AddCredentialModel addCredentialModel;
	private MainModel mainModel;

	public AddCredentialController(AddCredentialDialog view, AddCredentialModel addCredentialModel, MainModel mainModel) {
		this.view = view;
		this.addCredentialModel = addCredentialModel;
		this.mainModel = mainModel;
	}

	private void addCredential() {
		String username = view.getEmail();
		String url = view.getWebsite();
		String password = view.getPassword();
		UserAccount account = mainModel.getUserAccount();

		try {
			addCredentialModel.addCredential(account, url, username, password);
			addCredentialModel.reset();
			view.dispose();
			addCredentialModel.removeObserver(view);
		} catch (ModelException e1) {
			JOptionPane.showMessageDialog(view, e1.getMessage(), "Add credential", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cancel() {
		addCredentialModel.reset();
		view.dispose();
		addCredentialModel.removeObserver(view);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Integer.parseInt(e.getActionCommand())) {
		case ADD_CREDENTIAL:
			addCredential();			
			break;
		case CANCEL:
			cancel();
			break;
		default:
		}
	}

}
