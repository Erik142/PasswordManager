package passwordmanager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class LoginScreen {
	
	public static void loginScreen() {
		
		 final JFrame frame = new JFrame("Password Manager");
	        final JButton btnLogin = new JButton("Login");
	        final JButton btnSignUp = new JButton("Sign Up");
	        final JButton btnForgot = new JButton("Forgot my password");
	        
	 
	        btnLogin.addActionListener(
	                new ActionListener(){
	                    public void actionPerformed(ActionEvent e) {
	                        LoginDialog loginDlg = new LoginDialog(frame);
	                        loginDlg.setVisible(true);
	                        // if Login is successful then send the user to the main page where they can see their stuff
	                        if(loginDlg.isSucceeded()) {
	                        	
	                        	frame.dispose();
	                        	CreateFrame.createFrame();
	                        }
	                        
	                    }
	                });
	        
	        btnSignUp.addActionListener(
	                new ActionListener(){
	                    public void actionPerformed(ActionEvent e) {
	                        SignUpDialog signUpDlg = new SignUpDialog(frame);
	                        signUpDlg.setVisible(true);
	                        // if Sign up is successful then save and send the information forward to the database
	                        if(signUpDlg.isSucceeded()){
	                            // TODO - send information to databse
	                        }
	                    }
	                });
	        
	        btnForgot.addActionListener(
	                new ActionListener(){
	                    public void actionPerformed(ActionEvent e) {
	                        ForgotDialog forgotUpDlg = new ForgotDialog(frame);
	                        forgotUpDlg.setVisible(true);
	                        // if forgot password is successful 
	                    }
	                }); 
	        
	 
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(300, 100);
	        frame.setLayout(new FlowLayout());
	        frame.getContentPane().add(btnLogin);
	        frame.getContentPane().add(btnSignUp);
	        frame.getContentPane().add(btnForgot);
	        frame.setVisible(true);

	}

}
