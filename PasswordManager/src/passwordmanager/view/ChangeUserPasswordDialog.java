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
	private JLabel lbOld;
	private JLabel lbNew;
	private JLabel lbConfirm;
	private JPasswordField pfPasswordOld;
	private JPasswordField pfPasswordNew;
	private JPasswordField pfPasswordConfirm;
    private JButton btnChange;
    private JButton btnCancel;
    
    private final Frame parent;
    
    /**
     * The constructor uses a parent frame to create and display the dialog for changing a user's password
     * @param parent
     */
    public ChangeUserPasswordDialog(Frame parent) {
    	this.parent = parent;
    	showChangePasswordDialog();
    }
    
    private void showChangePasswordDialog() {
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
     * @param controller
     */
    public void registerListener(ChangePasswordController controller) {
    	this.btnChange.setActionCommand("" + controller.CHANGE_PASSWORD);
    	this.btnChange.addActionListener(controller);
    	this.btnCancel.setActionCommand("" + controller.CANCEL);
    	this.btnCancel.addActionListener(controller);
    }
}
