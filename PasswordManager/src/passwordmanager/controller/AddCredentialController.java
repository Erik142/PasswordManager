package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.exception.BadResponseException;
import passwordmanager.exception.ModelException;
import passwordmanager.model.CredentialModel;
import passwordmanager.view.AddCredentialDialog;

/**
 * The controller for the AddCredentialDialog
 * 
 * @author Erik Wahlberger
 * @version 2021-03-11
 */
public class AddCredentialController implements ActionListener {

	/**
	 * Action command for adding a credential
	 */
	public final int ADD_CREDENTIAL = 0;
	/**
	 * Action command for the cancel button
	 */
	public final int CANCEL = 1;

	private AddCredentialDialog view;
	private CredentialModel credentialModel;
	
	/**
	 * Creates an instance of the controller with the parentView and Model along with the mainModel
	 * @param view The view that this controller will be used with
	 * @param credentialModel The model that will be used
	 */
	public AddCredentialController(AddCredentialDialog view, CredentialModel credentialModel) {
		this.view = view;
		this.credentialModel = credentialModel;
	}
	
	/**
	 * Retrieves the fields from the GUI and calls on model to add the credential to the database
	 */
	private void addCredential() {
		String username = view.getEmail();
		String url = view.getWebsite();
		String password = view.getPassword();

		try {
			credentialModel.addCredential(url, username, password);
			view.dispose();
		} catch (ModelException | BadResponseException e1) {
			JOptionPane.showMessageDialog(view, e1.getMessage(), "Add credential", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Cancels the act and disposes the AddCredentialDialog view
	 */
	private void cancel() {
		view.dispose();
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
