package passwordmanager.view;

import java.awt.*;

import javax.swing.*;

import passwordmanager.controller.InitialViewActionListener;
import passwordmanager.util.FrameUtil;

/**
 * This class creates the GUI for the initial view that starts with the client
 * 
 * @author Arian Alikashani
 * @version 2021-03-11
 *
 */
public class InitialView {
	
	private final JButton btnLogin;
	private final JButton btnSignUp;
	private final JButton btnForgot;
	
	protected final JFrame frame;
	
	/**
     * The constructor creates a frame to create and display the dialog for the initial view
     */
	public InitialView() {
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
        
        btnLogin.setEnabled(false);
        btnSignUp.setEnabled(false);
        btnForgot.setEnabled(false);
	}
	
	/**
	 * Returns the frame
	 * @return frame
	 */
	public Frame getFrame() {
		return this.frame;
	}
	
	/**
     * Checks whether user pressed login, forgot or SignUp button and tells controller to do the appropriate action
     * 
     * @param actionListener The action listener
     */
	public void registerListener(InitialViewActionListener actionListener) {
        btnLogin.setActionCommand("" + actionListener.LOG_IN);
		btnLogin.addActionListener(actionListener);
        btnForgot.setActionCommand("" + actionListener.FORGOT_PASSWORD);
        btnForgot.addActionListener(actionListener);
		btnSignUp.setActionCommand("" + actionListener.SIGN_UP);
        btnSignUp.addActionListener(actionListener); 
	}
	
	/**
	 * Enables all the buttons in the dialog
	 */
	public void enableButtons() {
        btnLogin.setEnabled(true);
        btnSignUp.setEnabled(true);
        btnForgot.setEnabled(true);
	}

}
