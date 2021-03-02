package passwordmanager.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import passwordmanager.PasswordClient;
import passwordmanager.forgotpassword.ForgotPasswordDialog;
import passwordmanager.forgotpassword.ForgotPasswordDialogController;
import passwordmanager.forgotpassword.ForgotPasswordModel;
import passwordmanager.forgotpassword.ForgotPasswordWindowController;
import passwordmanager.mainview.MainView;
import passwordmanager.signup.SignUpDialog;

public class LoginScreenController implements ActionListener {
	private LoginScreen parentView;
	private PasswordClient client;
	
	ForgotPasswordDialogController forgotPasswordController;
	ForgotPasswordWindowController forgotPasswordWindowController;
	ForgotPasswordDialog forgotPasswordDialog;
	ForgotPasswordModel forgotPasswordModel;
	
	public LoginScreenController(LoginScreen parentView, LoginScreenModel model) throws IOException {
		this.parentView = parentView;
		
		forgotPasswordModel = new ForgotPasswordModel(client);
		forgotPasswordDialog = new ForgotPasswordDialog(parentView.frame, forgotPasswordModel);
		forgotPasswordController = new ForgotPasswordDialogController(forgotPasswordDialog, forgotPasswordModel);
		forgotPasswordWindowController = new ForgotPasswordWindowController(forgotPasswordDialog, forgotPasswordModel);
		
		forgotPasswordDialog.registerListener(forgotPasswordController, forgotPasswordWindowController);
		forgotPasswordModel.addObserver(forgotPasswordDialog);
	}
	
	public ActionListener performLogin() {
		return new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                LoginDialog loginDlg = new LoginDialog(parentView.frame);
                loginDlg.setVisible(true);
                // if Login is successful then send the user to the main page where they can see their stuff
                if(loginDlg.isSucceeded()) {
                	parentView.frame.dispose();
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
                SignUpDialog signUpDlg = new SignUpDialog(parentView.frame);
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
