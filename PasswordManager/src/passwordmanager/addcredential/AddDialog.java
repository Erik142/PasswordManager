package passwordmanager.addcredential;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import passwordmanager.Observer;
import passwordmanager.util.StringExtensions;


public class AddDialog extends JDialog implements Observer<AddCredentialModel> {
	

    private JLabel lbWebsite;
    private JLabel lbEmail;
    private JLabel lbPassword;
    private JTextField tfWebsite;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton addButton;
    private JButton cancelButton;
    
    public AddDialog(Frame parent) {
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbWebsite = new JLabel("Website: ");
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
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);
 
        pfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
        
        lbEmail = new JLabel("Email: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lbEmail, cs);
 
        tfEmail = new JTextField(20);
        cs.gridx = 2;
        cs.gridy = 2;
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
    }
    
    public String getWebsite() {
        return tfWebsite.getText().trim();
    }
    public String getEmail() {
        return tfEmail.getText().trim();
    }

    
    public String getPassword() {
        return new String(pfPassword.getPassword());
    }

    public void registerListener(AddCredentialController controller) {
    	addButton.setActionCommand(controller.ADD_COMMAND);
    	addButton.addActionListener(controller);
    	cancelButton.setActionCommand(controller.CANCEL_COMMAND);
    	cancelButton.addActionListener(controller);
    }
    
	@Override
	public void update(AddCredentialModel observable) {
		String dialogMessage = observable.getDialogMessage();
		int dialogType = observable.getDialogErrorStatus() ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
		
		if (!StringExtensions.isNullOrEmpty(dialogMessage)) {
			JOptionPane.showMessageDialog(null,
	                dialogMessage ,
	                "Add credential",
	                dialogType);
		}
		
		tfWebsite.setText(observable.getUrl());
		tfEmail.setText(observable.getUserName());
		pfPassword.setText(observable.getPassword());
		
		setModal(observable.getVisibilityStatus());
		setVisible(observable.getVisibilityStatus());
	}
}
