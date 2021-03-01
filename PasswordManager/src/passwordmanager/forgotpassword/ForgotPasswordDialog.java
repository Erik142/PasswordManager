package passwordmanager.forgotpassword;

import java.awt.*;
import javax.swing.*;

import passwordmanager.Observer;
import passwordmanager.util.StringExtensions;

public class ForgotPasswordDialog extends JDialog implements Observer<ForgotPasswordModel> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1191660850628433498L;
	private JLabel lbEmail;
    private JButton btnSend;
    private JButton btnCancel;
    private JTextField tfEmail;

    private Frame parent;
    
    public ForgotPasswordDialog(Frame parent, ForgotPasswordModel model) {
    	this.parent = parent;
    	
    	JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbEmail = new JLabel("Email: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbEmail, cs);
 
        tfEmail = new JTextField(20);
        tfEmail.setText(model.getEmail());
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
        
        this.setVisible(model.getIsViewVisible());
    }
    
    public void registerListener(ForgotPasswordDialogController controller, ForgotPasswordWindowController windowController) {
    	btnSend.setActionCommand(controller.SEND_MAIL_COMMAND);
    	btnSend.addActionListener(controller);
    	btnCancel.setActionCommand(controller.CANCEL_COMMAND);
    	btnCancel.addActionListener(controller);
    	this.addWindowListener(windowController);
    }

    public String getEmail() {
    	return tfEmail.getText();
    }
    
	@Override
	public void update(ForgotPasswordModel observable) {
		// TODO Auto-generated method stub
		String dialogMessage = observable.getDialogMessage();
		
		if (!StringExtensions.isNullOrEmpty(dialogMessage)) {
			JOptionPane.showMessageDialog(parent,
		            dialogMessage,
		            "Forgot password",
		            JOptionPane.INFORMATION_MESSAGE);
		}
		
		tfEmail.setText(observable.getEmail());
		
		this.setVisible(observable.getIsViewVisible());
	}
}
