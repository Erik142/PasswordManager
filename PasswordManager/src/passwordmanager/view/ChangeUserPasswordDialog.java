package passwordmanager.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import passwordmanager.controller.ChangePasswordController;
import passwordmanager.model.Observer;

/**
 * This class creates the GUI for changing a user's password
 * 
 * @author Arian Alikashani
 * @version 2021-03-07
 *
 */
public class ChangeUserPasswordDialog  extends JDialog {
	
	private static final long serialVersionUID = 1L;
	/**
	 * Label for the "old password" JTextField
	 */
	private JLabel lbOld;
	/**
	 * Label for the "new password" JTextField
	 */
	private JLabel lbNew;
	/**
	 * Label for the "confirm password" JTextField
	 */
	private JLabel lbConfirm;
	/**
	 * Password field for the "old password"
	 */
	private JPasswordField pfPasswordOld;
	/**
	 * Password field for the "new password"
	 */
	private JPasswordField pfPasswordNew;
	/**
	 * Password field for "confirm password"
	 */
	private JPasswordField pfPasswordConfirm;
	/**
	 * Used to change the user account password
	 */
    private JButton btnChange;
    /**
     * Used to cancel and close the dialog
     */
    private JButton btnCancel;
    
    /**
     * The constructor uses a parent frame to create and display the dialog for changing a user's password
     * @param parent The parent frame which will be used as a parent for this dialog
     */
    public ChangeUserPasswordDialog(Frame parent) {
    	showChangePasswordDialog(parent);
    }
    
    private void showChangePasswordDialog(Frame parent) {
    	JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbOld = new JLabel("Old Password: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbOld, cs);
        
        lbNew = new JLabel("New Password ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbNew, cs);
        
        lbConfirm = new JLabel("Repeat new Password:");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lbConfirm, cs);
 
        pfPasswordOld = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(pfPasswordOld, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
        
        pfPasswordNew = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPasswordNew, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
        
        pfPasswordConfirm = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(pfPasswordConfirm, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
        
        btnChange = new JButton("Change my password");
        btnCancel = new JButton("Cancel");
        
        JPanel bp = new JPanel();
        bp.add(btnChange);
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
     * Returns the old password
     * @return String with the old password trimmed.
     */
    public String getPasswordOld() {
        return new String(pfPasswordOld.getPassword()).trim();
    }
    
    /**
     * Returns the new password
     * @return String with the new password trimmed.
     */
    public String getPasswordNew() {
        return new String(pfPasswordNew.getPassword()).trim();
    }
    
    /**
     * Returns the confirmation password
     * @return String with the confirmation password trimmed.
     */
    public String getPasswordConfirm() {
        return new String(pfPasswordConfirm.getPassword()).trim();
    }

    /**
     * Checks whether user pressed change or cancel button and tells controller to do the appropriate action
     * 
     * @param controller The action listener
     */
    public void registerListener(ChangePasswordController controller) {
    	this.btnChange.setActionCommand("" + controller.CHANGE_PASSWORD);
    	this.btnChange.addActionListener(controller);
    	this.btnCancel.setActionCommand("" + controller.CANCEL);
    	this.btnCancel.addActionListener(controller);
    }
}
