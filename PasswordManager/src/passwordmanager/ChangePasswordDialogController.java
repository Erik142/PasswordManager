package passwordmanager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ChangePasswordDialogController {
	private ChangePasswordDialog parentView;

	public ChangePasswordDialogController(ChangePasswordDialog parentView)
	{
		this.parentView = parentView;
	}
	public ActionListener errorChangingPassword() {
		return new ActionListener() {
	public void actionPerformed(ActionEvent e) {
    	if (parentView.getPasswordOld().isBlank() || parentView.getPasswordNew().isBlank() || parentView.getPasswordConfirm().isBlank()){
    		
        	JOptionPane.showMessageDialog(parentView,
                    "Please fill all the the Password fields",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }else if(!(parentView.getPasswordNew().equals(parentView.getPasswordConfirm()))){
        	
        	JOptionPane.showMessageDialog(parentView,
                    "Your repeated password is wrong",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        	
        }
        /*  TODO - check the written old password with the actual password save in the database
         *  else if(getPasswordOld(.equals()))) {
        	
        }
        */
	}
	
};
}
	public ActionListener cancelB() {
		return new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        parentView.dispose();
    }
};
	}}
