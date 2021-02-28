package passwordmanager;

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
    private boolean succeeded;
    
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
 
        btnSignUp.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
            	if (comparePassword() && checkEmpty()) {
            		
                	JOptionPane.showMessageDialog(SignUpDialog.this,
                            "Success",
                            "Sign Up",
                            JOptionPane.INFORMATION_MESSAGE);
                	succeeded = true;
                	dispose();
            		
                }
                else {
                	
                    JOptionPane.showMessageDialog(SignUpDialog.this,
                            "Passwords don't match or password field is empty",
                            "Sign Up",
                            JOptionPane.ERROR_MESSAGE);
                    // reset username and password
                    pfPassword1.setText("");
                    pfPassword2.setText("");
                    succeeded = false;
                	
                	
                }
                
            }
        });
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel bp = new JPanel();
        bp.add(btnSignUp);
        bp.add(btnCancel);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
    
    public boolean isSucceeded() {
        return succeeded;
    }
    
    public boolean comparePassword() {
    	if(new String(pfPassword1.getPassword()).trim().equals(new String(pfPassword2.getPassword()).trim()) && new String(pfPassword1.getPassword()).trim() != "") {
    		return true;
    		
    		
    	}else {	return false;}
    	
    }
    
    public boolean checkEmpty() {
    	if(new String(pfPassword1.getPassword()).length()==0){
    		return false;
    	}
    	
		return true;
    	
    }
    

}
