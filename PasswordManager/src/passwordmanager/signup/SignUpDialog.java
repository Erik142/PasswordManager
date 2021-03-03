package passwordmanager.signup;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import passwordmanager.Observer;
import passwordmanager.util.StringExtensions;

public class SignUpDialog extends JDialog implements Observer<SignUpModel> {
    private JTextField tfUsername;
    private JPasswordField pfPassword1;
    private JPasswordField pfPassword2;
    private JLabel lbUsername;
    private JLabel lbPassword1;
    private JLabel lbPassword2;
    private JButton btnSignUp;
    private JButton btnCancel;
    
    public SignUpDialog(Frame parent) {
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbUsername = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);
 
        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfUsername, cs);
 
        lbPassword1 = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword1, cs);
 
        pfPassword1 = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPassword1, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
        
        lbPassword2 = new JLabel("Repeat Password: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lbPassword2, cs);
 
        pfPassword2 = new JPasswordField(20);
        cs.gridx = 2;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(pfPassword2, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        btnSignUp = new JButton("Sign Up");
 
        btnCancel = new JButton("Cancel");
        JPanel bp = new JPanel();
        bp.add(btnSignUp);
        bp.add(btnCancel);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
        setModal(false);
        setVisible(false);
    }
    

    public String getEmail() {
    	return tfUsername.getText();
    }
    
	public String getPassword() {
		return new String(pfPassword1.getPassword());
	}

	public String getConfirmPassword() {
		return new String(pfPassword2.getPassword());
	}

	public void registerListener(SignUpController controller, SignUpWindowController componentController) {
		btnSignUp.setActionCommand(controller.SIGNUP_COMMAND);
		btnSignUp.addActionListener(controller);
		btnCancel.setActionCommand(controller.CANCEL_COMMAND);
		btnCancel.addActionListener(controller);
		
		this.addComponentListener(componentController);
	}
	
	@Override
	public void update(SignUpModel observable) {
		String dialogMessage = observable.getDialogMessage();
		boolean success = observable.getStatus();
		
		if (!StringExtensions.isNullOrEmpty(dialogMessage)) {
			int messageType = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
			
			JOptionPane.showMessageDialog(this,
				dialogMessage,
                "Sign Up",
                messageType);
		}
		
		tfUsername.setText(observable.getEmail());
		pfPassword1.setText(observable.getPassword());
		pfPassword2.setText(observable.getConfirmPassword());
		
		this.setModal(observable.getIsViewVisible());
		this.setVisible(observable.getIsViewVisible());
	}
    

}
