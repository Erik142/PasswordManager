package passwordmanager.login;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import passwordmanager.Observer;
import passwordmanager.config.Configuration;


public class LoginScreen implements Observer<LoginScreenModel> {
	
	private final JButton btnLogin;
	private final JButton btnSignUp;
	private final JButton btnForgot;
	
	protected final JFrame frame;
	
	public LoginScreen() throws IOException {
		this.frame = new JFrame("Password Manager");
		
		btnLogin = new JButton("Login");
        btnSignUp = new JButton("Sign Up");
        btnForgot = new JButton("Forgot my password");
 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);
        frame.setLayout(new FlowLayout());
        frame.getContentPane().add(btnLogin);
        frame.getContentPane().add(btnSignUp);
        frame.getContentPane().add(btnForgot);
        frame.setVisible(true);
	}
	
	public Frame getFrame() {
		return this.frame;
	}
	
	public void registerListener(LoginController loginController, ForgotPasswordController forgotPasswordController, SignupController signupController) {
        btnLogin.addActionListener(loginController);
        btnForgot.setActionCommand(forgotPasswordController.FORGOT_PASSWORD_EVENT);
        btnForgot.addActionListener(forgotPasswordController);
        btnSignUp.addActionListener(signupController); 
	}

	@Override
	public void update(LoginScreenModel observable) {
		// TODO Auto-generated method stub
		
	}
}
