package passwordmanager.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import passwordmanager.controller.ChangeCredentialController;
import passwordmanager.controller.UpdateTableWindowListener;
import passwordmanager.model.CredentialModel;

/**
 * This class creates the GUI for changing a credential
 * 
 * @author Arian Alikashani
 * @version 2021-03-07
 *
 */
public class ChangeCredentialDialog extends JDialog {
    
	private static final long serialVersionUID = 1L;
	/**
	 * Label for the website/URL JTextField
	 */
	private JLabel lbService;
	/**
	 * Label for the username JTextField
	 */
    private JLabel lbUserName;
    /**
     * Label for the password JPasswordField
     */
    private JLabel lbPassword;
    /**
     * Text field for the website/URL
     */
	private JTextField tfService;
	/**
	 * Text field for the username
	 */
    private JTextField tfUserName;
    /**
     * Password field for the password
     */
    private JPasswordField pfPassword;
    /**
     * Used to update the credential with the new values
     */
    private JButton changeButton;
    /**
     * Used to cancel and close the window
     */
    private JButton cancelButton;
    
    
    /**
     * The constructor uses a parent frame to create and display the dialog for adding a credential
     * @param parent The parent frame on which this dialog will be create on top off
     * @param model The model that will used to retrieve data for the GUI
     */
    public ChangeCredentialDialog(Frame parent, CredentialModel model) {
    	showChangeDialog(model, parent);
    }
        
    private void showChangeDialog(CredentialModel model, Frame parent) {
    	JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbService = new JLabel("Service: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbService, cs);
 
 
        lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);
 
        pfPassword = new JPasswordField(20);
        pfPassword.setText(model.getPassword());
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
        
        lbUserName = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbUserName, cs);
        
        tfService = new JTextField(20);
        tfService.setText(model.getService());
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfService, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        tfUserName = new JTextField(20);
        tfUserName.setText(model.getUserName());
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(tfUserName, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        changeButton = new JButton("Change");
	    cancelButton = new JButton("Cancel");
	
	        
	    JPanel bp = new JPanel();
	    bp.add(changeButton);
	    bp.add(cancelButton);
	
	    getContentPane().add(panel, BorderLayout.CENTER);
	    getContentPane().add(bp, BorderLayout.PAGE_END);
	
	    pack();
	    setResizable(false);
	    setLocationRelativeTo(parent);
        setModal(true);
	    setVisible(false);
	}
    
    /**
     * Returns the service
     * @return String with the service trimmed.
     */
    public String getService() {
        return tfService.getText().trim();
    }
    
    /**
     * Returns the username
     * @return String with the username trimmed.
     */
    public String getUserName() {
        return tfUserName.getText().trim();
    }

    /**
     * Returns the password
     * @return String with the password trimmed.
     */
    public String getPassword() {
        return new String(pfPassword.getPassword());
    }

    /**
     * Checks whether user pressed change or cancel button and tells controller to do the appropriate action
     * 
     * @param controller The action listener
     * @param windowListener The window listener
     */
    public void registerListener(ChangeCredentialController controller, UpdateTableWindowListener windowListener) {
    	changeButton.setActionCommand("" + controller.CHANGE_CREDENTIAL);
    	changeButton.addActionListener(controller);
    	cancelButton.setActionCommand("" + controller.CANCEL);
    	cancelButton.addActionListener(controller);
        this.addWindowListener(windowListener);
    }
    
}
    

