package passwordmanager.forgotpassword;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ForgotPasswordDialogController implements ActionListener {

	public final String SEND_MAIL_COMMAND = "PerformPasswordReset";
	public final String CANCEL_COMMAND = "CancelPasswordReset";
	
	private ForgotPasswordDialog view;
	private ForgotPasswordModel model;
	
	public ForgotPasswordDialogController(ForgotPasswordDialog view, ForgotPasswordModel model) {
		this.view = view;
		this.model = model;
	}
	
	private void cancel() {
		model.setEmail("");
		view.setVisible(false);
	}
	
	public void setViewVisibility(boolean isVisible) {
		model.setIsViewVisible(isVisible);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand() == SEND_MAIL_COMMAND) {
			model.sendEmail(view.getEmail());
		}
		else if (e.getActionCommand() == CANCEL_COMMAND) {
			cancel();
		}
	}
}
