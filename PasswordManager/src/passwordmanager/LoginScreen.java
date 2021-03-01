package passwordmanager;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import passwordmanager.config.Configuration;


public class LoginScreen {
	
	protected final JFrame frame;
	private LoginScreenController controller;
	
	
	public LoginScreen(Configuration config) throws IOException {
		this.frame = new JFrame("Password Manager");
		controller = new LoginScreenController(this, config);
		
		showLoginScreen();
	}
	
	private void showLoginScreen() throws IOException {
	        final JButton btnLogin = new JButton("Login");
	        final JButton btnSignUp = new JButton("Sign Up");
	        final JButton btnForgot = new JButton("Forgot my password");
	 
	        btnLogin.addActionListener(controller.performLogin());
	        
	        btnSignUp.addActionListener(controller.performSignup());
	        
	        btnForgot.addActionListener(controller.showForgotPasswordDialog()); 
	        
	 
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(300, 100);
	        frame.setLayout(new FlowLayout());
	        frame.getContentPane().add(btnLogin);
	        frame.getContentPane().add(btnSignUp);
	        frame.getContentPane().add(btnForgot);
	        frame.setVisible(true);
	}
}
