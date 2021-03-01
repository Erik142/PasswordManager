package passwordmanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import passwordmanager.config.Configuration;

public class MainViewController {
	private MainView parentView;
	private PasswordClient client;
	
	public MainViewController(MainView parentView, PasswordClient client) throws IOException {
		this.parentView = parentView;
		this.client = client;
	}
	
	public ActionListener addButton() {
		return new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	AddDialog addDlg = new AddDialog(parentView.getFrame());
                addDlg.setVisible(true);
            
                
            }
        };
	}
	
	public ActionListener changeButton() {
		return new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	if(parentView.getTable().getSelectedRow()==-1) {
            		JOptionPane.showMessageDialog(null,
                            "Please select a row",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
            		return;
            		
            	}
                ChangeDialog changeDlg = new ChangeDialog(parentView.getFrame(), getCred(parentView.getTable()));
                changeDlg.setVisible(true);

            
                
            }
        };
	}
	public ActionListener removeButton() {
		return new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	if(parentView.getTable().getSelectedRow()==-1) {
            		JOptionPane.showMessageDialog(null,
                            "Please select a row",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
            		return;
            		
            	}
            	int reply = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete the password for " +parentView.getTable().getValueAt(parentView.getTable().getSelectedRow(),0).toString() + "?" ,
                        "Remove",
                        JOptionPane.YES_NO_OPTION);
            	if (reply == JOptionPane.YES_OPTION) {
            	    //TODO - Remove credential code
            	} else {
            		
            	} 
        		
                
            }
        };
	}
	
	public ActionListener deleteAccountButton() {
		return new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	int reply = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete your account?" ,
                        "Delete Account",
                        JOptionPane.YES_NO_OPTION);
            	if (reply == JOptionPane.YES_OPTION) {
            	    //TODO - Delete account and sign out
            	} else {
            		
            	} 
                
            }
        };
	}
	
	public ActionListener changePasswordButton() {
		return new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	ChangePasswordDialog changePasswordDlg = new ChangePasswordDialog(this.frame);
                changePasswordDlg.setVisible(true);
            
                
            }
        };
	}
	
	public ActionListener signOutButton() {
		return new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	int reply = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to sign out" ,
                        "Sign Out",
                        JOptionPane.YES_NO_OPTION);
            	if (reply == JOptionPane.YES_OPTION) {
            	    //TODO - sign out
            	} else {
            		
            	} 
            
                
            }

			
        };
	}
	
	public static Credential getCred(JTable table){
	    int row = table.getSelectedRow();
	    if (row == -1){
	    	
	            //popup("select a row to remove");

	    }

	    String  website = table.getValueAt(row,0).toString();
	    String  email = table.getValueAt(row,1).toString();
	    String  password = table.getValueAt(row,2).toString();
	    Credential c=new Credential("user", website, email, password);
	    return c;
	}
	
	public Object[][] getData(UserAccount acc){
		Object[][] data=null;
		//TODO Retrieve passwords for account acc from database
		
		return data;
	}

}
