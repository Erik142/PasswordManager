package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.exception.ModelException;
import passwordmanager.model.ForgotPasswordModel;
import passwordmanager.view.ForgotPasswordDialog;

/**
 * ActionListener for the buttons in ForgotPasswordDialog
 * 
 * @author Erik Wahlberger
 */
public class ForgotPasswordDialogController implements ActionListener {

	public final String SEND_MAIL_COMMAND = "PerformPasswordReset";
	public final String CANCEL_COMMAND = "CancelPasswordReset";

	private ForgotPasswordDialog view;
	private ForgotPasswordModel model;

	/**
	 * Creates a new instance of the ForgotPasswordDialogController class with the
	 * specified ForgotPasswordDialog and ForgotPasswordModel
	 * 
	 * @param view  The ForgotPasswordDialog
	 * @param model The ForgotPasswordModel
	 */
	public ForgotPasswordDialogController(ForgotPasswordDialog view, ForgotPasswordModel model) {
		this.view = view;
		this.model = model;
	}

	/**
	 * Cancels and closes the ForgotPasswordDialog
	 */
	private void cancel() {
		model.setEmail("");
		view.dispose();
		model.removeObserver(view);
	}

	/**
	 * Trigger a "send e-mail" in the ForgotPasswordModel
	 */
	private void sendEmail() {
		try {
			model.sendEmail(view.getEmail());
			JOptionPane.showMessageDialog(view, "You will receive an email with a link to change your password.",
					"Forgot password", JOptionPane.INFORMATION_MESSAGE);
		} catch (ModelException e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "Forgot password", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Implementation of the ActionListener interface
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == SEND_MAIL_COMMAND) {
			sendEmail();
		} else if (e.getActionCommand() == CANCEL_COMMAND) {
			cancel();
		}
	}
}
