package passwordmanager;

import java.awt.*;
import javax.swing.*;

public class ForgotPasswordDialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1191660850628433498L;
	private JLabel lbEmail;
    private JButton btnSend;
    private JButton btnCancel;
    
    protected JTextField tfEmail;
    
    private ForgotPasswordDialogController controller;
    
    public ForgotPasswordDialog(Frame parent, PasswordClient client) {
    	controller = new ForgotPasswordDialogController(this, client);
    	
    	JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbEmail = new JLabel("Email: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbEmail, cs);
 
        tfEmail = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfEmail, cs);
        
        btnSend = new JButton("Send new password to my email");
        btnSend.addActionListener(controller.forgotPassword());
        
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(controller.cancelButtonClick());
        
        JPanel bp = new JPanel();
        bp.add(btnSend);
        bp.add(btnCancel);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
}
