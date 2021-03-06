package passwordmanager.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import passwordmanager.communication.PasswordClient;
import passwordmanager.model.ForgotPasswordModel;
import passwordmanager.model.LoginScreenModel;
import passwordmanager.view.ForgotPasswordDialog;
import passwordmanager.view.LoginDialog;
import passwordmanager.view.InitialView;
import passwordmanager.view.MainView;
import passwordmanager.view.SignUpDialog;

public class LoginScreenController implements ActionListener {
	private InitialView parentView;
	private PasswordClient client;
	
	ForgotPasswordDialogController forgotPasswordController;
	ForgotPasswordWindowController forgotPasswordWindowController;
	ForgotPasswordDialog forgotPasswordDialog;
	ForgotPasswordModel forgotPasswordModel;
	
	public LoginScreenController(InitialView parentView) throws IOException {
		this.parentView = parentView;
		
		forgotPasswordModel = new ForgotPasswordModel(client);
		forgotPasswordDialog = new ForgotPasswordDialog(parentView.getFrame(), forgotPasswordModel);
		forgotPasswordController = new ForgotPasswordDialogController(forgotPasswordDialog, forgotPasswordModel);
		forgotPasswordWindowController = new ForgotPasswordWindowController(forgotPasswordDialog, forgotPasswordModel);
		
		forgotPasswordDialog.registerListener(forgotPasswordController);
		forgotPasswordModel.addObserver(forgotPasswordDialog);
	}
	
	public ActionListener performLogin() {
		return new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                LoginDialog loginDlg = new LoginDialog(parentView.getFrame());
                loginDlg.setVisible(true);
                // if Login is successful then send the user to the main page where they can see their stuff
                if(loginDlg.isSucceeded()) {
                	parentView.getFrame().dispose();
                	try {
						new MainView().createFrame();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            }
        };
	}
	
	public ActionListener performSignup() {
        return new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SignUpDialog signUpDlg = new SignUpDialog(parentView.getFrame());
                signUpDlg.setVisible(true);
                // if Sign up is successful then save and send the information forward to the database
            }
        };
	}
	
	public ActionListener showForgotPasswordDialog() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				forgotPasswordController.setViewVisibility(true);
			}
			
		};
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
