package passwordmanager.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import passwordmanager.controller.ChangeCredentialController;
import passwordmanager.controller.UpdateTableWindowListener;
import passwordmanager.model.ManipulateCredentialModel;
import passwordmanager.model.Observer;

public class ChangeCredentialDialog extends JDialog implements Observer<ManipulateCredentialModel> {
	
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
    
    public ChangeCredentialDialog(Frame parent, ManipulateCredentialModel model) {
    	this.parent = parent;
    	showChangeDialog(model);
    }
        
    public void showChangeDialog(ManipulateCredentialModel model) {
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

    public String getService() {
        return tfService.getText().trim();
    }
    public String getUserName() {
        return tfUserName.getText().trim();
    }

    public String getPassword() {
        return new String(pfPassword.getPassword());
    }

    public void registerListener(ChangeCredentialController controller, UpdateTableWindowListener windowListener) {
    	changeButton.setActionCommand("" + controller.CHANGE_CREDENTIAL);
    	changeButton.addActionListener(controller);
    	cancelButton.setActionCommand("" + controller.CANCEL);
    	cancelButton.addActionListener(controller);
        this.addWindowListener(windowListener);
    }
    
	@Override
	public void update(ManipulateCredentialModel observable) {
		this.tfService.setText(observable.getService());
		this.tfUserName.setText(observable.getUserName());
		this.pfPassword.setText(observable.getPassword());
	}
	
}
    

