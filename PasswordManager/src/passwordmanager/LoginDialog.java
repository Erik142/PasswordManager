package passwordmanager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class LoginDialog extends JDialog {
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTextField tfUsername;
    public JPasswordField pfPassword;
    private JLabel lbUsername;
    private JLabel lbPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    public boolean succeeded;
    private LoginDialogController controller;
    
    public LoginDialog(Frame parent) {
    	super(parent, "Login", true);
    	controller = new LoginDialogController(this);
    	
    	showLoginDialog();
    }
    
    private void showLoginDialog() {
        
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
 
        lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);
 
        pfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        btnLogin = new JButton("Login");
 
        btnLogin.addActionListener(controller.afterLoginTry());
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(controller.cancelB());
 
        JPanel bp = new JPanel();
        bp.add(btnLogin);
        bp.add(btnCancel);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
 
    public String getUsername() {
        return tfUsername.getText().trim();
    }
 
    public String getPassword() {
        return new String(pfPassword.getPassword());
    }
 
    public boolean isSucceeded() {
        return succeeded;
    }
}
