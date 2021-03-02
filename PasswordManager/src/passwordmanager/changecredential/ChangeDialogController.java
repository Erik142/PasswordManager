package passwordmanager.changecredential;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class ChangeDialogController {
	private ChangeDialog parentView;

	public ChangeDialogController(ChangeDialog parentView)
	{
		this.parentView = parentView;
	}
	
	public ActionListener pleaseFill() {
	return new ActionListener() {
	public void actionPerformed(ActionEvent e) {
		
    	if (parentView.getWebsite().isBlank() || parentView.getEmail().isBlank() || parentView.getPassword().isBlank()){
    		
        	JOptionPane.showMessageDialog(parentView,
                    "Please fill all the credentials",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        else{
        	//TODO-Send the credentials forward and update.
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

	 