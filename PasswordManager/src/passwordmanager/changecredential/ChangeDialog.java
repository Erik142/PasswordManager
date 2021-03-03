package passwordmanager.changecredential;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import passwordmanager.Credential;
import passwordmanager.Observer;
import passwordmanager.util.StringExtensions;

public class ChangeDialog extends JDialog implements Observer<ManipulateCredentialModel> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lbService;
    private JLabel lbUserName;
    private JLabel lbPassword;
	private JTextField tfService;
    private JTextField tfUserName;
    private JPasswordField pfPassword;
    private JButton changeButton;
    private JButton cancelButton;
    
    private Frame parent;
    
    public ChangeDialog(Frame parent) {
    	this.parent = parent;
    	showChangeDialog();
    }
        
    public void showChangeDialog() {
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
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfService, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        tfUserName = new JTextField(20);
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
	    setVisible(false);
	}

    public String getService() {
        return tfService.getText().trim();
    }
    public String getUserName() {
        return tfUserName.getText().trim();
    }

    public String getPassword() {
        return new String(pfPassword.getPassword());
    }

    public void registerListener(ChangeCredentialController controller, ChangeCredentialComponentListener componentListener) {
    	changeButton.setActionCommand(controller.CHANGE_COMMAND);
    	changeButton.addActionListener(controller);
    	cancelButton.setActionCommand(controller.CANCEL_COMMAND);
    	cancelButton.addActionListener(controller);
    	this.addComponentListener(componentListener);
    }
    
	@Override
	public void update(ManipulateCredentialModel observable) {
		String dialogMessage = observable.getDialogMessage();
		int dialogType = observable.getIsDialogError() ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
		
		if (!StringExtensions.isNullOrEmpty(dialogMessage)) {
			JOptionPane.showMessageDialog(this,
                    dialogMessage,
                    "Change credential",
                    dialogType);
		}
		
		setModal(observable.getChangeViewVisibilityStatus());
		setVisible(observable.getChangeViewVisibilityStatus());
		
		this.tfService.setText(observable.getService());
		this.tfUserName.setText(observable.getUserName());
		this.pfPassword.setText(observable.getPassword());
	}
	
}
    

