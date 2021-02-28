package passwordmanager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ForgotPasswordDialog extends JDialog{
	private JLabel lbEmail;
	private JTextField tfEmail;
    private JButton btnSend;
    private JButton btnCancel;
    
    public ForgotPasswordDialog(Frame parent) {
    	JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbEmail = new JLabel("Email: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbEmail, cs);
 
        tfEmail = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfEmail, cs);
        
        btnSend = new JButton("Send new password to my email");
        btnSend.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		//TODO-Code to send the email written in the text field to database and compare it
        		
        		
        		//Then show up a message to the user that she will receive an email if her there is an account with such email.
        		JOptionPane.showMessageDialog(ForgotPasswordDialog.this,
                        "You will receive an email to change your password if there is such an account.",
                        "Forgot password",
                        JOptionPane.INFORMATION_MESSAGE);
        		dispose();
        	}
        	
        });
        
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        JPanel bp = new JPanel();
        bp.add(btnSend);
        bp.add(btnCancel);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
}
