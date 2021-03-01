package passwordmanager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ChangeDialog extends JDialog{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lbWebsite;
    private JLabel lbEmail;
    private JLabel lbPassword;
	private JTextField tfWebsite;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton changeButton;
    private JButton cancelButton;
    private ChangeDialogController controller;
    
    private Frame parent;
    
    public ChangeDialog(Frame parent, Credential c) {
    	this.parent = parent;
    	controller = new  ChangeDialogController(this);
    	showChangeDialog(c);
    }
        
    public void showChangeDialog(Credential c) {
    	JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbWebsite = new JLabel("Website: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbWebsite, cs);
 
 
        lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);
 
        pfPassword = new JPasswordField(20);
        pfPassword.setText(c.getPassword());
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
        
        tfWebsite = new JTextField(20);
        tfWebsite.setText(c.getURL());
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfWebsite, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        tfEmail = new JTextField(20);
        tfEmail.setText(c.getUsername());
        cs.gridx = 2;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(tfEmail, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        changeButton = new JButton("Change");
    changeButton.addActionListener(controller.pleaseFill()); 
    	 
        
    cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(controller.cancelB()); 

        
    JPanel bp = new JPanel();
    bp.add(changeButton);
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
	
}
    

