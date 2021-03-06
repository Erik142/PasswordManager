package passwordmanager.view;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import passwordmanager.controller.InitialViewActionListener;
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
	
	public void registerListener(InitialViewActionListener authenticationActionListener) {
        btnLogin.setActionCommand("" + authenticationActionListener.LOG_IN);
		btnLogin.addActionListener(authenticationActionListener);
        btnForgot.setActionCommand("" + authenticationActionListener.FORGOT_PASSWORD);
        btnForgot.addActionListener(authenticationActionListener);
		btnSignUp.setActionCommand("" + authenticationActionListener.SIGN_UP);
        btnSignUp.addActionListener(authenticationActionListener); 
	}

}
