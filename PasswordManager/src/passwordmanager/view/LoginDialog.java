package passwordmanager.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import passwordmanager.controller.LoginDialogController;
import passwordmanager.model.LoginDialogModel;
import passwordmanager.model.Observer;
import passwordmanager.util.StringExtensions;

public class LoginDialog extends JDialog implements Observer<LoginDialogModel> {
	 
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
    
    private final Frame parent;
    
    public LoginDialog(Frame parent) {
    	super(parent, "Login", true);
    	
    	this.parent = parent;
    	
    	showLoginDialog();
    }
    
    private void showLoginDialog() {
        
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
        btnCancel = new JButton("Cancel");
 
        JPanel bp = new JPanel();
        bp.add(btnLogin);
        bp.add(btnCancel);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
    
    public void registerListener(LoginDialogController controller) {
    	btnLogin.setActionCommand(controller.LOGIN_COMMAND);
    	btnLogin.addActionListener(controller);
    	btnCancel.setActionCommand(controller.CANCEL_COMMAND);
    	btnCancel.addActionListener(controller);
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

	@Override
	public void update(LoginDialogModel observable) {
		this.tfUsername.setText(observable.getEmail());
		this.pfPassword.setText(observable.getPassword());
		
		String dialogMessage = observable.getDialogMessage();
		
		if (!StringExtensions.isNullOrEmpty(dialogMessage)) {
			int dialogType = observable.getLoggedInStatus() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
			
			JOptionPane.showMessageDialog(this,
                    dialogMessage,
                    "Login",
                    dialogType);
		}
		
		this.setModal(observable.getViewVisibility());
		this.setVisible(observable.getViewVisibility());
		this.succeeded = observable.getLoggedInStatus();
	}
}
