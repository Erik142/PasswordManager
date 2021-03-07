package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.exception.ModelException;
import passwordmanager.model.AccountModel;
import passwordmanager.view.ForgotPasswordDialog;

/**
 * ActionListener for the buttons in ForgotPasswordDialog
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public class ForgotPasswordDialogController implements ActionListener {

	/**
	 * Action command used to send an email
	 */
	public final int SEND_MAIL = 0;
	/**
	 * Action command used to cancel and close the window
	 */
	public final int CANCEL = 1;

	private ForgotPasswordDialog view;
	private AccountModel model;

	/**
	 * Creates a new instance of the ForgotPasswordDialogController class with the
	 * specified ForgotPasswordDialog and ForgotPasswordModel
	 * 
	 * @param view  The ForgotPasswordDialog
	 * @param model The AccountModel
	 */
	public ForgotPasswordDialogController(ForgotPasswordDialog view, AccountModel model) {
		this.view = view;
		this.model = model;
	}

	/**
	 * Cancels and closes the ForgotPasswordDialog
	 */
	private void cancel() {
		view.dispose();
	}

	/**
	 * Trigger a "send e-mail" in the ForgotPasswordModel
	 */
	private void sendEmail() {
		try {
			model.forgotPassword(view.getEmail());
			JOptionPane.showMessageDialog(view, "You will receive an email with a link to change your password.",
					"Forgot password", JOptionPane.INFORMATION_MESSAGE);
			view.dispose();
		} catch (ModelException e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "Forgot password", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Implementation of the ActionListener interface
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Integer.parseInt(e.getActionCommand())) {
			case SEND_MAIL:
			sendEmail();
			break;
			case CANCEL:
			cancel();
			default:
		}
	}
}
