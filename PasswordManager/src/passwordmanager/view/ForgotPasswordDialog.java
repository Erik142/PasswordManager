package passwordmanager.view;

import java.awt.*;
import javax.swing.*;

import passwordmanager.controller.ForgotPasswordDialogController;

/**
 * This class creates the GUI for forgotten passwords
 * 
 * @author Arian Alikashani
 * @version 2021-03-07
 *
 */
public class ForgotPasswordDialog extends JDialog {
	
	private static final long serialVersionUID = -1191660850628433498L;
	/**
	 * Label for the email JTextField
	 */
	private JLabel lbEmail;
	/**
	 * Used to trigger a "forgot password" email from the server
	 */
    private JButton btnSend;
    /**
     * Used to cancel and close the dialog
     */
    private JButton btnCancel;
    /**
     * Text field for the email
     */
    private JTextField tfEmail;
    
    
    /**
     * The constructor uses a parent frame to create and display the dialog for forgotten passwords
     * @param parent The parent frame for this dialog
     */
    public ForgotPasswordDialog(Frame parent) {
    	super(parent, true);
    	
    	JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbEmail = new JLabel("Email: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbEmail, cs);
 
        tfEmail = new JTextField(20);
        tfEmail.setText("");
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfEmail, cs);
        
        btnSend = new JButton("Send new password to my email");
        
        btnCancel = new JButton("Cancel");
        
        JPanel bp = new JPanel();
        bp.add(btnSend);
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
     * Returns the email
     * @return String with the email
     */
    public String getEmail() {
    	return tfEmail.getText();
    }
    
    /**
     * Checks whether user pressed send or cancel button and tells controller to do the appropriate action
     * 
     * @param controller The action listener
     */
    public void registerListener(ForgotPasswordDialogController controller) {
    	btnSend.setActionCommand("" + controller.SEND_MAIL);
    	btnSend.addActionListener(controller);
    	btnCancel.setActionCommand("" + controller.CANCEL);
    	btnCancel.addActionListener(controller);
    }
}
