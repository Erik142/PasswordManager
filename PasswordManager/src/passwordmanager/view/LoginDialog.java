package passwordmanager.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import passwordmanager.controller.LoginDialogController;
import passwordmanager.model.LoginDialogModel;
import passwordmanager.model.Observer;

/**
 * This class creates the GUI for the LoginDialog
 * 
 * @author Arian Alikashani
 * @version 2021-03-07
 *
 */
public class LoginDialog extends JDialog implements Observer<LoginDialogModel> {
	 
   
	private static final long serialVersionUID = 1L;
	public JTextField tfUsername;
    public JPasswordField pfPassword;
    private JLabel lbUsername;
    private JLabel lbPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    public boolean succeeded;
    
    private final Frame parent;
    
    /**
     * The constructor uses a parent frame to create and display the LoginDialog
     * @param parent
     */
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
        setModal(true);
        setVisible(false);
    }
    
    /**
     * Checks whether user pressed login or cancel button and tells controller to do the appropriate action
     * 
     * @param controller
     */
    public void registerListener(LoginDialogController controller) {
    	btnLogin.setActionCommand("" + controller.LOGIN);
    	btnLogin.addActionListener(controller);
    	btnCancel.setActionCommand("" + controller.CANCEL);
    	btnCancel.addActionListener(controller);
    }
    /**
     * Returns username
     * @return String with username trimmed
     */
    public String getUsername() {
        return tfUsername.getText().trim();
    }
    
    /**
     * Returns password
     * @return String with password
     */
    public String getPassword() {
        return new String(pfPassword.getPassword());
    }
    /**
     * Returns the boolean succeeded
     * @return succeeded
     */
    public boolean isSucceeded() {
        return succeeded;
    }

	@Override
	public void update(LoginDialogModel observable) {
		this.tfUsername.setText(observable.getEmail());
		this.pfPassword.setText(observable.getPassword());
	    this.succeeded = observable.getLoggedInStatus();
	}
}
