package passwordmanager;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import passwordmanager.util.StringExtensions;

public class ForgotPasswordDialogController {

	private ForgotPasswordDialog parentDialog;
	private PasswordClient client;
	
	public ForgotPasswordDialogController(ForgotPasswordDialog parentDialog, PasswordClient client) {
		this.parentDialog = parentDialog;
		this.client = client;
	}
	
	public ActionListener cancelButtonClick() {
		return new ActionListener() {
			
			@Override
            public void actionPerformed(ActionEvent e) {
                parentDialog.dispose();
            }
			
        };
	}
	
	public ActionListener forgotPassword() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String dialogMessage = "";
				
				String email = parentDialog.tfEmail.getText();
				
				if (StringExtensions.isNullOrEmpty(email)) {
					dialogMessage = "E-mail field cannot be empty!";
				}
				else {
					try {
						boolean result = client.forgotPassword(email);
						
						if (result) {
							dialogMessage = "You will receive an email with a link to change your password.";
						}
						else {
							dialogMessage = "The account does not exist! Please sign up to create an account.";
						}
					} catch (Exception ex) {
						dialogMessage = "Error: The server could not handle the request!";
					}
				}
				
				JOptionPane.showMessageDialog(parentDialog,
			            dialogMessage,
			            "Forgot password",
			            JOptionPane.INFORMATION_MESSAGE);
			}
			
		};
	}
}
