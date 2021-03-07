package passwordmanager.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import passwordmanager.controller.SignUpController;

/**
 * This class creates the GUI for the MainView
 * 
 * @author Arian Alikashani
 * @version 2021-03-07
 *
 */
public class SignUpDialog extends JDialog {
	/**
	 * Text field for the username
	 */
    private JTextField tfUsername;
    /**
     * Password field for the password
     */
    private JPasswordField pfPassword1;
    /**
     * Password field for "confirm password"
     */
    private JPasswordField pfPassword2;
    /**
     * Label for the username JTextfield
     */
    private JLabel lbUsername;
    /**
     * Label for the password JPasswordField
     */
    private JLabel lbPassword1;
    /**
     * Label for the "confirm password" JPasswordField
     */
    private JLabel lbPassword2;
    /**
     * Used to sign up
     */
    private JButton btnSignUp;
    /**
     * Used to cancel and close the dialog
     */
    private JButton btnCancel;
    
    /**
     * The constructor uses a parent frame to create and display the MainView
     * @param parent The parent frame for this dialog
     */
    public SignUpDialog(Frame parent) {
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbUsername = new JLabel("E-mail: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);
 
        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfUsername, cs);
 
        lbPassword1 = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword1, cs);
 
        pfPassword1 = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPassword1, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
        
        lbPassword2 = new JLabel("Repeat Password: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lbPassword2, cs);
 
        pfPassword2 = new JPasswordField(20);
        cs.gridx = 2;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(pfPassword2, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        btnSignUp = new JButton("Sign Up");
 
        btnCancel = new JButton("Cancel");
        JPanel bp = new JPanel();
        bp.add(btnSignUp);
        bp.add(btnCancel);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
        setModal(false);
        setVisible(false);
    }
    
    /**
     * Returns the email
     * @return String with the email
     */
    public String getEmail() {
    	return tfUsername.getText();
    }
    
    /**
     * Returns the password
     * @return String with the password
     */
	public String getPassword() {
		return new String(pfPassword1.getPassword());
	}

	/**
	 * Returns the confirmationPassword
	 * @return String with the confirmationPassword
	 */
	public String getConfirmPassword() {
		return new String(pfPassword2.getPassword());
	}

	/**
     * Checks which button was pressed and tells controller to do the appropriate action
     * 
     * @param controller The action listener
     */
	public void registerListener(SignUpController controller) {
		btnSignUp.setActionCommand("" + controller.SIGNUP);
		btnSignUp.addActionListener(controller);
		btnCancel.setActionCommand("" + controller.CANCEL);
		btnCancel.addActionListener(controller);
	}

}
