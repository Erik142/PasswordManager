package passwordmanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import passwordmanager.config.Configuration;

public class LoginScreenController {
	private LoginScreen parentView;
	private PasswordClient client;
	
	public LoginScreenController(LoginScreen parentView, Configuration config) throws IOException {
		this.parentView = parentView;
		this.client = new PasswordClient(config);
	}
	
	public ActionListener performLogin() {
		return new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                LoginDialog loginDlg = new LoginDialog(parentView.frame);
                loginDlg.setVisible(true);
                // if Login is successful then send the user to the main page where they can see their stuff
                if(loginDlg.isSucceeded()) {
                	parentView.frame.dispose();
                	CreateFrame.createFrame();
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
                if(signUpDlg.isSucceeded()){
                    // TODO - send information to databse
                }
            }
        };
	}
	
	public ActionListener showForgotPasswordDialog() {
		
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ForgotPasswordDialog forgotUpDlg = new ForgotPasswordDialog(parentView.frame, client);
                forgotUpDlg.setVisible(true);
                // if forgot password is successful 
			}
			
		};
	}
}
