package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.exception.ModelException;
import passwordmanager.model.CredentialModel;
import passwordmanager.view.ChangeCredentialDialog;

/**
 * The controller for the ChangeCredentialDialog
 * 
 * @author Hannes Larsson
 * @version 2021-03-07
 */
public class ChangeCredentialController implements ActionListener {

	/**
	 * Action command to change the credential
	 */
	public final int CHANGE_CREDENTIAL = 0;
	/**
	 * Action command for the cancel button
	 */
	public final int CANCEL = 1;
	
	private ChangeCredentialDialog view;
	private CredentialModel model;
	
	/**
	 * Creates an instance of the controller with the parentView and model
	 * @param view The view that this controller will be used with
	 * @param model The model that this controller will be used with
	 */
	public ChangeCredentialController(ChangeCredentialDialog view, CredentialModel model) {
		this.view = view;
		this.model = model;
	}
	
	/**
	 * Retrieves the fields from the GUI and calls on model to update the credential to the database
	 *
	 */
	private void changeCredential() {
		String service = view.getService();
		String username = view.getUserName();
		String password = view.getPassword();
			
		try {
			model.updateCredential(service, username, password);
			view.dispose();
		} catch (ModelException e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Cancels the act and disposes the ChangeCredentialDialog view
	 */
	private void cancel() {
		view.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Integer.parseInt(e.getActionCommand())) {
		case CHANGE_CREDENTIAL:
			changeCredential();
			break;
		case CANCEL:
			cancel();
			break;
		default:
		}
	}

}
