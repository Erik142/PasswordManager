package passwordmanager.view;

import java.awt.*;
import javax.swing.*;

import passwordmanager.controller.ForgotPasswordDialogController;
import passwordmanager.model.ForgotPasswordModel;
import passwordmanager.model.Observer;

/**
 * This class creates the GUI for forgotten passwords
 * 
 * @author Arian Alikashani
 * @version 2021-03-07
 *
 */
public class ForgotPasswordDialog extends JDialog implements Observer<ForgotPasswordModel> {
	
	private static final long serialVersionUID = -1191660850628433498L;
	private JLabel lbEmail;
    private JButton btnSend;
    private JButton btnCancel;
    private JTextField tfEmail;
    
    
    /**
     * The constructor uses a parent frame to create and display the dialog for forgotten passwords
     * @param parent
     * @param model
     */
    public ForgotPasswordDialog(Frame parent, ForgotPasswordModel model) {
    	super(parent, true);
    	
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
        
        setModal(true);
        setVisible(false);
    }
    
    /**
     * Checks whether user pressed send or cancel button and tells controller to do the appropriate action
     * 
     * @param controller
     */
    public void registerListener(ForgotPasswordDialogController controller) {
    	btnSend.setActionCommand(controller.SEND_MAIL_COMMAND);
    	btnSend.addActionListener(controller);
    	btnCancel.setActionCommand(controller.CANCEL_COMMAND);
    	btnCancel.addActionListener(controller);
    }

    /**
     * Returns the email
     * @return String with the email
     */
    public String getEmail() {
    	return tfEmail.getText();
    }
    
	@Override
	public void update(ForgotPasswordModel observable) {
		tfEmail.setText(observable.getEmail());
	}
}
