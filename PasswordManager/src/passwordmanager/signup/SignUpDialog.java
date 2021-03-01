package passwordmanager.signup;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class SignUpDialog extends JDialog{
    private JTextField tfUsername;
    private JPasswordField pfPassword1;
    private JPasswordField pfPassword2;
    private JLabel lbUsername;
    private JLabel lbPassword1;
    private JLabel lbPassword2;
    private JButton btnSignUp;
    private JButton btnCancel;

    private SignUpController controller;
    
    public SignUpDialog(Frame parent) {
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbUsername = new JLabel("Username: ");
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
        panel.add(getPfPassword1(), cs);
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
        panel.add(getPfPassword2(), cs);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        btnSignUp = new JButton("Sign Up");
 
        btnSignUp.addActionListener(controller.signUpButton());
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(controller.cancelButton());
        JPanel bp = new JPanel();
        bp.add(btnSignUp);
        bp.add(btnCancel);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
    

	public JPasswordField getPfPassword1() {
		return pfPassword1;
	}

	public JPasswordField getPfPassword2() {
		return pfPassword2;
	}
    

}
