package passwordmanager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ChangePasswordDialog  extends JDialog{
	private JLabel lbOld;
	private JLabel lbNew;
	private JLabel lbConfirm;
	private JPasswordField pfPasswordOld;
	private JPasswordField pfPasswordNew;
	private JPasswordField pfPasswordConfirm;
    private JButton btnChange;
    private JButton btnCancel;
    
    public ChangePasswordDialog(Frame parent) {
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
        
        
        
        btnChange.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
            	if (getPasswordOld().isBlank() || getPasswordNew().isBlank() || getPasswordConfirm().isBlank()){
            		
                	JOptionPane.showMessageDialog(ChangePasswordDialog.this,
                            "Please fill all the the Password fields",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }else if(!(getPasswordNew().equals(getPasswordConfirm()))){
                	
                	JOptionPane.showMessageDialog(ChangePasswordDialog.this,
                            "Your repeated password is wrong",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                	
                }
                /*  TODO - check the written old password with the actual password save in the database
                 *  else if(getPasswordOld(.equals()))) {
                	
                }
                */
        	}
        	
        });
        
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        JPanel bp = new JPanel();
        bp.add(btnChange);
        bp.add(btnCancel);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
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
}
