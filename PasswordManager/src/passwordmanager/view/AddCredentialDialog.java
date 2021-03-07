package passwordmanager.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import passwordmanager.controller.AddCredentialController;
import passwordmanager.controller.UpdateTableWindowListener;

/**
 * This class creates the GUI for adding a credential
 * 
 * @author Arian Alikashani
 * @version 2021-03-07
 *
 */

public class AddCredentialDialog extends JDialog {
	
	/**
	 * Label for the website JTextField
	 */
    private JLabel lbWebsite;
    /**
     * Label for the email JTextField
     */
    private JLabel lbEmail;
    /**
     * Label for the password JTextField
     */
    private JLabel lbPassword;
    /**
     * Text field for the website
     */
    private JTextField tfWebsite;
    /**
     * Text field for the E-mail
     */
    private JTextField tfEmail;
    /**
     * Password field for the password
     */
    private JPasswordField pfPassword;
    /**
     * Used to add a credential
     */
    private JButton addButton;
    /**
     * Used to cancel and close the window
     */
    private JButton cancelButton;
    
    /**
     * The constructor uses a parent frame to create and display the dialog for adding a credential
     * @param parent The parent on which to create this dialog upon
     */
    public AddCredentialDialog(Frame parent) {
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbWebsite = new JLabel("Service: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbWebsite, cs);
 
        tfWebsite = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfWebsite, cs);
 
        lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);
 
        pfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
        
        lbEmail = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbEmail, cs);
 
        tfEmail = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(tfEmail, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        addButton = new JButton("Add");
        cancelButton = new JButton("Cancel");

        JPanel bp = new JPanel();
        bp.add(addButton);
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
     * Returns the website
     * @return String with the website trimmed.
     */
    public String getWebsite() {
        return tfWebsite.getText().trim();
    }
    /**
     * Returns the email
     * @return Strimg with the email trimmed.
     */
    public String getEmail() {
        return tfEmail.getText().trim();
    }
    
    /**
     * Returns the password
     * @return String with the password.
     */
    public String getPassword() {
        return new String(pfPassword.getPassword());
    }

    /**
     * Checks whether user pressed add or cancel button and tells controller to do the appropriate action
     * 
     * @param controller The action listener
     * @param windowListener The window listener
     */
    public void registerListener(AddCredentialController controller, UpdateTableWindowListener windowListener) {
    	addButton.setActionCommand("" + controller.ADD_CREDENTIAL);
    	addButton.addActionListener(controller);
    	cancelButton.setActionCommand("" + controller.CANCEL);
    	cancelButton.addActionListener(controller);
        this.addWindowListener(windowListener);
    }
}
