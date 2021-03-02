package passwordmanager.changecredential;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeCredentialController implements ActionListener {
	
	public final String CHANGE_COMMAND = "ChangeClickCommand";
	public final String CANCEL_COMMAND = "ChangeCancelCommand";
	
	private ChangeDialog view;
	private ManipulateCredentialModel model;
	
	public ChangeCredentialController(ChangeDialog view, ManipulateCredentialModel model) {
		this.view = view;
		this.model = model;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == CHANGE_COMMAND) {
			String url = view.getWebsite();
			String username = view.getEmail();
			String password = view.getPassword();
			
			model.updateCredential(url, username, password);
		} else if (e.getActionCommand() == CANCEL_COMMAND) {
			model.setChangeViewVisibilityStatus(false);
		}
	}

}
