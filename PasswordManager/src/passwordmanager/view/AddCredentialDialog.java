package passwordmanager.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import passwordmanager.controller.AddCredentialController;
import passwordmanager.controller.UpdateTableWindowListener;
import passwordmanager.model.AddCredentialModel;
import passwordmanager.model.Observer;


public class AddCredentialDialog extends JDialog implements Observer<AddCredentialModel> {
	

    private JLabel lbWebsite;
    private JLabel lbEmail;
    private JLabel lbPassword;
    private JTextField tfWebsite;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton addButton;
    private JButton cancelButton;
    
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
    
    public String getWebsite() {
        return tfWebsite.getText().trim();
    }
    public String getEmail() {
        return tfEmail.getText().trim();
    }
    
    public String getPassword() {
        return new String(pfPassword.getPassword());
    }

    public void registerListener(AddCredentialController controller, UpdateTableWindowListener windowListener) {
    	addButton.setActionCommand("" + controller.ADD_CREDENTIAL);
    	addButton.addActionListener(controller);
    	cancelButton.setActionCommand("" + controller.CANCEL);
    	cancelButton.addActionListener(controller);
        this.addWindowListener(windowListener);
    }
    
	@Override
	public void update(AddCredentialModel observable) {	
		tfWebsite.setText(observable.getUrl());
		tfEmail.setText(observable.getUserName());
		pfPassword.setText(observable.getPassword());
	}
}
