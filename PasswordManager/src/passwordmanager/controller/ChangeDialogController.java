package passwordmanager.controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import passwordmanager.view.ChangeDialog;

public class ChangeDialogController {
	private ChangeDialog parentView;

	public ChangeDialogController(ChangeDialog parentView)
	{
		this.parentView = parentView;
	}
	
	public ActionListener pleaseFill() {
	return new ActionListener() {
	public void actionPerformed(ActionEvent e) {
		
    	if (parentView.getService().isBlank() || parentView.getUserName().isBlank() || parentView.getPassword().isBlank()){
    		
        	JOptionPane.showMessageDialog(parentView,
                    "Please fill all the credentials",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        else{
        	parentView.dispose();
        }
        
    }
	
	};
	}
	public ActionListener cancelB() {
		return new ActionListener() {
	public void actionPerformed(ActionEvent e) {
        parentView.dispose();
    }
};
}
}

	 