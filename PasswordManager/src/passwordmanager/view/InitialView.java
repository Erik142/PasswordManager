package passwordmanager.view;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import passwordmanager.config.Configuration;
import passwordmanager.controller.ForgotPasswordController;
import passwordmanager.controller.LoginController;
import passwordmanager.controller.SignUpDialogController;
import passwordmanager.model.LoginScreenModel;
import passwordmanager.model.Observer;
import passwordmanager.util.FrameUtil;


public class InitialView {
	
	private final JButton btnLogin;
	private final JButton btnSignUp;
	private final JButton btnForgot;
	
	protected final JFrame frame;
	
	public InitialView() throws IOException {
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
        
        FrameUtil.centerFrame(frame);
	}
	
	public Frame getFrame() {
		return this.frame;
	}
	
	public void registerListener(LoginController loginController, ForgotPasswordController forgotPasswordController, SignUpDialogController signupController) {
        btnLogin.addActionListener(loginController);
        btnForgot.setActionCommand(forgotPasswordController.FORGOT_PASSWORD_EVENT);
        btnForgot.addActionListener(forgotPasswordController);
        btnSignUp.addActionListener(signupController); 
	}

}
