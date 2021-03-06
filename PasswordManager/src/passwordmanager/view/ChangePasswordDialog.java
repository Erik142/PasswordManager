package passwordmanager.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import passwordmanager.controller.ChangePasswordController;
import passwordmanager.model.ChangeUserAccountModel;
import passwordmanager.model.Observer;
import passwordmanager.util.StringExtensions;

public class ChangePasswordDialog  extends JDialog implements Observer<ChangeUserAccountModel> {
	/**
	 * 
	 */
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
    
    public ChangePasswordDialog(Frame parent) {
    	this.parent = parent;
    	showChangePasswordDialog();
    }
    
    public void showChangePasswordDialog() {
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
        setVisible(false);
    }
    
    
    public String getPasswordOld() {
        return new String(pfPasswordOld.getPassword()).trim();
    }
    
    public String getPasswordNew() {
        return new String(pfPasswordNew.getPassword()).trim();
    }
    
    
    public String getPasswordConfirm() {
        return new String(pfPasswordConfirm.getPassword()).trim();
    }

    public void registerListener(ChangePasswordController controller) {
    	this.btnChange.setActionCommand(controller.CHANGE_COMMAND);
    	this.btnChange.addActionListener(controller);
    	this.btnCancel.setActionCommand(controller.CANCEL_COMMAND);
    	this.btnCancel.addActionListener(controller);
    }
    
	@Override
	public void update(ChangeUserAccountModel observable) {
		String dialogMessage = observable.getDialogMessage();
		int dialogType = observable.getIsDialogError() ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
		
		if (!StringExtensions.isNullOrEmpty(dialogMessage)) {
			JOptionPane.showMessageDialog(this,
                    dialogMessage,
                    "Change user password",
                    dialogType);
		}
		
		this.setModal(observable.getIsViewVisible());
		this.setVisible(observable.getIsViewVisible());
		
		this.pfPasswordOld.setText(observable.getOldPassword());
		this.pfPasswordNew.setText(observable.getNewPassword());
		this.pfPasswordConfirm.setText(observable.getConfirmPassword());
	}
}
