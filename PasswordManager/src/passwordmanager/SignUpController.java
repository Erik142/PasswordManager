package passwordmanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import passwordmanager.config.Configuration;

public class SignUpController {
    private boolean succeeded;
	private SignUpDialog parentView;
	private PasswordClient client;

	public SignUpController(SignUpDialog parentView, Configuration config) throws IOException {
		this.parentView = parentView;
		this.client = new PasswordClient(config);
	}
	
	public ActionListener signUpButton() {
		return new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            	if (comparePassword() && checkEmpty()) {
            		
                	JOptionPane.showMessageDialog(parentView,
                            "Success",
                            "Sign Up",
                            JOptionPane.INFORMATION_MESSAGE);
                	succeeded = true;
                	parentView.dispose();
            		
                }
                else {
                	
                    JOptionPane.showMessageDialog(parentView,
                            "Passwords don't match or password field is empty",
                            "Sign Up",
                            JOptionPane.ERROR_MESSAGE);
                    // reset username and password
                    parentView.getPfPassword1().setText("");
                    parentView.getPfPassword2().setText("");
                    succeeded = false;
                	
                	
                }
            }
        };
        
        
	}
	
	public ActionListener cancelButton () {
		return new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                parentView.dispose();
            }
		};
	}
    
    public boolean isSucceeded() {
        return succeeded;
    }
    
    public boolean comparePassword() {
    	if(new String(parentView.getPfPassword1().getPassword()).trim().equals(new String(parentView.getPfPassword2().getPassword()).trim()) && new String(parentView.getPfPassword1().getPassword()).trim() != "") {
    		return true;
    		
    		
    	}else {	return false;}
    	
    }
    
    public boolean checkEmpty() {
    	if(new String(parentView.getPfPassword1().getPassword()).length()==0){
    		return false;
    	}
    	
		return true;
    	
    }
    

}
