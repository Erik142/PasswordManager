package passwordmanager.mainview;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import passwordmanager.addcredential.AddCredentialModel;

public class AddCredentialButtonController implements ActionListener {
	private AddCredentialModel model;
	
	public AddCredentialButtonController(AddCredentialModel model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		model.reset();
		model.setVisibilityStatus(true);
	}
}
